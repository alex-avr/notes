package org.avr.notes.repo.test

import org.avr.notes.common.repo.folder.*
import org.avr.notes.common.repo.note.*

class RepositoryMock(
    private val invokeCreateFolder: (DbFolderRequest) -> DbFolderResponse = { DbFolderResponse.MOCK_SUCCESS_EMPTY },
    private val invokeGetFolderInfo: (DbFolderIdRequest) -> DbFolderResponse = { DbFolderResponse.MOCK_SUCCESS_EMPTY },
    private val invokeGetFolderChildren: (DbFolderIdRequest) -> DbFolderChildrenResponse = { DbFolderChildrenResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateFolder: (DbFolderRequest) -> DbFolderResponse = { DbFolderResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteFolder: (DbFolderIdRequest) -> DbFolderResponse = { DbFolderResponse.MOCK_SUCCESS_EMPTY },

    private val invokeCreateNote: (DbNoteRequest) -> DbNoteResponse = { DbNoteResponse.MOCK_SUCCESS_EMPTY },
    private val invokeGetNote: (DbNoteIdRequest) -> DbNoteResponse = { DbNoteResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateNote: (DbNoteRequest) -> DbNoteResponse = { DbNoteResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteNote: (DbNoteIdRequest) -> DbNoteResponse = { DbNoteResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchNotes: (DbNoteFilterRequest) -> DbNoteListResponse = { DbNoteListResponse.MOCK_SUCCESS_EMPTY },
) : IFolderRepository, INoteRepository {
    override suspend fun createFolder(request: DbFolderRequest): DbFolderResponse {
        return invokeCreateFolder(request)
    }

    override suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse {
        return invokeGetFolderInfo(request)
    }

    override suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse {
        return invokeGetFolderChildren(request)
    }

    override suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse {
        return invokeUpdateFolder(request)
    }

    override suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse {
        return invokeDeleteFolder(request)
    }

    override suspend fun createNote(request: DbNoteRequest): DbNoteResponse {
        return invokeCreateNote(request)
    }

    override suspend fun getNote(request: DbNoteIdRequest): DbNoteResponse {
        return invokeGetNote(request)
    }

    override suspend fun updateNote(request: DbNoteRequest): DbNoteResponse {
        return invokeUpdateNote(request)
    }

    override suspend fun deleteNote(request: DbNoteIdRequest): DbNoteResponse {
        return invokeDeleteNote(request)
    }

    override suspend fun searchNotes(request: DbNoteFilterRequest): DbNoteListResponse {
        return invokeSearchNotes(request)
    }
}