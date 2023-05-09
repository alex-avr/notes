package org.avr.notes.app.common.plugins

import io.ktor.server.application.*
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.logging.common.NotesLoggerProvider

fun Application.initAppSettings(): NotesAppSettings {
    val corSettings = NotesCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return NotesAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        //folderProcessor = FolderProcessor(settings = corSettings),
        //noteProcessor = NoteProcessor(settings = corSettings),
    )
}

expect fun Application.getLoggerProviderConf(): NotesLoggerProvider
