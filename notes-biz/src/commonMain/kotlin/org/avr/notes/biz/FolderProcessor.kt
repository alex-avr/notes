package org.avr.notes.biz

import org.avr.notes.biz.general.operation
import org.avr.notes.biz.general.stubs
import org.avr.notes.biz.validation.finishFolderValidation
import org.avr.notes.biz.validation.validateTitleHasContent
import org.avr.notes.biz.validation.validateTitleNotEmpty
import org.avr.notes.biz.validation.validation
import org.avr.notes.biz.workers.folder.*
import org.avr.notes.biz.workers.initStatus
import org.avr.notes.common.FolderContext
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.cor.rootChain
import org.avr.notes.cor.worker

class FolderProcessor(private val settings: NotesCorSettings = NotesCorSettings()) {
    suspend fun exec(ctx: FolderContext) = BusinessChain.exec(ctx.apply { settings =  this@FolderProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<FolderContext> {
            initStatus("Инициализация статуса")

            operation("Создание папки", FolderCommand.CREATE_FOLDER) {
                stubs("Обработка стабов") {
                    stubFolderCreateSuccess("Имитация успешной обработки")
                    stubFolderValidationBadName("Имитация ошибки валидации названия папки")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    worker("Очистка названия папки") { folderValidating.title = folderValidating.title.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            operation("Обновление папки", FolderCommand.UPDATE_FOLDER) {
                stubs("Обработка стабов") {
                    stubFolderUpdateSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderValidationBadName("Имитация ошибки валидации названия папки")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    worker("Очистка названия папки") { folderValidating.title = folderValidating.title.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            operation("Получение информации о папке", FolderCommand.GET_FOLDER_INFO) {
                stubs("Обработка стабов") {
                    stubFolderGetInfoSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            operation("Получение дочерних элементов папки", FolderCommand.GET_FOLDER_CHILDREN) {
                stubs("Обработка стабов") {
                    stubFolderGetChildrenSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            operation("Удаление папки", FolderCommand.DELETE_FOLDER) {
                stubs("Обработка стабов") {
                    stubFolderDeleteSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
        }.build()
    }
}