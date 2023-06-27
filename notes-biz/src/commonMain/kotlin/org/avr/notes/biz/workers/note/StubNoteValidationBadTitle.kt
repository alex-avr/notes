package org.avr.notes.biz.workers.note

import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.stubs.NotesStubs
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<NoteContext>.stubNoteValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == NotesStubs.BAD_FOLDER_NAME && state == NotesState.RUNNING }
    handle {
        state = NotesState.FAILING
        this.errors.add(
            NotesError(
                group = "validation",
                code = "validation-note-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}