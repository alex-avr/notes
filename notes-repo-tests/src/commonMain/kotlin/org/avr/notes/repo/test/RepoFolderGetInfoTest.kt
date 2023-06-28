package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.repo.folder.DbFolderIdRequest
import org.avr.notes.common.repo.folder.IFolderRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoFolderGetInfoTest {
    abstract val repo: IFolderRepository

    protected open val getInfoSucceededObject = initObjects[0] as Folder

    @Test
    fun getInfoSuccess() = runRepoTest {
        val result = repo.getFolderInfo(DbFolderIdRequest(getInfoSucceededObject.id))

        assertEquals(true, result.isSuccess)
        assertEquals(getInfoSucceededObject, result.data)
    }

    @Test
    fun getNotFound() = runRepoTest {
        val result = repo.getFolderInfo(DbFolderIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("getFolderInfo") {
        override val initObjects: List<IFolderChild> = listOf(
            createInitTestFolder("getInfo"),
        )

        val notFoundId = FolderId("folder-repo-read-notFound")
    }
}