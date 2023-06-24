package org.avr.notes.common.models

import kotlinx.datetime.Instant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId

data class Note(
    var id: NoteId = NoteId.NONE,
    var parentFolderId: FolderId = FolderId.NONE,
    var title: String = "",
    var body: String = "",
    var createTime: Instant = Instant.NONE,
    var updateTime: Instant = Instant.NONE,
    var version: Int = 0,
    override val folderChildType: FolderChildType = FolderChildType.NOTE
) : IFolderChild {
    fun deepCopy() = copy()
}