package org.avr.notes.repo.postgresql

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.avr.notes.common.NONE
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.folder.FolderId
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.*

object FolderTable : IdTable<UUID>("folders") {
    override val id = uuid("id").entityId()
    val parentFolderId = uuid("parent_id").nullable()
    val title = varchar("title", 128)
    val createTime = timestamp("created_time")
    val updateTime = timestamp("updated_time").nullable()
    val version = ushort("version")

    override val primaryKey = PrimaryKey(id)

    fun from(res : InsertStatement<Number>) = Folder(
        id = FolderId(res[id].toString()),
        parentFolderId = FolderId(res[parentFolderId].toString()),
        title = res[title],
        createTime = res[createTime].toKotlinInstant(),
        updateTime = res[updateTime]?.toKotlinInstant() ?: Instant.NONE,
        version = res[version].toInt()
    )

    fun from(res : ResultRow) = Folder(
        id = FolderId(res[id].toString()),
        parentFolderId = FolderId(res[parentFolderId].toString()),
        title = res[title],
        createTime = res[createTime].toKotlinInstant(),
        updateTime = res[updateTime]?.toKotlinInstant() ?: Instant.NONE,
        version = res[version].toInt()
    )

    fun to(it: UpdateBuilder<*>, folder: Folder) {
        it[id] = UUID.fromString(folder.id.asString())
        it[parentFolderId] = if (folder.parentFolderId?.asString() != null) UUID.fromString(folder.parentFolderId?.asString()) else null
        it[title] = folder.title
        it[createTime] = folder.createTime.toJavaInstant()
        it[updateTime] = folder.updateTime.takeIf { it != Instant.NONE }?.toJavaInstant()
        it[version] = folder.version.toUShort()
    }
}