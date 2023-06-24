package org.avr.notes.biz.stub.note

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
import org.avr.notes.common.stubs.NotesStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NoteStubDeleteTest {
    private val processor = NoteProcessor()

    private val id = NoteId("ecf4e57f-ad4d-4dbe-8854-55fbfe10554d")

    @Test
    fun delete() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.DELETE_NOTE,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.SUCCESS,
            noteRequest = Note(
                id = id
            )
        )

        processor.exec(ctx)

        assertEquals(id, ctx.noteResponse.id)
    }

    @Test
    fun badId() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.DELETE_NOTE,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.BAD_ID,
            noteRequest = Note(
                id = NoteId.NONE
            )
        )

        processor.exec(ctx)

        assertEquals(Note(), ctx.noteResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.DELETE_NOTE,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.DB_ERROR,
            noteRequest = Note(
                id = id,
                parentFolderId = FolderId.NONE,
                title = ""
            )
        )

        processor.exec(ctx)

        assertEquals(Note(), ctx.noteResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.DELETE_NOTE,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.NONE,
            noteRequest = Note(
                id = id
            )
        )

        processor.exec(ctx)

        assertEquals(Note(), ctx.noteResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}