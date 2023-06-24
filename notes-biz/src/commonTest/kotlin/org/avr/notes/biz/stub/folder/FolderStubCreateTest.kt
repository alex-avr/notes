package org.avr.notes.biz.stub.folder

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.avr.notes.biz.FolderProcessor
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.stubs.NotesStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FolderStubCreateTest {
    private val processor = FolderProcessor()

    private val id = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d")
    private val title = "Test Folder"
    private val createTime = Instant.parse("2023-02-01T10:00Z")
    private val updateTime = Instant.parse("2023-02-01T10:00Z")
    private val version = 1

    @Test
    fun create() = runTest {
        val ctx = FolderContext(
            command = FolderCommand.CREATE_FOLDER,
            state = NotesState.NONE,
            workMode = NotesWorkMode.STUB,
            stubCase = NotesStubs.SUCCESS,
            folderRequest = Folder(
                id = id,
                parentFolderId = FolderId.NONE,
                title = title
            )
        )

        processor.exec(ctx)

        assertEquals(id, ctx.folderResponse.id)
        assertEquals(FolderId.NONE, ctx.folderResponse.parentFolderId)
        assertEquals(title, ctx.folderResponse.title)
        assertEquals(createTime, ctx.folderResponse.createTime)
        assertEquals(version, ctx.folderResponse.version)
    }
}