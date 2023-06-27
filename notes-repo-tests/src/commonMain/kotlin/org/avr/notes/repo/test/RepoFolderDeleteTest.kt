package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.repo.folder.DbFolderIdRequest
import org.avr.notes.common.repo.folder.IFolderRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoFolderDeleteTest {
    abstract val repo: IFolderRepository

    protected open val deleteSucceededObject = initObjects.first() as Folder

    @Test
    fun deleteSuccess() = runRepoTest {
        val id = deleteSucceededObject.id
        val result = repo.deleteFolder(DbFolderIdRequest(id))
        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitAds("deleteFolder") {
        override val initObjects: List<IFolderChild> = listOf(
            createInitTestFolder("delete"),
        )
    }
}