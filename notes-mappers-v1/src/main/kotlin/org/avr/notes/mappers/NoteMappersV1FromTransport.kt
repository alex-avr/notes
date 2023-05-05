package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.api.v1.org.avr.notes.app.v1.RequestDebugParameters
import org.avr.notes.app.v1.NoteRequestParameters
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.FolderChildType
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.NotesRequestId
import org.avr.notes.common.models.note.NoteCommand

private fun noteRequestDataToInternal(idString: String?, parentFolderIdString: String?, noteData: NoteData?, version: Int?): Note = Note(
    id = getNoteId(idString),
    parentFolderId = getFolderId(parentFolderIdString),
    title = noteData?.title ?: "",
    body = noteData?.body ?: "",
    createTime = instantFromString(noteData?.createTime),
    updateTime = instantFromString(noteData?.updateTime),
    version = version ?: 1,
    folderChildType = FolderChildType.NOTE
)

fun NoteContext.fromRequestData(noteCommand: NoteCommand, debugParameters: RequestDebugParameters, requestParameters: NoteRequestParameters, requestBody: INoteRequest?) {
    requestId = NotesRequestId(debugParameters.requestId)
    workMode = debugParameters.workMode.transportToWorkMode()
    stubCase = debugParameters.stubType.transportToStubCase()

    command = noteCommand

    when (noteCommand) {
        NoteCommand.CREATE_NOTE -> fromNoteCreateRequestData(requestBody as NoteCreateRequest)
        NoteCommand.READ_NOTE -> fromNoteGetRequestData(requestParameters)
        NoteCommand.UPDATE_NOTE-> fromNoteUpdateRequestData(requestParameters, requestBody as NoteUpdateRequest)
        NoteCommand.SEARCH_NOTES -> fromNoteSearchRequestData(requestParameters)
        NoteCommand.DELETE_NOTE -> fromNoteDeleteRequestData(requestParameters)
        else -> throw UnknownNoteCommandException(noteCommand)
    }
}

private fun NoteContext.fromNoteCreateRequestData(requestBody: NoteCreateRequest) {
    noteRequest = noteRequestDataToInternal(null, requestBody.parentFolderId, requestBody.noteData, null)
}

private fun NoteContext.fromNoteGetRequestData(requestParameters: NoteRequestParameters) {
    val noteId = requestParameters.noteId ?: throw MissingRequestParameterException("noteId")

    noteRequest = noteRequestDataToInternal(noteId, null, null, null)
}

private fun NoteContext.fromNoteUpdateRequestData(requestParameters: NoteRequestParameters, requestBody: NoteUpdateRequest) {
    val noteId = requestParameters.noteId ?: throw MissingRequestParameterException("noteId")

    noteRequest = noteRequestDataToInternal(noteId, requestBody.noteInfo?.parentFolderId, requestBody.noteData, requestBody.noteInfo?.version)
}

private fun NoteContext.fromNoteSearchRequestData(requestParameters: NoteRequestParameters) {
    val noteFilter = requestParameters.noteSearchFilter ?: throw MissingRequestParameterException("searchFilter")

    noteSearchFilter = noteSearchFilterFromTransport(NoteSearchFilter(noteFilter))
}
private fun NoteContext.fromNoteDeleteRequestData(requestParameters: NoteRequestParameters) {
    val noteId = requestParameters.noteId ?: throw MissingRequestParameterException("noteId")

    noteRequest = noteRequestDataToInternal(noteId, null, null, null)
}