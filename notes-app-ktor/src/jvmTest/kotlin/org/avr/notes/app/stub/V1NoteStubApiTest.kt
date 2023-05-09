package org.avr.notes.app.stub

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.avr.notes.api.v1.models.*
import org.avr.notes.app.moduleJvm
import org.avr.notes.api.v1.DebugHeaders
import org.avr.notes.stub.NoteStub
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Тесты для API заметок, использующие заглушки
 * todo: актуализировать после полной реализаии работы с заглушками
 */
class V1NoteStubApiTest {
    private val requestId = "cfa57cd5-9acd-4bf5-94e0-48161b9da987"
    private val noteId = "ecf4e57f-ad4d-4dbe-8854-55fbfe10554d"

    @Test
    fun testCreateNote() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val note = NoteStub.create()
        ktorClient().use { client ->
            val response = client.post("/v1/notes") {
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = NoteCreateRequest(
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
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<NoteCreateResponse>()
            println(responseObj)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
            assertEquals(2, responseObj.version)
            assertEquals(noteId, responseObj.noteId)
        }
    }

    @Test
    fun testUpdateNote() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val note = NoteStub.update()
        ktorClient().use { client ->
            val response = client.put("/v1/notes") {
                url {
                    appendPathSegments(noteId)
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = NoteUpdateRequest(
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
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<NoteUpdateResponse>()
            println(responseObj)
            assertEquals(200, response.status.value)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
            assertEquals(2, responseObj.version)
            assertEquals(noteId, responseObj.noteId)
        }
    }

    @Test
    fun testGetNote() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val note = NoteStub.get()
        ktorClient().use { client ->
            val response = client.get("/v1/notes") {
                url {
                    appendPathSegments(noteId)
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = NoteGetRequest(
                    requestType = "readNote",
                    requestId = requestId,
                    noteId = noteId
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<NoteGetResponse>()
            println(responseObj)
            assertEquals(200, response.status.value)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
            assertEquals(noteId, responseObj.note?.noteId)
            assertEquals(note.title, responseObj.note?.title)
            assertEquals(note.body, responseObj.note?.body)
            assertEquals(note.parentFolderId.asString(), responseObj.note?.parentFolderId)
            assertEquals(2, responseObj.note?.version)
        }
    }

    @Test
    fun testDeleteNote() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        ktorClient().use { client ->
            val response = client.delete("/v1/notes") {
                url {
                    appendPathSegments(noteId)
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = NoteDeleteRequest(
                    requestType = "deleteNode",
                    requestId = requestId,
                    noteId = noteId
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<NoteDeleteResponse>()
            println(responseObj)
            assertEquals(200, response.status.value)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
        }
    }

    @Test
    fun testSearchNotes() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val note = NoteStub.searchResults()
        ktorClient().use { client ->
            val response = client.get("/v1/notes/search") {
                url {
                    parameters.append("searchString", "note")
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = NoteSearchRequest(
                    requestType = "search",
                    requestId = requestId
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<NoteSearchResponse>()
            println(responseObj)
            assertEquals(200, response.status.value)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
        }
    }
}