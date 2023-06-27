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
import org.avr.notes.stub.NoteStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = NoteStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteTitleCorrect(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = stub.id,
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
    assertEquals("abc", ctx.noteValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteTitleTrim(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = stub.id,
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = " \n\t abc \t\n "
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
    assertEquals("abc", ctx.noteValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteTitleEmpty(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = stub.id,
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = ""
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNoteTitleSymbols(command: NoteCommand, processor: NoteProcessor) = runTest {
    val ctx = NoteContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        noteRequest = Note(
            id = NoteId("123"),
            parentFolderId = FolderId("734-53425-gdsfgf-tre"),
            title = "!@#$%^&*(),.{}"
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
