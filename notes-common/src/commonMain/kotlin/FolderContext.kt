package org.avr.notes.common

import kotlinx.datetime.Instant
import org.avr.notes.common.models.*
import org.avr.notes.common.models.folder.FolderCommand

data class FolderContext(
    var command: FolderCommand = FolderCommand.NONE,
    var state: NotesState = NotesState.NONE,
    var errors: MutableList<NotesError> = mutableListOf(),

    var workMode: NotesWorkMode = NotesWorkMode.PROD,
    var stubCase: NotesStubs = NotesStubs.NONE,

    var requestId: NotesRequestId = NotesRequestId.NONE,
    var processingStartTime: Instant = Instant.NONE,

    var folderRequest: Folder = Folder(parent = null),
    var folderResponse: Folder = Folder(parent = null),
    var folderChildrenResponse: MutableList<IFolderChild> = mutableListOf(),
)