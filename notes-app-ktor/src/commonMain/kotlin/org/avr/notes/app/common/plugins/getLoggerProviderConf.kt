package org.avr.notes.app.common.plugins

import io.ktor.server.application.*
import org.avr.notes.logging.common.NotesLoggerProvider

expect fun Application.getLoggerProviderConf(): NotesLoggerProvider