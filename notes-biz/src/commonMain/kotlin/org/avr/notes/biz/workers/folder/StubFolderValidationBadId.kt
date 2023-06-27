package org.avr.notes.biz.workers.folder

import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.NotesError
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.stubs.NotesStubs
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<FolderContext>.stubFolderValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == NotesStubs.BAD_ID && state == NotesState.RUNNING }
    handle {
        state = NotesState.FAILING
        this.errors.add(
            NotesError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
