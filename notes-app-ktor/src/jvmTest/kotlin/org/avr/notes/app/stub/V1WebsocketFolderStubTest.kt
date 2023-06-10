package org.avr.notes.app.stub

import io.ktor.client.plugins.websocket.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import org.avr.notes.api.v1.apiV1Mapper
import org.avr.notes.api.v1.models.*
import org.avr.notes.app.moduleJvm
import org.avr.notes.stub.FolderStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Тесты обработки запросов через WebSockets для папок
 */
class V1WebsocketFolderStubTest {
    private val requestId = "cfa57cd5-9acd-4bf5-94e0-48161b9da987"
    private val folderId = "8640c047-a6a1-4c5b-b3a8-204c809bdb1d"
    @Test
    fun createStub() {
        val folder = FolderStub.create()
        val folderRequest = FolderCreateRequest(
            requestType = "create",
            requestId = requestId,
            parentFolderId = null,
            folderData = FolderData(
                title = folder.title,
                createTime = folder.createTime.toString(),
                updateTime = folder.updateTime.toString()
            )
        )

        val request = WsRequest(
            objectKind = ObjectKind.FOLDER,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS
            ),
            folderRequestData = folderRequest
        )

        testMethod<FolderCreateResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(folderRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(folderId, it.folderId)
            assertEquals(1, it.version)
        }
    }

    @Test
    fun updateStub() {
        val folder = FolderStub.update()
        val folderRequest = FolderUpdateRequest(
            requestType = "update",
            requestId = requestId,
            folderData = FolderData(
                title = folder.title,
                createTime = folder.createTime.toString(),
                updateTime = folder.updateTime.toString()
            ),
            folderInfo = FolderBriefInfo(
                folderId = folderId,
                version = 2
            )
        )

        val request = WsRequest(
            objectKind = ObjectKind.FOLDER,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = folderId
            ),
            folderRequestData = folderRequest
        )

        testMethod<FolderUpdateResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(folderRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(folderId, it.folderId)
        }
    }

    @Test
    fun getInfoStub() {
        val folderRequest = FolderGetInfoRequest(
            requestType = "read",
            requestId = requestId,
        )

        val request = WsRequest(
            objectKind = ObjectKind.FOLDER,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = folderId
            ),
            folderRequestData = folderRequest
        )

        testMethod<FolderGetInfoResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(folderRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(folderId, it.folderInfo?.folderId)
            assertEquals(1, it.folderInfo?.version)
            assertEquals("Test Folder", it.folderInfo?.title)
            assertEquals("2023-02-01T10:00:00Z", it.folderInfo?.createTime)
            assertEquals("2023-02-01T10:00:00Z", it.folderInfo?.updateTime)
        }
    }

    @Test
    fun getChildrenStub() {
        val folderRequest = FolderGetChildrenRequest(
            requestType = "getChildren",
            requestId = requestId,
        )

        val request = WsRequest(
            objectKind = ObjectKind.FOLDER,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = folderId
            ),
            folderRequestData = folderRequest
        )

        testMethod<FolderGetChildrenResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(folderRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
            assertEquals(2, it.children?.size)
        }
    }

    @Test
    fun deleteStub() {
        val folderRequest = FolderDeleteRequest(
            requestType = "delete",
            requestId = requestId
        )

        val request = WsRequest(
            objectKind = ObjectKind.FOLDER,
            requestParameters = WsRequestData(
                requestId = requestId,
                RequestWorkMode.STUB,
                RequestStubType.SUCCESS,
                itemId = folderId
            ),
            folderRequestData = folderRequest
        )

        testMethod<FolderDeleteResponse>(request) {
            assertEquals(requestId, it.requestId)
            assertEquals(folderRequest.requestType, it.responseType)
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> testMethod(
        request: Any,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application {
            moduleJvm()
        }
        environment {
            config = MapApplicationConfig()
        }
        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("/v1/ws") {
            withTimeout(30000) {
                val message = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(message.readText(), WsInitResponse::class.java)
                assertIs<WsInitResponse>(response)
            }
            send(Frame.Text(apiV1Mapper.writeValueAsString(request)))
            withTimeout(30000) {
                val message = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(message.readText(), T::class.java)

                assertBlock(response)
            }
        }
    }
}