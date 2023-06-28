package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.repo.note.DbNoteIdRequest
import org.avr.notes.common.repo.note.INoteRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNoteDeleteTest {
    abstract val repo: INoteRepository

    protected open val deleteSucceededObject = initObjects.first() as Note

    @Test
    fun deleteSuccess() = runRepoTest {
        val id = deleteSucceededObject.id
        val result = repo.deleteNote(DbNoteIdRequest(id))
        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitAds("deleteNote") {
        override val initObjects: List<IFolderChild> = listOf(
            createInitTestNote("delete"),
        )
    }
}