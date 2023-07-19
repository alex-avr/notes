package org.avr.notes.repo.postgresql

import org.avr.notes.common.helpers.asNotesError
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.repo.IRepositoryFactory
import org.avr.notes.common.repo.folder.*
import org.avr.notes.common.repo.note.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class RepoSql(
    properties: SqlProperties,
    initObjects: Collection<IFolderChild> = emptyList()
) : IRepositoryFactory, IFolderRepository, INoteRepository {

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
        }

        Database.connect(
            properties.url, driver, properties.user, properties.password
        )

        transaction {
            if (properties.dropDatabase) {
                SchemaUtils.drop(FolderTable)
                SchemaUtils.drop(NoteTable)
            }
            SchemaUtils.create(FolderTable)
            SchemaUtils.create(NoteTable)

            initObjects.forEach { createObject(it) }
        }
    }

    override val folderRepository: IFolderRepository
        get() = this
    override val noteRepository: INoteRepository
        get() = this

    override suspend fun createFolder(request: DbFolderRequest): DbFolderResponse = transactionFolderWrapper {
        DbFolderResponse.success(doCreateFolder(request.folder))
    }

    override suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse = transactionFolderWrapper {
        doGetFolderInfo(request.id)
    }

    override suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse =
        transactionFolderChildrenWrapper {
            doGetFolderChildren(request.id)
        }

    override suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse = doUpdateFolder(request.folder.id) {
        FolderTable.update({ FolderTable.id eq request.folder.id.toUuid() }) {
            to(it, request.folder.copy())
        }
        doGetFolderInfo(request.folder.id)
    }

    override suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse = doUpdateFolder(request.id) {
        FolderTable.deleteWhere { id eq request.id.toUuid() }
        DbFolderResponse.success(it)
    }

    override suspend fun createNote(request: DbNoteRequest): DbNoteResponse = transactionNoteWrapper {
        DbNoteResponse.success(doCreateNote(request.note))
    }

    override suspend fun getNote(request: DbNoteIdRequest): DbNoteResponse = transactionNoteWrapper {
        doGetNote(request.id)
    }

    override suspend fun updateNote(request: DbNoteRequest): DbNoteResponse = doUpdateNote(request.note.id) {
        NoteTable.update({ NoteTable.id eq request.note.id.toUuid() }) {
            to(it, request.note.copy())
        }
        doGetNote(request.note.id)
    }

    override suspend fun deleteNote(request: DbNoteIdRequest): DbNoteResponse = doUpdateNote(request.id) {
        NoteTable.deleteWhere { id eq request.id.toUuid() }
        DbNoteResponse.success(it)
    }

    override suspend fun searchNotes(request: DbNoteFilterRequest): DbNoteListResponse = transactionNoteListWrapper {
        doSearchNotes(request.searchFilter)
    }

    private fun doSearchNotes(searchFilter: String): DbNoteListResponse {
        val noteList = NoteTable.select {
            (NoteTable.title ilike searchFilter).or { NoteTable.body ilike searchFilter }
        }.map { NoteTable.from(it) }
        return DbNoteListResponse.success(noteList)
    }

    private fun createObject(child: IFolderChild) {
        when (child) {
            is Folder -> doCreateFolder(child)
            is Note -> doCreateNote(child)
        }
    }

    private fun doCreateNote(note: Note): Note {
        val res = NoteTable.insert {
            to(it, note)
        }

        return NoteTable.from(res)
    }

    private fun doGetNote(id: NoteId): DbNoteResponse {
        val res = NoteTable.select {
            NoteTable.id eq id.toUuid()
        }.singleOrNull() ?: return DbNoteResponse.errorNoteNotFound
        return DbNoteResponse.success(NoteTable.from(res))
    }

    private fun doUpdateNote(
        id: NoteId,
        block: (Note) -> DbNoteResponse
    ): DbNoteResponse =
        transactionNoteWrapper {
            if (id == NoteId.NONE) return@transactionNoteWrapper DbNoteResponse.errorNoteEmptyId

            val current = NoteTable.select { NoteTable.id eq id.toUuid() }
                .firstOrNull()
                ?.let { NoteTable.from(it) }

            when (current) {
                null -> DbNoteResponse.errorNoteNotFound
                else -> block(current)
            }
        }

    private fun doCreateFolder(folder: Folder): Folder {
        val res = FolderTable.insert {
            to(it, folder)
        }

        return FolderTable.from(res)
    }

    private fun doGetFolderInfo(id: FolderId): DbFolderResponse {
        val res = FolderTable.select {
            FolderTable.id eq id.toUuid()
        }.singleOrNull() ?: return DbFolderResponse.errorFolderNotFound
        return DbFolderResponse.success(FolderTable.from(res))
    }

    private fun doGetFolderChildren(id: FolderId): DbFolderChildrenResponse {
        val folderList = FolderTable.select {
            FolderTable.parentFolderId eq id.toUuid()
        }.map { FolderTable.from(it) }

        val noteList = NoteTable.select {
            NoteTable.parentFolderId eq id.toUuid()
        }.map { NoteTable.from(it) }
        return DbFolderChildrenResponse.success(folderList + noteList)
    }

    private fun doUpdateFolder(
        id: FolderId,
        block: (Folder) -> DbFolderResponse
    ): DbFolderResponse =
        transactionFolderWrapper {
            if (id == FolderId.NONE) return@transactionFolderWrapper DbFolderResponse.errorFolderEmptyId

            val current = FolderTable.select { FolderTable.id eq id.toUuid() }
                .firstOrNull()
                ?.let { FolderTable.from(it) }

            when (current) {
                null -> DbFolderResponse.errorFolderNotFound
                else -> block(current)
            }
        }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionFolderWrapper(block: () -> DbFolderResponse): DbFolderResponse =
        transactionWrapper(block) { DbFolderResponse.error(it.asNotesError()) }

    private fun transactionFolderChildrenWrapper(block: () -> DbFolderChildrenResponse): DbFolderChildrenResponse =
        transactionWrapper(block) { DbFolderChildrenResponse.error(it.asNotesError()) }

    private fun transactionNoteWrapper(block: () -> DbNoteResponse): DbNoteResponse =
        transactionWrapper(block) { DbNoteResponse.error(it.asNotesError()) }


    private fun transactionNoteListWrapper(block: () -> DbNoteListResponse): DbNoteListResponse =
        transactionWrapper(block) { DbNoteListResponse.error(it.asNotesError()) }

}