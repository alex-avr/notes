package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.repo.folder.DbFolderRequest
import org.avr.notes.common.repo.folder.IFolderRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoFolderUpdateTest {
    abstract val repo: IFolderRepository

    protected open val updateSucceededObject = initObjects.first()
    private val updateIdNotFound = FolderId("folder-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        Folder(
            id = updateSucceededObject.id,
            title = "update object"
        )
    }
    private val reqUpdateNotFound = Folder(
        id = updateIdNotFound,
        title = "update object not found"
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateFolder(DbFolderRequest(reqUpdateSucc))

        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateFolder(DbFolderRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<Folder> = listOf(
            createInitTestFolder("update")
        )
    }
}