package org.avr.notes.mappers

import org.avr.notes.api.v1.models.Error
import org.avr.notes.api.v1.models.ResponseResult
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.models.NotesState

private fun NotesError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

fun List<NotesError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun responseResultFromState(state: NotesState) =
    if (state != NotesState.FAILING) ResponseResult.SUCCESS else ResponseResult.ERROR