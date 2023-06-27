package org.avr.notes.common.repo.folder

import org.avr.notes.common.helpers.errorAlreadyExists
import org.avr.notes.common.helpers.errorEmptyId
import org.avr.notes.common.helpers.errorNotFound
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.repo.IDbResponse

data class DbFolderResponse(
    override val data: Folder?,
    override val isSuccess: Boolean = true,
    override val errors: List<NotesError> = emptyList()
) : IDbResponse<Folder> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbFolderResponse(null, true)

        fun success(result: Folder) = DbFolderResponse(result, true)

        fun error(errors: List<NotesError>, data: Folder? = null) = DbFolderResponse(data, false, errors)

        fun error(error: NotesError, data: Folder? = null) = DbFolderResponse(data, false, listOf(error))

        val errorFolderEmptyId = error(errorEmptyId)

        val errorFolderNotFound = error(errorNotFound)

        val errorFolderAlreadyExists = error(errorAlreadyExists)
    }
}
