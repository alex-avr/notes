package org.avr.notes.api.v1

import org.avr.notes.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

/**
 * Тесты для проверки сериализации и десериализации транспортных моделей запросов для заметок
 */
class NoteRequestSerializationTest {
    private val createNoteRequest = NoteCreateRequest(
        requestType = "create",
        requestId = "74656",
        debug = DebugSettings(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.SUCCESS
        ),
        parentFolderId = "root-folder-id",
        noteData = NoteData(
            title = "Test title",
            body = "Some body",
            createTime = "2023-02-01T10:00",
            updateTime = "2023-02-01T10:23",
            1
        )
    )

    @Test
    fun testSerializeNoteCreateRequest() {
        val json = apiV1Mapper.writeValueAsString(createNoteRequest)

        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        assertContains(json, Regex("\"requestId\":\\s*\"74656\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
        assertContains(json, Regex("\"parentFolderId\":\\s*\"root-folder-id\""))
        assertContains(json, Regex("\"title\":\\s*\"Test title\""))
        assertContains(json, Regex("\"body\":\\s*\"Some body\""))
    }

    @Test
    fun testDeserializeNoteCreateRequest() {
        val json = apiV1Mapper.writeValueAsString(createNoteRequest)
        val obj = apiV1Mapper.readValue(json, INoteRequest::class.java) as NoteCreateRequest

        assertEquals(createNoteRequest, obj)
    }
}