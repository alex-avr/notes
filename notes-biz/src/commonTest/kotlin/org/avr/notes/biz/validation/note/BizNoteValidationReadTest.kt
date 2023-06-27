package org.avr.notes.biz.validation.note

import org.avr.notes.biz.NoteProcessor
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.common.models.note.NoteCommand
import kotlin.test.Test

class BizNoteValidationReadTest {
    private val command = NoteCommand.READ_NOTE

    private val settings by lazy {
        NotesCorSettings()
    }

    private val processor by lazy { NoteProcessor(settings) }

    @Test
    fun correctId() = validationNoteIdCorrect(command, processor)

    @Test fun trimId() = validationNoteIdTrim(command, processor)

    @Test fun emptyId() = validationNoteIdEmpty(command, processor)

    @Test fun badFormatId() = validationNoteIdFormat(command, processor)
}