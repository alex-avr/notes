package org.avr.notes.log.mappers

import org.avr.notes.api.logs.models.ErrorLogModel
import org.avr.notes.common.models.NotesError

fun NotesError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name
)