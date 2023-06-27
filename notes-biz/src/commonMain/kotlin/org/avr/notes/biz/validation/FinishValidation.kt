package org.avr.notes.biz.validation

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<FolderContext>.finishFolderValidation(title: String) = worker {
    this.title = title
    on { state == NotesState.RUNNING }
    handle {
        folderValidated = folderValidating
    }
}

fun ICorChainDsl<NoteContext>.finishNoteValidation(title: String) = worker {
    this.title = title
    on { state == NotesState.RUNNING }
    handle {
        noteValidated = noteValidating
    }
}

fun ICorChainDsl<NoteContext>.finishNoteFilterValidation(title: String) = worker {
    this.title = title
    on { state == NotesState.RUNNING }
    handle {
        noteSearchFilterValidated = noteSearchFilterValidating
    }
}