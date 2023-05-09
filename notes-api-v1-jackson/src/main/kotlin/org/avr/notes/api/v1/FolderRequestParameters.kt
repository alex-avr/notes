package org.avr.notes.api.v1

/**
 * Возможные параметры запроса для папок
 */
data class FolderRequestParameters(
    /**
     * Идентификатор папки
     */
    val folderId: String? = null
)
