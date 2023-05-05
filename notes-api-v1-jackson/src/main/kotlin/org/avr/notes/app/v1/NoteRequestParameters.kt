package org.avr.notes.app.v1

data class NoteRequestParameters(
    val noteId: String? = null,
    val parentFolderId: String? = null,
    val noteSearchFilter: String? = null
)
