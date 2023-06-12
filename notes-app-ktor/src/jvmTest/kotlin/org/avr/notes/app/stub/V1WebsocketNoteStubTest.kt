package org.avr.notes.app.stub

import org.avr.notes.api.v1.models.*
import org.avr.notes.stub.NoteStub
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Тесты обработки запросов через WebSockets для заметок
 */
class V1WebsocketNoteStubTest {
    private val requestId = "cfa57cd5-9acd-4bf5-94e0-48161b9da987"
    private val noteId = "ecf4e57f-ad4d-4dbe-8854-55fbfe10554d"
    private val parentFolderId = "8640c047-a6a1-4c5b-b3a8-204c809bdb1d"

    @Test
    fun createStub() {
        val note = NoteStub.create()
        val noteRequest = NoteCreateRequest(
            requestType = "create",
            requestId = requestId,
            parentFolderId = null,
            noteData = NoteData(
                title = note.title,
                body = note.body,
                createTime = note.createTime.toString(),
                updateTime = note.updateTime.toString(),
            )
        )

        val request = WsRequest(
            objectKind = ObjectKind.NOTE,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS
            ),
            noteRequestData = noteRequest
        )

        testMethod<NoteCreateResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(noteRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(noteId, it.noteId)
        }
    }

    @Test
    fun updateStub() {
        val note = NoteStub.update()
        val noteRequest = NoteUpdateRequest(
            requestType = "update",
            requestId = requestId,
            noteData = NoteData(
                title = note.title,
                body = note.body,
                createTime = note.createTime.toString(),
                updateTime = note.updateTime.toString(),
            ),
            noteInfo = NoteBriefInfo(
                noteId = noteId,
                parentFolderId = note.parentFolderId.asString(),
                version = 2
            )
        )

        val request = WsRequest(
            objectKind = ObjectKind.NOTE,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = noteId
            ),
            noteRequestData = noteRequest
        )

        testMethod<NoteUpdateResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(noteRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(noteId, it.noteId)
        }
    }

    @Test
    fun getStub() {
        val noteRequest = NoteGetRequest(
            requestType = "read",
            requestId = requestId,
            noteId = noteId
        )

        val request = WsRequest(
            objectKind = ObjectKind.NOTE,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = noteId
            ),
            noteRequestData = noteRequest
        )

        testMethod<NoteGetResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(noteRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(noteId, it.note?.noteId)
            assertEquals("Updated note", it.note?.title)
            assertEquals("This is a test updated note", it.note?.body)
            assertEquals(parentFolderId, it.note?.parentFolderId)
            assertEquals("2023-02-01T10:00:00Z", it.note?.createTime)
            assertEquals("2023-03-02T11:00:00Z", it.note?.updateTime)
            assertEquals(2, it.note?.version)
        }
    }

    @Test
    fun deleteStub() {
        val noteRequest = NoteDeleteRequest(
            requestType = "delete",
            requestId = requestId,
            noteId = noteId
        )

        val request = WsRequest(
            objectKind = ObjectKind.NOTE,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = noteId
            ),
            noteRequestData = noteRequest
        )

        testMethod<NoteDeleteResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(noteRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val searchString = "note"
        val noteRequest = NoteSearchRequest(
            requestType = "search",
            requestId = requestId,
            noteFilter = NoteSearchFilter(searchString)
        )

        val request = WsRequest(
            objectKind = ObjectKind.NOTE,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                searchString = searchString
            ),
            noteRequestData = noteRequest
        )

        testMethod<NoteSearchResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(noteRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(1, it.notes?.size)

            val note = it.notes?.first()
            assertEquals(noteId, note?.noteId)
            assertEquals("Updated note", note?.title)
            assertEquals("This is a test updated note", note?.body)
            assertEquals(parentFolderId, note?.parentFolderId)
            assertEquals("2023-02-01T10:00:00Z", note?.createTime)
            assertEquals("2023-03-02T11:00:00Z", note?.updateTime)
            assertEquals(2, note?.version)
        }
    }
}