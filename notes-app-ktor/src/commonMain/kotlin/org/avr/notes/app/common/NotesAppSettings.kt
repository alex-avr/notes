package org.avr.notes.app.common

import org.avr.notes.biz.FolderProcessor
import org.avr.notes.biz.NoteProcessor
import org.avr.notes.common.NotesCorSettings

data class NotesAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: NotesCorSettings,
    val folderProcessor: FolderProcessor = FolderProcessor(settings = corSettings),
    val noteProcessor: NoteProcessor = NoteProcessor(settings = corSettings),
)