package org.avr.notes.biz.validation.note

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.avr.notes.biz.NoteProcessor
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.common.models.note.NoteId
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteIdCorrect(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = NoteId("123-234-abc-ABC"),
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteIdTrim(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = NoteId(" \n\t 123-234-abc-ABC \n\t "),
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteIdEmpty(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = NoteId(""),
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteIdFormat(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = NoteId("!@#\$%^&*(),.{}"),
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
