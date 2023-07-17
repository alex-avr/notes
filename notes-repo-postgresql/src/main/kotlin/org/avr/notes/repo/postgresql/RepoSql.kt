package org.avr.notes.repo.postgresql

import org.avr.notes.common.helpers.asNotesError
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.repo.IRepositoryFactory
import org.avr.notes.common.repo.folder.*
import org.avr.notes.common.repo.note.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    private fun createObject(child: IFolderChild) {
        when (child) {
            is Folder -> createFolder(child)
            is Note -> createNote(child)
        }
    }

    private fun createFolder(folder: Folder): Folder {
        val res = FolderTable.insert {
            to(it, folder)
        }

        return FolderTable.from(res)
    }

    private fun getFolderInfo(ids: FolderId): DbFolderResponse {
        val res = FolderTable.select {
            FolderTable.id eq ids.asString()
        }.singleOrNull() ?: return DbFolderResponse.errorFolderNotFound
        return DbFolderResponse.success(FolderTable.from(res))
    }

    private fun createNote(note: Note): Note {
        val res = NoteTable.insert {
            to(it, note)
        }

        return NoteTable.from(res)
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

    private fun transactionNoteWrapper(block: () -> DbNoteResponse): DbNoteResponse =
        transactionWrapper(block) { DbNoteResponse.error(it.asNotesError()) }
    override val folderRepository: IFolderRepository
        get() = this
    override val noteRepository: INoteRepository
        get() = this

    override suspend fun createFolder(request: DbFolderRequest): DbFolderResponse = transactionFolderWrapper {
        DbFolderResponse.success(request.folder)
    }

    override suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createNote(request: DbNoteRequest): DbNoteResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(request: DbNoteIdRequest): DbNoteResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(request: DbNoteRequest): DbNoteResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(request: DbNoteIdRequest): DbNoteResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchNotes(request: DbNoteFilterRequest): DbNoteListResponse {
        TODO("Not yet implemented")
    }


}