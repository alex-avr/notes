package org.avr.notes.common

import kotlinx.datetime.Instant
import org.avr.notes.common.models.*
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.common.models.note.NoteSearchFilter
import org.avr.notes.common.stubs.NotesStubs

data class NoteContext(
    var command: NoteCommand = NoteCommand.NONE,
    var state: NotesState = NotesState.NONE,
    var errors: MutableList<NotesError> = mutableListOf(),

    var workMode: NotesWorkMode = NotesWorkMode.PROD,
    var stubCase: NotesStubs = NotesStubs.NONE,

    var requestId: NotesRequestId = NotesRequestId.NONE,
    var processingStartTime: Instant = Instant.NONE,

    var noteRequest: Note = Note(),
    var noteSearchFilter: NoteSearchFilter = NoteSearchFilter.NONE,
    var noteResponse: Note = Note(),
    var noteMultiResponse: MutableList<Note> = mutableListOf()
) : IContext