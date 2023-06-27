package org.avr.notes.stub

import kotlinx.datetime.Instant
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId

object NoteStub {
    fun create() = Note(
        id = NoteId("ecf4e57f-ad4d-4dbe-8854-55fbfe10554d"),
        parentFolderId = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        title = "New note",
        body = "This is a test note",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-03-02T11:00Z"),
        version = 1
    )

    fun update() = Note(
        id = NoteId("ecf4e57f-ad4d-4dbe-8854-55fbfe10554d"),
        parentFolderId = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        title = "Updated note",
        body = "This is a test updated note",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-03-02T11:00Z"),
        version = 2
    )

    fun get() = update()

    fun searchResults() = mutableListOf(get())

    fun prepareResult(block: Note.() -> Unit): Note = get().apply(block)
}