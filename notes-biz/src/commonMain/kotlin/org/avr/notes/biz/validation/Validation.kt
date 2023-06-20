package org.avr.notes.biz.validation

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.chain

fun ICorChainDsl<FolderContext>.validation(block: ICorChainDsl<FolderContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == NotesState.RUNNING }
}

fun ICorChainDsl<NoteContext>.validation(block: ICorChainDsl<NoteContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == NotesState.RUNNING }
}