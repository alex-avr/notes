package org.avr.notes.biz.workers

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<FolderContext>.folderInitStatus(title: String) = worker() {
    this.title = title
    on { state == NotesState.NONE }
    handle { state = NotesState.RUNNING }
}

fun ICorChainDsl<NoteContext>.noteInitStatus(title: String) = worker() {
    this.title = title
    on { state == NotesState.NONE }
    handle { state = NotesState.RUNNING }
}