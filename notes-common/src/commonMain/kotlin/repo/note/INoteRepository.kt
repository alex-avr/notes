package org.avr.notes.common.repo.note

/**
 * Интерфейс репозитория для работы с заметками
 */
interface INoteRepository {
    suspend fun createNote(request: DbNoteRequest): DbNoteResponse

    suspend fun getNote(request: DbNoteIdRequest): DbNoteResponse

    suspend fun updateNote(request: DbNoteRequest): DbNoteResponse

    suspend fun deleteNote(request: DbNoteIdRequest): DbNoteResponse

    suspend fun searchNotes(request: DbNoteFilterRequest): DbNoteListResponse
}