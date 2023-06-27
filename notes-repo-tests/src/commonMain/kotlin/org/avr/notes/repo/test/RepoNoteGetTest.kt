package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.repo.note.DbNoteIdRequest
import org.avr.notes.common.repo.note.INoteRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNoteGetTest {
    abstract val repo: INoteRepository

    protected open val getSucceededObject = initObjects.first() as Note

    @Test
    fun getInfoSuccess() = runRepoTest {
        val result = repo.getNote(DbNoteIdRequest(getSucceededObject.id))

        assertEquals(true, result.isSuccess)
        assertEquals(getSucceededObject, result.data)
    }

    @Test
    fun getNotFound() = runRepoTest {
        val result = repo.getNote(DbNoteIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("get") {
        override val initObjects: List<IFolderChild> = listOf(
            createInitTestNote("getInfo"),
        )

        val notFoundId = NoteId("note-repo-read-notFound")
    }
}