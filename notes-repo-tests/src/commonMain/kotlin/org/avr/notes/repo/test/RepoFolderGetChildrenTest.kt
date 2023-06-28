package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.repo.folder.DbFolderIdRequest
import org.avr.notes.common.repo.folder.IFolderRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoFolderGetChildrenTest {
    abstract val repo: IFolderRepository

    @Test
    fun getChildrenSuccess() = runRepoTest {
        val parentFolder = initObjects.first() as Folder
        val result = repo.getFolderChildren(DbFolderIdRequest(parentFolder.id))

        assertEquals(true, result.isSuccess)
        assertEquals(0, result.errors.size)
        val childList: List<IFolderChild>? = result.data
        assertNotNull(childList)
        assertEquals(2, childList.size)
        assertTrue(childList.any { it is Folder }, "There has to be a folder")
        assertTrue(childList.any { it is Note }, "There has to be a note")
    }

    @Test
    fun getChildrenNotFound() = runRepoTest {
        val result = repo.getFolderChildren(DbFolderIdRequest(notFoundId))

        assertEquals(true, result.isSuccess)
        assertEquals(0, result.data?.size)
    }

    companion object : BaseInitAds("getFolderChildren") {
        private val parentFolder = createInitTestFolder("parentFolder")
        private val childFolder = createInitTestFolder("childFolder").apply { parentFolderId = parentFolder.id }
        private val childNote = createInitTestNote("childNote").apply { parentFolderId = parentFolder.id }

        override val initObjects: List<IFolderChild> = listOf(
            parentFolder,
            childFolder,
            childNote
        )

        val notFoundId = FolderId("ad-repo-read-notFound")
    }
}