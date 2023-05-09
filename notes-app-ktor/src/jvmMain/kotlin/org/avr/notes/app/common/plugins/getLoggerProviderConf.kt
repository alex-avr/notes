package org.avr.notes.app.common.plugins

import io.ktor.server.application.*
import org.avr.notes.logging.common.NotesLoggerProvider
import org.avr.notes.logging.jvm.notesLoggerLogback

actual fun Application.getLoggerProviderConf(): NotesLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        //"kmp" -> MpLoggerProvider { mpLoggerKermit(it) }
        "logback", null -> NotesLoggerProvider { notesLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Allowed values are: logback")
    }
