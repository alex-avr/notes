package org.avr.notes.stub

import kotlinx.datetime.Instant
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId

object FolderStub {
    fun create() = Folder(
        id = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        parentFolderId = FolderId.NONE,
        title = "Test Folder",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-02-01T10:00Z"),
        version = 1
    )

    fun update() = Folder(
        id = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        parentFolderId = FolderId.NONE,
        title = "Test Folder Updated",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-03-02T11:00Z"),
        version = 2
    )
    fun getInfo() = create()

    fun folderWithChildren() = Folder(
        id = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
        parentFolderId = FolderId.NONE,
        title = "Test Folder Updated",
        createTime = Instant.parse("2023-02-01T10:00Z"),
        updateTime = Instant.parse("2023-03-02T11:00Z"),
        version = 3,
        mutableListOf(
            Note(
                id = NoteId("ecf4e57f-ad4d-4dbe-8854-55fbfe10554d"),
                parentFolderId = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
                title = "New note",
                body = "This is a test note",
                createTime = Instant.parse("2023-02-01T10:00Z"),
                updateTime = Instant.parse("2023-03-02T11:00Z"),
                version = 1
            ),
            Folder(
                id = FolderId("7d40a69e-b3e9-4629-9d76-e358e51f0b10"),
                parentFolderId = FolderId("8640c047-a6a1-4c5b-b3a8-204c809bdb1d"),
                title = "Sample folder",
                createTime = Instant.parse("2023-02-01T10:00Z"),
                updateTime = Instant.parse("2023-03-02T11:00Z"),
                version = 1
            )
        )
    )
}