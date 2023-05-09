package org.avr.notes.mappers

import kotlinx.datetime.Instant
import org.avr.notes.api.v1.models.INoteRequest
import org.avr.notes.api.v1.models.NoteSearchFilter
import org.avr.notes.api.v1.models.RequestStubType
import org.avr.notes.api.v1.models.RequestWorkMode
import org.avr.notes.common.NONE
import org.avr.notes.common.models.NotesRequestId
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.stubs.NotesStubs

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

fun RequestWorkMode?.transportToWorkMode() = when (this) {
    RequestWorkMode.PROD -> NotesWorkMode.PROD
    RequestWorkMode.TEST -> NotesWorkMode.TEST
    RequestWorkMode.STUB -> NotesWorkMode.STUB
    null -> NotesWorkMode.PROD
}

fun RequestStubType?.transportToStubCase() = when (this) {
    RequestStubType.NONE -> NotesStubs.NONE
    RequestStubType.SUCCESS -> NotesStubs.SUCCESS
    RequestStubType.NOT_FOUND -> NotesStubs.NOT_FOUND
    RequestStubType.BAD_ID -> NotesStubs.BAD_ID
    RequestStubType.BAD_NOTE_TITLE -> NotesStubs.BAD_NOTE_TITLE
    RequestStubType.BAD_NOTE_BODY -> NotesStubs.BAD_NOTE_BODY
    RequestStubType.BAD_FOLDER_NAME -> NotesStubs.BAD_FOLDER_NAME
    RequestStubType.CANNOT_DELETE -> NotesStubs.CANNOT_DELETE
    RequestStubType.BAD_SEARCH_STRING -> NotesStubs.BAD_SEARCH_STRING
    null -> NotesStubs.NONE
}

fun noteSearchFilterFromTransport(searchFilter: NoteSearchFilter?): org.avr.notes.common.models.note.NoteSearchFilter =
    searchFilter?.searchString?.let { org.avr.notes.common.models.note.NoteSearchFilter(it) }
        ?: org.avr.notes.common.models.note.NoteSearchFilter.NONE