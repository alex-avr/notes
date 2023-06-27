package org.avr.notes.biz.stub.note

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
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
class NoteStubUpdateTest {
    private val processor = NoteProcessor()

    private val id = NoteId("ecf4e57f-ad4d-4dbe-8854-55fbfe10554d")
    private val parentFolderId = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d")
    private val title = "Updated note"
    private val body = "This is a test updated note"
    private val createTime = Instant.parse("2023-02-01T10:00Z")
    private val version = 2

    @Test
    fun update() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.UPDATE_NOTE,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.SUCCESS,
            noteRequest = Note(
                id = id,
                parentFolderId = parentFolderId,
                title = title,
                body = body,
                createTime = createTime,
                version = version
            )
        )

        processor.exec(ctx)

        assertEquals(id, ctx.noteResponse.id)
        assertEquals(parentFolderId, ctx.noteResponse.parentFolderId)
        assertEquals(title, ctx.noteResponse.title)
        assertEquals(body, ctx.noteResponse.body)
        assertEquals(createTime, ctx.noteResponse.createTime)
        assertEquals(version, ctx.noteResponse.version)
    }

    @Test
    fun badId() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.UPDATE_NOTE,
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
    fun badTitle() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.UPDATE_NOTE,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.BAD_FOLDER_NAME,
            noteRequest = Note(
                id = id,
                parentFolderId = FolderId.NONE,
                title = ""
            )
        )

        processor.exec(ctx)

        assertEquals(Note(), ctx.noteResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = NoteContext(
            command = NoteCommand.UPDATE_NOTE,
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
            command = NoteCommand.UPDATE_NOTE,
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