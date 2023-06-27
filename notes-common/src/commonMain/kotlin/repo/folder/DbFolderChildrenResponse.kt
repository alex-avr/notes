package org.avr.notes.common.repo.folder

import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.repo.IDbResponse

data class DbFolderChildrenResponse(
    override val data: List<IFolderChild>? = emptyList(),
    override val isSuccess: Boolean = true,
    override val errors: List<NotesError> = emptyList()
) : IDbResponse<List<IFolderChild>> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbFolderChildrenResponse(emptyList(), true)

        fun success(result: List<IFolderChild>) = DbFolderChildrenResponse(result, true)
        fun error(errors: List<NotesError>) = DbFolderChildrenResponse(null, false, errors)
        fun error(error: NotesError) = DbFolderChildrenResponse(null, false, listOf(error))
    }
}

