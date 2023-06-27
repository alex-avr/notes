package org.avr.notes.common.repo.note

import org.avr.notes.common.helpers.errorAlreadyExists
import org.avr.notes.common.helpers.errorEmptyId
import org.avr.notes.common.helpers.errorNotFound
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.repo.IDbResponse

data class DbNoteResponse(
    override val data: Note?,
    override val isSuccess: Boolean = true,
    override val errors: List<NotesError> = emptyList()
) : IDbResponse<Note> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbNoteResponse(null, true)

        fun success(result: Note) = DbNoteResponse(result, true)

        fun error(errors: List<NotesError>, data: Note? = null) = DbNoteResponse(data, false, errors)

        fun error(error: NotesError, data: Note? = null) = DbNoteResponse(data, false, listOf(error))

        val errorNoteEmptyId = error(errorEmptyId)

        val errorNoteNotFound = error(errorNotFound)

        val errorNoteAlreadyExists = error(errorAlreadyExists)
    }
}
