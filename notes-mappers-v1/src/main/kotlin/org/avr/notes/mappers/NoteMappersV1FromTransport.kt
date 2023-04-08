package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.FolderChildType
import org.avr.notes.common.models.Note
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

fun NoteContext.fromTransport(request: INoteRequest) = when (request) {
    is NoteCreateRequest -> fromTransport(request)
    is NoteGetRequest -> fromTransport(request)
    is NoteUpdateRequest -> fromTransport(request)
    is NoteSearchRequest -> fromTransport(request)
    is NoteDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request.javaClass)
}

private fun NoteContext.fromTransport(request: NoteCreateRequest) {
    command = NoteCommand.CREATE_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(null, request.parentFolderId, request.noteData, null)
}

private fun NoteContext.fromTransport(request: NoteGetRequest) {
    command = NoteCommand.READ_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(request.noteId, null, null, null)
}

private fun NoteContext.fromTransport(request: NoteUpdateRequest) {
    command = NoteCommand.UPDATE_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(request.noteInfo?.noteId, request.noteInfo?.parentFolderId, request.noteData, request.noteInfo?.version)
}

private fun NoteContext.fromTransport(request: NoteSearchRequest) {
    command = NoteCommand.SEARCH_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteSearchFilter = noteSearchFilterFromTransport(request.noteFilter)
}
private fun NoteContext.fromTransport(request: NoteDeleteRequest) {
    command = NoteCommand.READ_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(request.noteId, null, null, null)
}