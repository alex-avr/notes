package org.avr.notes.common

import kotlinx.datetime.Instant
import org.avr.notes.common.models.*
import org.avr.notes.common.models.folder.Folder
import org.avr.notes.common.models.note.Note
import org.avr.notes.common.models.note.NoteSearchFilter

data class NotesContext(
    var command: NotesCommand = NotesCommand.NONE,
    var state: NotesState = NotesState.NONE,
    var errors: MutableList<NotesError> = mutableListOf(),

    var workMode: NotesWorkMode = NotesWorkMode.PROD,
    var stubCase: NotesStubs = NotesStubs.NONE,

    var requestId: NotesRequestId = NotesRequestId.NONE,
    var processingStartTime: Instant = Instant.NONE,

    var noteRequest: Note = Note(),
    var noteSearchFilter: NoteSearchFilter,
    var noteResponse: Note = Note(),
    var noteMultiResponse: MutableList<Note> = mutableListOf(),

    var folderRequest: Folder = Folder(),
    var folderResponse: Folder = Folder(),
    var folderChildrenResponse: MutableList<IFolderChild> = mutableListOf(),
)