package org.avr.notes.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Instant
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.repo.note.DbNoteRequest
import org.avr.notes.common.repo.note.INoteRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoNoteCreateTest {
    abstract val repo: INoteRepository

    private val newObject = Note(
        id = NoteId("ecf4e57f-ad4d-4dbe-8854-55fbfe10554d"),
        parentFolderId = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        title = "New note",
        body = "This is a test note",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-03-02T11:00Z"),
        version = 1
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createNote(DbNoteRequest(newObject))
        val expected = newObject.copy()

        assertEquals(true, result.isSuccess)
        assertEquals(expected.id, result.data?.id)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.body, result.data?.body)
        assertEquals(expected.createTime, result.data?.createTime)
        assertEquals(expected.updateTime, result.data?.updateTime)
        assertEquals(expected.version, result.data?.version)
    }

    companion object: BaseInitAds("createNote") {
        override val initObjects: List<IFolderChild>
            get() = emptyList()

    }
}