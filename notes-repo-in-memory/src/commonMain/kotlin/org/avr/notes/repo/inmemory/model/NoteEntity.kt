package org.avr.notes.repo.inmemory.model

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Instant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId

data class NoteEntity(
    override val id: String,
    override val parentFolderId: String?,
    val title: String? = null,
    val body: String? = null,
    val createTime: Instant? = null,
    val updateTime: Instant? = null,
    val version: Int = 1
) : IEntity {
    constructor(model: Note) : this(
        id = model.id.asString().takeIf { it.isNotBlank() } ?: uuid4().toString(),
        parentFolderId = model.parentFolderId.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        body = model.body.takeIf { it.isNotBlank() },
        createTime = model.createTime,
        updateTime = model.updateTime,
        version = model.version
    )

    fun toInternal() = Note(
        id = NoteId(id),
        parentFolderId = parentFolderId?.let { FolderId(it) } ?: FolderId.NONE,
        title = title ?: "",
        body = body ?: "",
        createTime = createTime ?: Instant.NONE,
        updateTime = updateTime ?: Instant.NONE,
        version = version
    )
}
