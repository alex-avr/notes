package org.avr.notes.common.repo.note

import org.avr.notes.common.models.Note
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.repo.IDbResponse

data class DbNoteListResponse(
    override val data: List<Note>? = emptyList(),
    override val isSuccess: Boolean = true,
    override val errors: List<NotesError> = emptyList()
) : IDbResponse<List<Note>> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbNoteListResponse(emptyList(), true)

        fun success(result: List<Note>) = DbNoteListResponse(result, true)
        fun error(errors: List<NotesError>) = DbNoteListResponse(null, false, errors)
        fun error(error: NotesError) = DbNoteListResponse(null, false, listOf(error))
    }
}
