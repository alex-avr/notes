package org.avr.notes.repo.inmemory.model

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Instant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.folder.FolderId

data class FolderEntity(
    override val id: String,
    override val parentFolderId: String? = null,
    val title: String? = null,
    val createTime: Instant? = null,
    val updateTime: Instant? = null,
    val version: Int = 1
) : IEntity {
    constructor(model: Folder) : this(
        id = model.id.asString().takeIf { it.isNotBlank() } ?: uuid4().toString(),
        parentFolderId = model.parentFolderId?.asString().takeIf { it?.isNotBlank() ?: false },
        title = model.title.takeIf { it.isNotBlank() },
        createTime = model.createTime,
        updateTime = model.updateTime,
        version = model.version
    )

    fun toInternal() = Folder(
        id = FolderId(id),
        parentFolderId = parentFolderId?.let { FolderId(it) } ?: FolderId.NONE,
        title = title?: "",
        createTime = createTime ?: Instant.NONE,
        updateTime = updateTime ?: Instant.NONE,
        version = version
    )
}
