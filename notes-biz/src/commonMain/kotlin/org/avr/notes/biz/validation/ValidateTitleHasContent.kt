package org.avr.notes.biz.validation

import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.errorValidation
import org.avr.notes.common.helpers.fail
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

private const val TitleValidationRegexp = "\\p{L}"

fun ICorChainDsl<FolderContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex(TitleValidationRegexp)
    on { folderValidating.title.isNotEmpty() && ! folderValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "the field must contain letters"
            )
        )
    }
}

fun ICorChainDsl<NoteContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex(TitleValidationRegexp)
    on { noteValidating.title.isNotEmpty() && ! noteValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "the field must contain letters"
            )
        )
    }
}