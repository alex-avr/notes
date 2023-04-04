package org.avr.notes.common.models.note

/**
 * Класс фильтра для поиска заметок
 */
data class NoteSearchFilter(val searchFilter: String) {
    companion object {
        val NONE: NoteSearchFilter = NoteSearchFilter("")
    }
}