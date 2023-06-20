package org.avr.notes.biz.general

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.chain

fun ICorChainDsl<FolderContext>.stubs(title: String, block: ICorChainDsl<FolderContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == NotesWorkMode.STUB && state == NotesState.RUNNING }
}

fun ICorChainDsl<NoteContext>.stubs(title: String, block: ICorChainDsl<NoteContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == NotesWorkMode.STUB && state == NotesState.RUNNING }
}