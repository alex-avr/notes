package org.avr.notes.biz.validation

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.errorValidation
import org.avr.notes.common.helpers.fail
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

private const val IdValidationRegexp = "^[0-9a-zA-Z-]+$"

fun ICorChainDsl<FolderContext>.folderValidateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex(IdValidationRegexp)
    on { folderValidating.id != FolderId.NONE && ! folderValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = folderValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}

fun ICorChainDsl<NoteContext>.noteValidateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex(IdValidationRegexp)
    on { noteValidating.id != NoteId.NONE && ! noteValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = noteValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
