package org.avr.notes.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.repo.folder.*
import org.avr.notes.common.repo.folder.DbFolderResponse.Companion.errorFolderAlreadyExists
import org.avr.notes.common.repo.folder.DbFolderResponse.Companion.errorFolderEmptyId
import org.avr.notes.common.repo.folder.DbFolderResponse.Companion.errorFolderNotFound
import org.avr.notes.common.repo.note.*
import org.avr.notes.common.repo.note.DbNoteResponse.Companion.errorNoteAlreadyExists
import org.avr.notes.common.repo.note.DbNoteResponse.Companion.errorNoteEmptyId
import org.avr.notes.common.repo.note.DbNoteResponse.Companion.errorNoteNotFound
import org.avr.notes.repo.inmemory.model.FolderEntity
import org.avr.notes.repo.inmemory.model.IEntity
import org.avr.notes.repo.inmemory.model.NoteEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

val cacheTimeToLive = 2.minutes
class RepoInMemory(
    initObjects: List<IFolderChild> = emptyList(),
    ttl: Duration = cacheTimeToLive,
    val randomUuid: () -> String = { uuid4().toString() }
) : IFolderRepository, INoteRepository {
    private val cache = Cache.Builder<String, IEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    override suspend fun createFolder(request: DbFolderRequest): DbFolderResponse {
        val key = randomUuid()
        val folder = request.folder.copy()
        val entity = FolderEntity(folder)
        return mutex.withLock {
            val ent = cache.get(key)
            if (ent == null) {
                cache.put(key, entity)
                DbFolderResponse(
                    data = folder,
                    isSuccess = true,
                )
            } else {
                errorFolderAlreadyExists
            }
        }
    }

    override suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse {
        val key = request.id.takeIf { it != FolderId.NONE }?.asString() ?: return errorFolderEmptyId

        return mutex.withLock {
            cache.get(key)
                ?.let {
                    when(it) {
                        is FolderEntity -> DbFolderResponse(data =  it.toInternal(), isSuccess = true)
                        else -> errorFolderNotFound
                    }
                } ?: errorFolderNotFound
        }
    }

    override suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse {
        val result = mutex.withLock {
            cache.asMap().asSequence()
                .filter { entry ->
                    entry.value.parentFolderId != null && entry.value.parentFolderId == request.id.asString()
                }
                .map { entry -> entry.value }
                .map { entity ->
                    when (entity) {
                        is FolderEntity -> entity.toInternal()
                        is NoteEntity -> entity.toInternal()
                    }
                }.toList()
        }

        return DbFolderChildrenResponse(result, true)
    }

    override suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse {
        val key = request.folder.id.takeIf { it != FolderId.NONE }?.asString() ?: return errorFolderNotFound
        val newFolder = request.folder.copy()
        val entity = FolderEntity(newFolder)
        return mutex.withLock {
            val oldFolder = cache.get(key)
            if (oldFolder != null) {
                cache.put(key, entity)
                DbFolderResponse(
                    data = newFolder,
                    isSuccess = true,
                )
            } else {
                return errorFolderNotFound
            }
        }
    }

    override suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse {
        val key = request.id.takeIf { it != FolderId.NONE }?.asString() ?: return errorFolderNotFound

        return mutex.withLock {
            val oldFolder = cache.get(key)
            if (oldFolder == null) {
                return errorFolderNotFound
            } else {
                cache.invalidate(key)
                when (oldFolder) {
                    is FolderEntity -> DbFolderResponse(
                            data = oldFolder.toInternal(),
                            isSuccess = true,
                        )
                    else -> errorFolderNotFound
                }
            }
        }
    }

    override suspend fun createNote(request: DbNoteRequest): DbNoteResponse {
        val key = randomUuid()
        val note = request.note.copy()
        val entity = NoteEntity(note)
        return mutex.withLock {
            val ent = cache.get(key)
            if (ent == null) {
                cache.put(key, entity)
                DbNoteResponse(
                    data = note,
                    isSuccess = true,
                )
            } else {
                errorNoteAlreadyExists
            }
        }
    }

    override suspend fun getNote(request: DbNoteIdRequest): DbNoteResponse {
        val key = request.id.takeIf { it != NoteId.NONE }?.asString() ?: return errorNoteEmptyId

        return mutex.withLock {
            cache.get(key)
                ?.let {
                    when(it) {
                        is NoteEntity -> DbNoteResponse(data =  it.toInternal(), isSuccess = true)
                        else -> errorNoteNotFound
                    }
                } ?: errorNoteNotFound
        }
    }

    override suspend fun updateNote(request: DbNoteRequest): DbNoteResponse {
        val key = request.note.id.takeIf { it != NoteId.NONE }?.asString() ?: return errorNoteNotFound
        val newFolder = request.note.copy()
        val entity = NoteEntity(newFolder)
        return mutex.withLock {
            val oldNote = cache.get(key)
            if (oldNote != null) {
                cache.put(key, entity)
                DbNoteResponse(
                    data = newFolder,
                    isSuccess = true,
                )
            } else {
                return errorNoteNotFound
            }
        }
    }

    override suspend fun deleteNote(request: DbNoteIdRequest): DbNoteResponse {
        val key = request.id.takeIf { it != NoteId.NONE }?.asString() ?: return errorNoteNotFound

        return mutex.withLock {
            val oldNote = cache.get(key)
            if (oldNote == null) {
                return errorNoteNotFound
            } else {
                cache.invalidate(key)
                when (oldNote) {
                    is NoteEntity -> DbNoteResponse(
                        data = oldNote.toInternal(),
                        isSuccess = true,
                    )
                    else -> errorNoteNotFound
                }
            }
        }
    }

    override suspend fun searchNotes(request: DbNoteFilterRequest): DbNoteListResponse {
        val result = mutex.withLock {
            cache.asMap().asSequence()
                .filter { entry ->
                    entry.value is NoteEntity
                }
                .map { entry -> entry.value as NoteEntity }
                .filter { noteEntity -> noteEntity.title?.contains(request.titleFilter) ?: false }
                .map { it.toInternal() }
                .toList()
        }

        return DbNoteListResponse(result, true)
    }

    private fun save(folderChild: IFolderChild) {
        val entity = when (folderChild) {
            is Folder -> FolderEntity(folderChild)
            is Note -> NoteEntity(folderChild)
        }

        cache.put(entity.id, entity)
    }
}