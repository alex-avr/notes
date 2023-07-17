package org.avr.notes.repo.stubs

import org.avr.notes.common.repo.note.*
import org.avr.notes.stub.NoteStub

class StubNoteRepository : INoteRepository {
    override suspend fun createNote(request: DbNoteRequest): DbNoteResponse {
        return DbNoteResponse(
            data = NoteStub.create(),
            isSuccess = true
        )
    }

    override suspend fun getNote(request: DbNoteIdRequest): DbNoteResponse {
        return DbNoteResponse(
            data = NoteStub.get(),
            isSuccess = true
        )
    }

    override suspend fun updateNote(request: DbNoteRequest): DbNoteResponse {
        return DbNoteResponse(
            data = NoteStub.update(),
            isSuccess = true
        )
    }

    override suspend fun deleteNote(request: DbNoteIdRequest): DbNoteResponse {
        return DbNoteResponse(
            data = NoteStub.get(),
            isSuccess = true
        )
    }

    override suspend fun searchNotes(request: DbNoteFilterRequest): DbNoteListResponse {
        return DbNoteListResponse(
            data = NoteStub.searchResults(),
            isSuccess = true
        )
    }
}