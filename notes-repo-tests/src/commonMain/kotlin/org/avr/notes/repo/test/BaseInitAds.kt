package org.avr.notes.repo.test

import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId

abstract class BaseInitAds(val operation: String) : IInitObjects<IFolderChild> {
    fun createInitTestFolder(
        suf: String
    ) = Folder(
        id = FolderId("repo-folder-$operation-$suf"),
        parentFolderId = FolderId("repo-parent-folder-$operation-$suf"),
        title = "$suf stub"
    )

    fun createInitTestNote(
        suf: String
    ) = Note(
        id = NoteId("repo-note-$operation-$suf"),
        parentFolderId = FolderId("repo-parent-folder-$operation-$suf"),
        title = "$suf stub"
    )
}