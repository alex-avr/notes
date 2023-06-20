package org.avr.notes.biz

import org.avr.notes.biz.workers.initStatus
import org.avr.notes.common.NoteContext
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.cor.rootChain

class NoteProcessor(private val settings: NotesCorSettings = NotesCorSettings()) {
    suspend fun exec(ctx: NoteContext) = BusinessChain.exec(ctx.apply { settings =  this@NoteProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<NoteContext> {
            initStatus("Инициализация статуса")
        }.build()
    }
}