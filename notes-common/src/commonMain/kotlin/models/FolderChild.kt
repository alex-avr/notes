package org.avr.notes.common.models

open class FolderChild(
    override val folderChildType: FolderChildType,
    override var parent: IFolderChild?) : IFolderChild {
}