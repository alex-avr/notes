package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderErrorCode
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.common.models.note.NoteId

private fun NoteContext.hasUpdateConflict() =
    this.errors.find { it.code == FolderErrorCode.CONFLICT.toString() } != null

fun NoteContext.toTransport(): INoteResponse = when (command) {
    NoteCommand.CREATE_NOTE -> toTransportCreate()
    NoteCommand.READ_NOTE -> toTransportRead()
    NoteCommand.UPDATE_NOTE -> if (hasUpdateConflict()) toTransportUpdateConflict() else toTransportUpdate()
    NoteCommand.DELETE_NOTE -> toTransportDelete()
    NoteCommand.SEARCH_NOTE -> toTransportSearch()
    NoteCommand.NONE -> throw UnknownNoteCommandException(command)
}

private fun Note.toTransport(): NoteResponseObject = NoteResponseObject(
    title = this.title,
    body = this.body,
    createTime = this.createTime.toString(),
    updateTime = this.updateTime.toString(),
    noteId = this.id.asString(),
    parentFolderId = this.parentFolderId.asString(),
    version = this.version
)

private fun NoteContext.toTransportCreate() = NoteCreateResponse(
    responseType = "create",
    requestId = this.requestId.asString().takeIf { it.isBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    noteId = noteResponse.id.asString(),
    parentFolderId = noteResponse.parentFolderId.asString(),
    version = noteResponse.version
)
private fun NoteContext.toTransportRead(): INoteResponse = NoteGetResponse(
    responseType = "read",
    requestId = this.requestId.asString().takeIf { it.isBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    note = this.noteResponse.toTransport()
)

private fun NoteContext.toTransportUpdate(): INoteResponse = NoteUpdateResponse(
    responseType = "update",
    requestId = this.requestId.asString().takeIf { it.isBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    noteId = this.noteResponse.id.asString(),
    parentFolderId = this.noteResponse.parentFolderId.asString(),
    version = this.noteResponse.version
)

private fun NoteContext.toTransportUpdateConflict(): INoteResponse = NoteUpdateConflictResponse(
    responseType = "updateConflict",
    requestId = this.requestId.asString().takeIf { it.isBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    note = this.noteResponse.toTransport()
)

private fun NoteContext.toTransportDelete(): INoteResponse = NoteDeleteResponse(
    responseType = "delete",
    requestId = this.requestId.asString().takeIf { it.isBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors()
)

private fun NoteContext.toTransportSearch(): INoteResponse = NoteSearchResponse(
    responseType = "search",
    requestId = this.requestId.asString().takeIf { it.isBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    notes = noteSearchResultToTransport(this.noteMultiResponse)
)

private fun noteSearchResultToTransport(noteMultiResponse: MutableList<Note>): List<NoteResponseObject>? {
    return noteMultiResponse.map {
        NoteResponseObject(
            title = it.title,
            body = it.body,
            createTime = it.createTime.toString(),
            updateTime = it.updateTime.toString(),
            noteId = it.id.takeIf { id -> id != NoteId.NONE }?.asString() ?: "",
            parentFolderId = it.parentFolderId.takeIf { id -> id != FolderId.NONE }?.asString() ?: "",
            version = it.version
        )
    }
}
