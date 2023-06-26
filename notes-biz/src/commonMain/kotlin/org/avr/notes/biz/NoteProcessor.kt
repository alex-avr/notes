package org.avr.notes.biz

import org.avr.notes.biz.general.noteOperation
import org.avr.notes.biz.general.noteStubs
import org.avr.notes.biz.validation.*
import org.avr.notes.biz.workers.note.*
import org.avr.notes.biz.workers.noteInitStatus
import org.avr.notes.common.NoteContext
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.cor.rootChain
import org.avr.notes.cor.worker

class NoteProcessor(private val settings: NotesCorSettings = NotesCorSettings()) {
    suspend fun exec(ctx: NoteContext) = BusinessChain.exec(ctx.apply { settings =  this@NoteProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<NoteContext> {
            noteInitStatus("Инициализация статуса")

            noteOperation("Создание заметки", NoteCommand.CREATE_NOTE) {
                noteStubs("Обработка стабов") {
                    stubNoteCreateSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации итдентификатора заметки")
                    stubNoteValidationBadTitle("Имитация ошибки валидации заголовка заметки")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                noteValidation {
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Очистка id") { noteValidating.id = NoteId(noteValidating.id.asString().trim()) }
                    worker("Очистка названия заметки") { noteValidating.title = noteValidating.title.trim() }
                    noteValidateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    noteValidateIdProperFormat("Проверка формата идентификатора")
                    validateParentFolderIdNotEmpty("Проверка, что идентификатор родительской папки не пуст")
                    noteValidateTitleNotEmpty("Проверка, что название заметки не пусто")
                    noteValidateTitleHasContent("Проверка символов")

                    finishNoteValidation("Завершение проверок")
                }
            }

            noteOperation("Чтение заметки", NoteCommand.READ_NOTE) {
                noteStubs("Обработка стабов") {
                    stubNoteGetSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации итдентификатора заметки")
                    stubNoteValidationBadTitle("Имитация ошибки валидации заголовка заметки")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                noteValidation {
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Очистка id") { noteValidating.id = NoteId(noteValidating.id.asString().trim()) }
                    noteValidateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    noteValidateIdProperFormat("Проверка формата идентификатора")
                    validateParentFolderIdNotEmpty("Проверка, что идентификатор родительской папки не пуст")
                    noteValidateTitleNotEmpty("Проверка, что название заметки не пусто")
                    noteValidateTitleHasContent("Проверка символов")

                    finishNoteValidation("Завершение проверок")
                }
            }

            noteOperation("Обновление заметки", NoteCommand.UPDATE_NOTE) {
                noteStubs("Обработка стабов") {
                    stubNoteUpdateSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации итдентификатора заметки")
                    stubNoteValidationBadTitle("Имитация ошибки валидации заголовка заметки")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                noteValidation {
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Очистка id") { noteValidating.id = NoteId(noteValidating.id.asString().trim()) }
                    worker("Очистка названия заметки") { noteValidating.title = noteValidating.title.trim() }
                    noteValidateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    noteValidateIdProperFormat("Проверка формата идентификатора")
                    validateParentFolderIdNotEmpty("Проверка, что идентификатор родительской папки не пуст")
                    noteValidateTitleNotEmpty("Проверка, что название заметки не пусто")
                    noteValidateTitleHasContent("Проверка символов")

                    finishNoteValidation("Завершение проверок")
                }
            }

            noteOperation("Удаление заметки", NoteCommand.DELETE_NOTE) {
                noteStubs("Обработка стабов") {
                    stubNoteDeleteSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации идентификатора заметки")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                noteValidation {
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Очистка id") { noteValidating.id = NoteId(noteValidating.id.asString().trim()) }
                    noteValidateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    noteValidateIdProperFormat("Проверка формата идентификатора")

                    finishNoteValidation("Завершение проверок")
                }
            }

            noteOperation("Поиск заметок", NoteCommand.SEARCH_NOTES) {
                noteStubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации идентификатора заметки")
                    stubNoteValidationBadSearchFilter("Имитация ошибки - не указана строка фильтра для поиска заметок")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                noteValidation {
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Очистка id") { noteValidating.id = NoteId(noteValidating.id.asString().trim()) }
                    noteValidateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    noteValidateIdProperFormat("Проверка формата идентификатора")

                    finishNoteFilterValidation("Завершение проверок")
                }
            }
        }.build()
    }
}