package org.avr.notes.common.helpers

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.models.NotesState

fun Throwable.asNotesError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = NotesError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: NotesError.Level = NotesError.Level.ERROR,
) = NotesError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    level: NotesError.Level = NotesError.Level.ERROR,
) = NotesError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)

fun FolderContext.addError(vararg error: NotesError) = errors.addAll(error)

fun FolderContext.fail(error: NotesError) {
    addError(error)
    state = NotesState.FAILING
}

fun NoteContext.addError(vararg error: NotesError) = errors.addAll(error)

fun NoteContext.fail(error: NotesError) {
    addError(error)
    state = NotesState.FAILING
}