package org.avr.notes.common

import org.avr.notes.logging.common.NotesLoggerProvider

data class NotesCorSettings(
    val loggerProvider: NotesLoggerProvider = NotesLoggerProvider()
) {
    companion object {
        val NONE = NotesCorSettings()
    }
}
