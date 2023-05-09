package org.avr.notes.api.v1

/**
 * Параметры запроса для заметок
 */
data class NoteRequestParameters(
    /**
     * Идентификатор заметки
     */
    val noteId: String? = null,

    /**
     * Идентификатор родительской папки
     */
    val parentFolderId: String? = null,

    /**
     * Фильтр для поиск заметок
     */
    val noteSearchFilter: String? = null
)
