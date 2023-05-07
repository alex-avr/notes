package org.avr.notes.mappers

import kotlinx.datetime.Instant
import org.avr.notes.api.v1.models.*
import org.avr.notes.app.v1.NoteRequestParameters
import org.avr.notes.app.v1.RequestDebugParameters
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.NotesRequestId
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.stubs.NotesStubs
import org.junit.Test
import kotlin.test.assertEquals

class NoteMappersTest {
    @Test
    fun testCreateNoteFromTransport() {
        val requestBody = NoteCreateRequest(
            requestType = "create",
            requestId = "test_id",
           parentFolderId = null,
            noteData = NoteData("new_note_title", "Test body", "2023-04-03T10:00:00Z", "2023-04-04T12:00:00Z")
        )

        val context = NoteContext()
        val noteCommand = NoteCommand.CREATE_NOTE
        val debugParameters = RequestDebugParameters(requestId = "test_id",
            workMode = RequestWorkMode.STUB,
            stubType = RequestStubType.SUCCESS)
        val requestParameters = NoteRequestParameters(noteId = null, parentFolderId = null)
        context.fromRequestData(noteCommand, debugParameters, requestParameters, requestBody)

        assertEquals(NoteCommand.CREATE_NOTE, context.command)
        assertEquals(NotesState.NONE, context.state)
        assertEquals(NotesWorkMode.STUB, context.workMode)
        assertEquals(NotesStubs.SUCCESS, context.stubCase)
        assertEquals("test_id", context.requestId.asString())
        assertEquals(FolderId.NONE, context.noteRequest.parentFolderId)
        assertEquals("new_note_title", context.noteRequest.title)
        assertEquals("Test body", context.noteRequest.body)
        assertEquals("2023-04-03T10:00:00Z", context.noteRequest.createTime.toString())
        assertEquals("2023-04-04T12:00:00Z", context.noteRequest.updateTime.toString())
    }

    @Test
    fun testGetNoteResponseToTransport() {
        val context = NoteContext(
            command = NoteCommand.READ_NOTE,
            state = NotesState.RUNNING,
            workMode = NotesWorkMode.PROD,
            stubCase = NotesStubs.NONE,
            requestId = NotesRequestId("test_id_3"),
            noteResponse = Note(
                id = NoteId("note_id"),
                parentFolderId = FolderId("parent_folder_id_2"),
                title = "The First note",
                body = "Test body",
                createTime = Instant.parse("2023-04-02T10:25:00Z"),
                updateTime = Instant.parse("2023-04-02T10:30:00Z"),
                version = 3
            )
        )

        val response = context.toTransport() as NoteGetResponse

        assertEquals("read", response.responseType)
        assertEquals("test_id_3", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("note_id", response.note?.noteId)
        assertEquals("parent_folder_id_2", response.note?.parentFolderId)
        assertEquals("The First note", response.note?.title)
        assertEquals("Test body", response.note?.body)
        assertEquals(3, response.note?.version)
        assertEquals("2023-04-02T10:25:00Z", response.note?.createTime.toString())
        assertEquals("2023-04-02T10:30:00Z", response.note?.updateTime.toString())
    }
}