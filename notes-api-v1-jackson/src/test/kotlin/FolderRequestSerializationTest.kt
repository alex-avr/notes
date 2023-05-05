package org.avr.notes.api.v1

import org.avr.notes.api.v1.models.FolderCreateRequest
import org.avr.notes.api.v1.models.FolderData
import org.avr.notes.api.v1.models.IFolderRequest
import org.avr.notes.app.v1.apiV1Mapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

/**
 * Тесты для проверки сериализации и десериализации транспортных моделей запросов для папок
 */
class FolderRequestSerializationTest {
    private val createFolderRequest = FolderCreateRequest(
        requestType = "create",
        requestId = "1341",
        parentFolderId = null,
        folderData = FolderData(
            title = "Root Folder",
            createTime = "2023-02-01T10:00",
            updateTime = "2023-02-01T10:23"
        )
    )

    @Test
    fun testSerializeFolderCreateRequest() {
        val json = apiV1Mapper.writeValueAsString(createFolderRequest)

        assertContains(json, Regex("\"title\":\\s*\"Root Folder\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun testDeserializeFolderCreateRequest() {
        val json = apiV1Mapper.writeValueAsString(createFolderRequest)
        val obj = apiV1Mapper.readValue(json, IFolderRequest::class.java) as FolderCreateRequest

        assertEquals(createFolderRequest, obj)
    }
}