package org.avr.notes.common.models.folder

import kotlinx.datetime.Instant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.FolderChild
import org.avr.notes.common.models.FolderChildType
import org.avr.notes.common.models.IFolderChild

data class Folder(
    var id: FolderId = FolderId.NONE,
    var parentFolderId: FolderId = FolderId.NONE,
    var title: String = "",
    var createTime: Instant = Instant.NONE,
    var updateTime: Instant = Instant.NONE,
    var version: Int = 1,
    var children: MutableList<IFolderChild> = mutableListOf()
) : FolderChild(FolderChildType.FOLDER, null)