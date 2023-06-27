package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.avr.notes.common.models.Note
import org.avr.notes.common.repo.note.DbNoteFilterRequest
import org.avr.notes.common.repo.note.INoteRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNoteSearchTest {
    abstract val repo: INoteRepository

    protected open val initializedObjects: List<Note> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchNotes(DbNoteFilterRequest("note"))
        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(2, result.data?.size)
    }

    companion object: BaseInitAds("search") {
        override val initObjects: List<Note> = listOf(
            createInitTestNote("note1"),
            createInitTestNote("note2")
        )
    }
}