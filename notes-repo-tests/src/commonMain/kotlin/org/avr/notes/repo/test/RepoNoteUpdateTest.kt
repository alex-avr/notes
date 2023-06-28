package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.repo.note.DbNoteRequest
import org.avr.notes.common.repo.note.INoteRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNoteUpdateTest {
    abstract val repo: INoteRepository

    protected open val updateSucceededObject = initObjects.first()
    private val updateIdNotFound = NoteId("note-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        Note(
            id = updateSucceededObject.id,
            title = "update object"
        )
    }
    private val reqUpdateNotFound = Note(
        id = updateIdNotFound,
        title = "update object not found"
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateNote(DbNoteRequest(reqUpdateSucc))

        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateNote(DbNoteRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<Note> = listOf(
            createInitTestNote("update")
        )
    }
}