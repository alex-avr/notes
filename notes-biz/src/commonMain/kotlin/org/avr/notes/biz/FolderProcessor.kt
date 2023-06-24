package org.avr.notes.biz

import org.avr.notes.biz.general.folderOperation
import org.avr.notes.biz.general.folderStubs
import org.avr.notes.biz.validation.*
import org.avr.notes.biz.workers.folder.*
import org.avr.notes.biz.workers.folderInitStatus
import org.avr.notes.common.FolderContext
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.cor.rootChain
import org.avr.notes.cor.worker

class FolderProcessor(private val settings: NotesCorSettings = NotesCorSettings()) {
    suspend fun exec(ctx: FolderContext) = BusinessChain.exec(ctx.apply { settings =  this@FolderProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<FolderContext> {
            folderInitStatus("Инициализация статуса")

            folderOperation("Создание папки", FolderCommand.CREATE_FOLDER) {
                folderStubs("Обработка стабов") {
                    stubFolderCreateSuccess("Имитация успешной обработки")
                    stubFolderValidationBadName("Имитация ошибки валидации названия папки")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                folderValidation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    worker("Очистка названия папки") { folderValidating.title = folderValidating.title.trim() }
                    folderValidateIdNotEmpty("Проверка, что идентификатор задан")
                    folderValidateIdProperFormat("Проверка сиволов в идентификаторе")
                    folderValidateTitleNotEmpty("Проверка, что заголовок не пуст")
                    folderValidateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            folderOperation("Обновление папки", FolderCommand.UPDATE_FOLDER) {
                folderStubs("Обработка стабов") {
                    stubFolderUpdateSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderValidationBadName("Имитация ошибки валидации названия папки")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                folderValidation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    worker("Очистка названия папки") { folderValidating.title = folderValidating.title.trim() }
                    folderValidateTitleNotEmpty("Проверка, что заголовок не пуст")
                    folderValidateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            folderOperation("Получение информации о папке", FolderCommand.GET_FOLDER_INFO) {
                folderStubs("Обработка стабов") {
                    stubFolderGetInfoSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                folderValidation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    folderValidateTitleNotEmpty("Проверка, что заголовок не пуст")
                    folderValidateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            folderOperation("Получение дочерних элементов папки", FolderCommand.GET_FOLDER_CHILDREN) {
                folderStubs("Обработка стабов") {
                    stubFolderGetChildrenSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                folderValidation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    folderValidateTitleNotEmpty("Проверка, что заголовок не пуст")
                    folderValidateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
            folderOperation("Удаление папки", FolderCommand.DELETE_FOLDER) {
                folderStubs("Обработка стабов") {
                    stubFolderDeleteSuccess("Имитация успешной обработки")
                    stubFolderValidationBadId("Имитация ошибки валидации id")
                    stubFolderDbError("Имитация ошибки работы с БД")
                    stubFolderNoCase("Ошибка: запрошенный стаб недопустим")
                }
                folderValidation {
                    worker("Копируем поля в folderValidating") { folderValidating = folderRequest.deepCopy() }
                    folderValidateTitleNotEmpty("Проверка, что заголовок не пуст")
                    folderValidateTitleHasContent("Проверка символов")
                    finishFolderValidation("Завершение проверок")
                }
            }
        }.build()
    }
}