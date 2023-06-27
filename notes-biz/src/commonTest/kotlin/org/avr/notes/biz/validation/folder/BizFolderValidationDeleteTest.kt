package org.avr.notes.biz.validation.folder

import org.avr.notes.biz.FolderProcessor
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.folder.FolderCommand
import kotlin.test.Test

class BizFolderValidationDeleteTest {
    private val command = FolderCommand.DELETE_FOLDER

    private val settings by lazy {
        NotesCorSettings()
    }

    private val processor by lazy { FolderProcessor(settings) }

    @Test fun correctId() = validationFolderIdCorrect(command, processor)
    @Test fun trimId() = validationFolderIdTrim(command, processor)
    @Test fun emptyId() = validationFolderIdEmpty(command, processor)
    @Test fun badFormatId() = validationFolderIdFormat(command, processor)
}