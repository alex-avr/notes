package org.avr.notes.mappers

import kotlinx.datetime.Instant
import org.avr.notes.api.v1.models.*
import org.avr.notes.common.NONE
import org.avr.notes.common.models.NotesRequestId
import org.avr.notes.common.models.NotesStubs
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId

fun getRequestId(request: IFolderRequest): NotesRequestId {
    return request.requestId?.let { NotesRequestId(it) } ?: NotesRequestId.NONE
}

fun getRequestId(request: INoteRequest): NotesRequestId {
    return request.requestId?.let { NotesRequestId(it) } ?: NotesRequestId.NONE
}

fun getFolderId(id: String?): FolderId {
    return id?.let { FolderId(it) } ?: FolderId.NONE
}

fun getNoteId(id: String?): NoteId {
    return id?.let { NoteId(it) } ?: NoteId.NONE
}

fun instantFromString(str: String?): Instant = when (str) {
    null -> Instant.NONE
    else -> Instant.parse(str)
}

fun DebugSettings?.transportToWorkMode() = when (this?.mode) {
    RequestDebugMode.PROD -> NotesWorkMode.PROD
    RequestDebugMode.TEST -> NotesWorkMode.TEST
    RequestDebugMode.STUB -> NotesWorkMode.STUB
    null -> NotesWorkMode.PROD
}

fun DebugSettings?.transportToStubCase() = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> NotesStubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> NotesStubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> NotesStubs.BAD_ID
    RequestDebugStubs.BAD_NOTE_TITLE -> NotesStubs.BAD_NOTE_TITLE
    RequestDebugStubs.BAD_NOTE_BODY -> NotesStubs.BAD_NOTE_BODY
    RequestDebugStubs.BAD_FOLDER_NAME -> NotesStubs.BAD_FOLDER_NAME
    RequestDebugStubs.CANNOT_DELETE -> NotesStubs.CANNOT_DELETE
    RequestDebugStubs.BAD_SEARCH_STRING -> NotesStubs.BAD_SEARCH_STRING
    null -> NotesStubs.NONE
}

fun noteSearchFilterFromTransport(searchFilter: NoteSearchFilter?): org.avr.notes.common.models.note.NoteSearchFilter =
    searchFilter?.searchString?.let { org.avr.notes.common.models.note.NoteSearchFilter(it) }
        ?: org.avr.notes.common.models.note.NoteSearchFilter.NONE