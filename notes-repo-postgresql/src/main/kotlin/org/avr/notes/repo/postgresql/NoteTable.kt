package org.avr.notes.repo.postgresql

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.*

object NoteTable : IdTable<UUID>("notes") {
    override val id = uuid("id").entityId()
    val parentFolderId = uuid("parent_id").nullable()
    val title = varchar("title", 128)
    val body = text("title")
    val createTime = timestamp("created_time")
    val updateTime = timestamp("updated_time").nullable()
    val version = ushort("version")

    override val primaryKey = PrimaryKey(id)

    fun from(res : InsertStatement<Number>) = Note(
        id = NoteId(res[id].toString()),
        parentFolderId = FolderId(res[parentFolderId].toString()),
        title = res[title],
        body = res[body],
        createTime = res[createTime].toKotlinInstant(),
        updateTime = res[updateTime]?.toKotlinInstant() ?: Instant.NONE,
        version = res[version].toInt()
    )

    fun from(res : ResultRow) = Note(
        id = NoteId(res[id].toString()),
        parentFolderId = FolderId(res[parentFolderId].toString()),
        title = res[title],
        body = res[body],
        createTime = res[createTime].toKotlinInstant(),
        updateTime = res[updateTime]?.toKotlinInstant() ?: Instant.NONE,
        version = res[version].toInt()
    )

    fun to(it: UpdateBuilder<*>, note: Note) {
        it[id] = UUID.fromString(note.id.asString())
        it[parentFolderId] = UUID.fromString(note.parentFolderId.asString())
        it[title] = note.title
        it[body] = note.body
        it[createTime] = note.createTime.toJavaInstant()
        it[updateTime] = note.updateTime.takeIf { it != Instant.NONE }?.toJavaInstant()
        it[version] = note.version.toUShort()
    }
}