package org.avr.notes.biz

import org.avr.notes.biz.general.operation
import org.avr.notes.biz.general.stubs
import org.avr.notes.biz.validation.*
import org.avr.notes.biz.workers.initStatus
import org.avr.notes.biz.workers.note.*
import org.avr.notes.common.NoteContext
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.cor.rootChain
import org.avr.notes.cor.worker

class NoteProcessor(private val settings: NotesCorSettings = NotesCorSettings()) {
    suspend fun exec(ctx: NoteContext) = BusinessChain.exec(ctx.apply { settings =  this@NoteProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<NoteContext> {
            initStatus("Инициализация статуса")

            operation("Создание заметки", NoteCommand.CREATE_NOTE) {
                stubs("Обработка стабов") {
                    stubNoteCreateSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации итдентификатора заметки")
                    stubNoteValidationBadTitle("Имитация ошибки валидации заголовка заметки")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в noteValidating") { noteValidating = noteRequest.deepCopy() }
                    worker("Очистка названия заметки") { noteValidating.title = noteValidating.title.trim() }
                    validateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    validateIdProperFormat("Проверка формата идентификатора")
                    validateParentFolderIdNotEmpty("Проверка, что идентификатор родительской папки не пуст")
                    validateTitleNotEmpty("Проверка, что название заметки не пусто")
                    validateTitleHasContent("Проверка символов")

                    finishNoteValidation("Завершение проверок")
                }
            }

            operation("Чтение заметки", NoteCommand.READ_NOTE) {
                stubs("Обработка стабов") {
                    stubNoteGetSuccess("Имитация успешной обработки")
                    stubNoteValidationBadId("Имитация ошибки валидации итдентификатора заметки")
                    stubNoteDbError("Имитация ошибки работы с БД")
                    stubNoteNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    validateIdNotEmpty("Проверка, что идентификатор заметки не пуст")
                    validateParentFolderIdNotEmpty("Проверка, что идентификатор родительской папки не пуст")
                    validateTitleNotEmpty("Проверка, что название заметки не пусто")
                    validateTitleHasContent("Проверка символов")

                    finishNoteValidation("Завершение проверок")
                }
            }
        }.build()
    }
}