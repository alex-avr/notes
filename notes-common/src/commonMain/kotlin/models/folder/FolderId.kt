package org.avr.notes.common.models.folder

import kotlin.jvm.JvmInline

@JvmInline
value class FolderId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FolderId("")
    }
}