package org.avr.notes.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class NotesRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = NotesRequestId("")
    }
}