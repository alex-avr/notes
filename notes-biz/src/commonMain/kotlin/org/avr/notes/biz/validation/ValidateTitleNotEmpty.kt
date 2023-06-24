package org.avr.notes.biz.validation

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.errorValidation
import org.avr.notes.common.helpers.fail
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<FolderContext>.folderValidateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { folderValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<NoteContext>.noteValidateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { noteValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
