package org.avr.notes.app.stub

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.avr.notes.api.v1.models.*
import org.avr.notes.app.moduleJvm
import org.avr.notes.app.v1.DebugHeaders
import org.avr.notes.stub.FolderStub
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Тесты для API папок, использующие заглушки
 * todo: актуализировать после полной реализаии работы с заглушками
 */
class V1FolderStubApiTest {
    private val requestId = "cfa57cd5-9acd-4bf5-94e0-48161b9da987"
    private val folderId = "8640c047-a6a1-4c5b-b3a8-204c809bdb1d"

    @Test
    fun testCreateFolder() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val folder = FolderStub.create()
        ktorClient().use { client ->
            val response = client.post("/v1/folders") {
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = FolderCreateRequest(
                    requestType = "create",
                    requestId = requestId,
                    parentFolderId = null,
                    folderData = FolderData(
                        title = folder.title,
                        createTime = folder.createTime.toString(),
                        updateTime = folder.updateTime.toString()
                    )
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<FolderCreateResponse>()
            println(responseObj)
            assertEquals(responseObj.requestId, requestId)
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
            assertEquals(1, responseObj.version)
            assertEquals(folderId, responseObj.folderId)
        }
    }

    @Test
    fun testUpdateFolder() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val folder = FolderStub.update()
        ktorClient().use { client ->
            val response = client.put("/v1/folders") {
                url {
                    appendPathSegments(folderId)
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = FolderUpdateRequest(
                    requestType = "update",
                    requestId = requestId,
                    folderData = FolderData(
                        title = folder.title,
                        createTime = folder.createTime.toString(),
                        updateTime = folder.updateTime.toString()
                    ),
                    folderInfo = FolderBriefInfo(
                        folderId,
                        null,
                        2
                    )
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<FolderUpdateResponse>()
            println(responseObj)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
            assertEquals(1, responseObj.version)
            assertEquals(folderId, responseObj.folderId)
        }
    }

    @Test
    fun testGetFolderInfo() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val folder = FolderStub.getInfo()
        ktorClient().use { client ->
            val response = client.get("/v1/folders") {
                url {
                    appendPathSegments(folderId)
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = FolderGetInfoRequest(
                    requestType = "getFolderInfo",
                    requestId = requestId,
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<FolderGetInfoResponse>()
            println(responseObj)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
            assertEquals(folder.title, responseObj.folderInfo?.title)
            assertEquals(1, responseObj.folderInfo?.version)
            assertEquals(folderId, responseObj.folderInfo?.folderId)
        }
    }

    @Test
    fun testDeleteFolder() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        ktorClient().use { client ->
            val response = client.delete("/v1/folders") {
                url {
                    appendPathSegments(folderId)
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = FolderDeleteRequest(
                    requestType = "delete",
                    requestId = requestId,
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<FolderDeleteResponse>()
            println(responseObj)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
        }
    }

    @Test
    fun testGetFolderChildren() = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val folder = FolderStub.folderWithChildren()
        ktorClient().use { client ->
            val response = client.get("/v1/folders") {
                url {
                    appendPathSegments(folderId, "children")
                }
                headers {
                    append(DebugHeaders.HEADER_REQUEST_ID, requestId)
                    append(DebugHeaders.HEADER_WORK_MODE, "stub")
                    append(DebugHeaders.HEADER_STUB_TYPE, "success")
                }
                val request = FolderGetChildrenRequest(
                    requestType = "getFolderChildren",
                    requestId = requestId,
                )
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseObj = response.body<FolderGetChildrenResponse>()
            println(responseObj)
            assertEquals(requestId, responseObj.requestId)
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
        }
    }
}