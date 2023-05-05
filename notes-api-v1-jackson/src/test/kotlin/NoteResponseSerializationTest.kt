package org.avr.notes.api.v1

import org.avr.notes.api.v1.models.INoteResponse
import org.avr.notes.api.v1.models.NoteCreateResponse
import org.avr.notes.api.v1.models.ResponseResult
import org.avr.notes.app.v1.apiV1Mapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

/**
 * Тесты для проверки сериализации и десериализации транспортных моделей ответов для заметок
 */
class NoteResponseSerializationTest {
    private val createNoteResponse = NoteCreateResponse(
        responseType = "create",
        requestId = "56435624",
        result = ResponseResult.SUCCESS,
        errors = null,
        noteId = "new-note-id",
        parentFolderId = "root-folder-id",
        version = 1
    )

    @Test
    fun testSerializeFolderCreateResponse() {
        val json = apiV1Mapper.writeValueAsString(createNoteResponse)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"requestId\":\\s*\"56435624\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"parentFolderId\":\\s*\"root-folder-id\""))
        assertContains(json, Regex("\"noteId\":\\s*\"new-note-id\""))
        assertContains(json, Regex("\"version\":\\s*1"))
    }

    @Test
    fun testDeserializeNoteCreateResponse() {
        val json = apiV1Mapper.writeValueAsString(createNoteResponse)
        val obj = apiV1Mapper.readValue(json, INoteResponse::class.java) as NoteCreateResponse

        assertEquals(createNoteResponse, obj)
    }
}