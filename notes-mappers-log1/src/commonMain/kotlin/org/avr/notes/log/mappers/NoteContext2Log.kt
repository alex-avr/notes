package org.avr.notes.log.mappers

import kotlinx.datetime.Clock
import org.avr.notes.api.logs.models.CommonLogModel
import org.avr.notes.api.logs.models.FolderChildInfo
import org.avr.notes.api.logs.models.NoteLog
import org.avr.notes.api.logs.models.NoteLogModel
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.note.NoteCommand

fun NoteContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "notes",
    errors = errors.map { it.toLog() },
    objectKind = CommonLogModel.ObjectKind.NOTE,
    noteData = toLog()
)

fun Note.toChildInfoLog() = FolderChildInfo(
    id = id.asString(),
    parentFolderId = parentFolderId.asString(),
    title = title,
    version = version.toString(),
    createTime = createTime.toString(),
    updateTime = updateTime.toString()
)

private fun NoteContext.toLog(): NoteLogModel = NoteLogModel(
    requestId = requestId.asString(),
    operation = commandToLogOperation(command),
    requestNote = this.noteRequest.toLog(),
    responseNote = this.noteResponse.toLog(),
    responseNoteList = makeFoundNoteLog(this.noteMultiResponse)
)

fun makeFoundNoteLog(noteList: MutableList<Note>): List<NoteLog> =
    noteList.map { note -> note.toLog() }

private fun Note.toLog() = NoteLog(
    id = id.asString(),
    parentFolderId = parentFolderId?.asString(),
    title = title,
    version = version.toString(),
    createTime = createTime.toString(),
    updateTime = updateTime.toString(),
    body = body
)

private fun commandToLogOperation(command: NoteCommand): NoteLogModel.Operation? =
    when (command) {
        NoteCommand.CREATE_NOTE -> NoteLogModel.Operation.CREATE
        NoteCommand.READ_NOTE -> NoteLogModel.Operation.READ
        NoteCommand.UPDATE_NOTE -> NoteLogModel.Operation.UPDATE
        NoteCommand.DELETE_NOTE -> NoteLogModel.Operation.DELETE
        NoteCommand.SEARCH_NOTES -> NoteLogModel.Operation.SEARCH
        NoteCommand.NONE -> null
    }