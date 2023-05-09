package org.avr.notes.mappers

import kotlinx.datetime.Instant
import org.avr.notes.api.v1.models.*
import org.avr.notes.api.v1.FolderRequestParameters
import org.avr.notes.api.v1.RequestDebugParameters
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.NotesRequestId
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.stubs.NotesStubs
import org.junit.Test
import kotlin.test.assertEquals

class FolderMappersTest {
    @Test
    fun testCreateFolderFromTransport() {
        val requestBody = FolderCreateRequest(
            requestType = "create",
            requestId = "test_id",
            parentFolderId = null,
            folderData = FolderData("new_folder", "2023-04-03T10:00:00Z", "2023-04-04T12:00:00Z")
        )

        val context = FolderContext()

        val folderCommand = FolderCommand.CREATE_FOLDER
        val debugParameters = RequestDebugParameters(requestId = "test_id",
            workMode = RequestWorkMode.STUB,
            stubType = RequestStubType.SUCCESS)
        val requestParameters = FolderRequestParameters()
        context.fromRequestData(folderCommand, debugParameters, requestParameters, requestBody)

        assertEquals(FolderCommand.CREATE_FOLDER, context.command)
        assertEquals(NotesState.NONE, context.state)
        assertEquals(NotesWorkMode.STUB, context.workMode)
        assertEquals(NotesStubs.SUCCESS, context.stubCase)
        assertEquals("test_id", context.requestId.asString())
        assertEquals(FolderId.NONE, context.folderRequest.parentFolderId)
        assertEquals("new_folder", context.folderRequest.title)
        assertEquals("2023-04-03T10:00:00Z", context.folderRequest.createTime.toString())
        assertEquals("2023-04-04T12:00:00Z", context.folderRequest.updateTime.toString())
    }

    @Test
    fun testFolderGetInfoResponseToTransport() {
        val context = FolderContext(
            command = FolderCommand.GET_FOLDER_INFO,
            state = NotesState.RUNNING,
            workMode = NotesWorkMode.PROD,
            stubCase = NotesStubs.NONE,
            requestId = NotesRequestId("test_id_2"),
            folderResponse = Folder(
                id = FolderId("folder_id"),
                parentFolderId = FolderId("parent_folder_id_1"),
                title = "The First folder",
                createTime = Instant.parse("2023-04-02T10:25:00Z"),
                updateTime = Instant.parse("2023-04-02T10:30:00Z"),
                version = 2
            )
        )

        val response = context.toTransport() as FolderGetInfoResponse

        assertEquals("read", response.responseType)
        assertEquals("test_id_2", response.requestId)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("folder_id", response.folderInfo?.folderId)
        assertEquals("parent_folder_id_1", response.folderInfo?.parentFolderId)
        assertEquals("The First folder", response.folderInfo?.title)
        assertEquals(2, response.folderInfo?.version)
        assertEquals("2023-04-02T10:25:00Z", response.folderInfo?.createTime.toString())
        assertEquals("2023-04-02T10:30:00Z", response.folderInfo?.updateTime.toString())
    }
}