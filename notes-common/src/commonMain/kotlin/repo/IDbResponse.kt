package org.avr.notes.common.repo

import org.avr.notes.common.models.NotesError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<NotesError>
}