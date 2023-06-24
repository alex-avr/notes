package org.avr.notes.common.models

import kotlinx.datetime.Instant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.folder.FolderId

data class Folder(
    var id: FolderId = FolderId.NONE,
    var parentFolderId: FolderId? = FolderId.NONE,
    var title: String = "",
    var createTime: Instant = Instant.NONE,
    var updateTime: Instant = Instant.NONE,
    var version: Int = 1,
    var children: MutableList<IFolderChild> = mutableListOf(),
    override val folderChildType: FolderChildType = FolderChildType.FOLDER
) : IFolderChild {

    fun deepCopy() = copy(
        children = children.toMutableList()
    )
}