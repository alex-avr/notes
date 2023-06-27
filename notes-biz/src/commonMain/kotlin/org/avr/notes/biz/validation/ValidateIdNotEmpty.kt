package org.avr.notes.biz.validation

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.errorValidation
import org.avr.notes.common.helpers.fail
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

fun ICorChainDsl<FolderContext>.folderValidateIdNotEmpty(title: String) = worker {
    this.title = title
    on { folderValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<NoteContext>.noteValidateIdNotEmpty(title: String) = worker {
    this.title = title
    on { noteValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<NoteContext>.validateParentFolderIdNotEmpty(title: String) = worker {
    this.title = title
    on { noteValidating.parentFolderId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "parentFolderId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}