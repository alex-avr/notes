package org.avr.notes.biz.workers.note

import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.fail
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.models.NotesState
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<NoteContext>.stubNoteNoCase(title: String) = worker {
    this.title = title
    on { state == NotesState.RUNNING }
    handle {
        fail(
            NotesError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}