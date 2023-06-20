package org.avr.notes.biz

import org.avr.notes.biz.general.operation
import org.avr.notes.biz.general.stubs
import org.avr.notes.biz.workers.folder.stubFolderCreateSuccess
import org.avr.notes.biz.workers.initStatus
import org.avr.notes.common.FolderContext
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.cor.rootChain

class FolderProcessor(private val settings: NotesCorSettings = NotesCorSettings()) {
    suspend fun exec(ctx: FolderContext) = BusinessChain.exec(ctx.apply { settings =  this@FolderProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<FolderContext> {
            initStatus("Инициализация статуса")

            operation("Создание папки", FolderCommand.CREATE_FOLDER) {
                stubs("Обработка стабов") {
                    stubFolderCreateSuccess("Имитация успешной обработки")
                    //stubValidationBadTitle("Имитация ошибки валидации заголовка")
                }
            }
        }.build()
    }
}