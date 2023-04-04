package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.common.NotesContext
import org.avr.notes.common.models.NotesCommand
import org.avr.notes.common.models.note.Note

private fun noteRequestDataToInternal(idString: String?, parentFolderIdString: String?, noteData: NoteData?, version: Int?): Note = Note(
    id = getNoteId(idString),
    parentFolderId = getFolderId(parentFolderIdString),
    title = noteData?.title ?: "",
    body = noteData?.body ?: "",
    createTime = instantFromString(noteData?.createTime),
    updateTime = instantFromString(noteData?.updateTime),
    version = version ?: 1
)

fun NotesContext.fromTransport(request: INoteRequest) = when (request) {
    is NoteCreateRequest -> fromTransport(request)
    is NoteGetRequest -> fromTransport(request)
    is NoteUpdateRequest -> fromTransport(request)
    is NoteSearchRequest -> fromTransport(request)
    is NoteDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request.javaClass)
}

private fun NotesContext.fromTransport(request: NoteCreateRequest) {
    command = NotesCommand.CREATE_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(null, request.parentFolderId, request.noteData, null)
}

private fun NotesContext.fromTransport(request: NoteGetRequest) {
    command = NotesCommand.READ_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(request.noteId, null, null, null)
}

private fun NotesContext.fromTransport(request: NoteUpdateRequest) {
    command = NotesCommand.UPDATE_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(request.noteInfo?.noteId, request.noteInfo?.parentFolderId, request.noteData, request.noteInfo?.version)
}

private fun NotesContext.fromTransport(request: NoteSearchRequest) {
    command = NotesCommand.SEARCH_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteSearchFilter = noteSearchFilterFromTransport(request.noteFilter)
}
private fun NotesContext.fromTransport(request: NoteDeleteRequest) {
    command = NotesCommand.READ_NOTE
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    noteRequest = noteRequestDataToInternal(request.noteId, null, null, null)
}