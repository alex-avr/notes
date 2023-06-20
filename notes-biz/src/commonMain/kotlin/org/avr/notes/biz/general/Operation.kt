package org.avr.notes.biz.general

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.chain

fun ICorChainDsl<FolderContext>.operation(title: String, command: FolderCommand, block: ICorChainDsl<FolderContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == NotesState.RUNNING }
}

fun ICorChainDsl<NoteContext>.operation(title: String, command: NoteCommand, block: ICorChainDsl<NoteContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == NotesState.RUNNING }
}