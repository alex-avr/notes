package org.avr.notes.common

import kotlinx.datetime.Instant
import org.avr.notes.common.models.*
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.stubs.NotesStubs

data class FolderContext (
    var command: FolderCommand = FolderCommand.NONE,
    var state: NotesState = NotesState.NONE,
    var errors: MutableList<NotesError> = mutableListOf(),
    var settings: NotesCorSettings = NotesCorSettings.NONE,

    var workMode: NotesWorkMode = NotesWorkMode.PROD,
    var stubCase: NotesStubs = NotesStubs.NONE,

    var requestId: NotesRequestId = NotesRequestId.NONE,
    var processingStartTime: Instant = Instant.NONE,

    var folderRequest: Folder = Folder(),

    var folderValidating: Folder = Folder(),

    var folderValidated: Folder = Folder(),

    var folderResponse: Folder = Folder(),
    var folderChildrenResponse: MutableList<IFolderChild> = mutableListOf(),
) : IContext