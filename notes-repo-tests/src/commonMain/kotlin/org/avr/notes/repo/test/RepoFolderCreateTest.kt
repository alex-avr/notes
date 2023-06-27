package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Instant
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.repo.folder.DbFolderRequest
import org.avr.notes.common.repo.folder.IFolderRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoFolderCreateTest {
    abstract val repo: IFolderRepository

    private val newObject = Folder(
        id = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        parentFolderId = FolderId.NONE,
        title = "Test Folder",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-02-01T10:00Z"),
        version = 1
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createFolder(DbFolderRequest(newObject))
        val expected = newObject.copy()

        assertEquals(true, result.isSuccess)
        assertEquals(expected.id, result.data?.id)
        assertEquals(expected.parentFolderId, result.data?.parentFolderId)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.createTime, result.data?.createTime)
        assertEquals(expected.updateTime, result.data?.updateTime)
        assertEquals(expected.version, result.data?.version)
    }

    companion object: BaseInitAds("createFolder") {
        override val initObjects: List<IFolderChild>
            get() = emptyList()

    }
}