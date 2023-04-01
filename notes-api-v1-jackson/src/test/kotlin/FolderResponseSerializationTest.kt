package org.avr.notes.api.v1

import org.avr.notes.api.v1.models.FolderCreateResponse
import org.avr.notes.api.v1.models.IFolderResponse
import org.avr.notes.api.v1.models.ResponseResult
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

/**
 * Тесты для проверки сериализации и десериализации транспортных моделей ответов для папок
 */
class FolderResponseSerializationTest {
    private val createFolderResponse = FolderCreateResponse(
        responseType = "create",
        requestId = "2345",
        result = ResponseResult.SUCCESS,
        errors = null,
        folderId = "new-folder-id",
        parentFolderId = "root-folder-id",
        version = 1
    )

    @Test
    fun testSerializeFolderCreateResponse() {
        val json = apiV1Mapper.writeValueAsString(createFolderResponse)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"requestId\":\\s*\"2345\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"parentFolderId\":\\s*\"root-folder-id\""))
        assertContains(json, Regex("\"folderId\":\\s*\"new-folder-id\""))
        assertContains(json, Regex("\"version\":\\s*1"))
    }

    @Test
    fun testDeserializeFolderCreateResponse() {
        val json = apiV1Mapper.writeValueAsString(createFolderResponse)
        val obj = apiV1Mapper.readValue(json, IFolderResponse::class.java) as FolderCreateResponse

        assertEquals(createFolderResponse, obj)
    }
}