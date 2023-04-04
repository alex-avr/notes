package org.avr.notes.common.models

enum class NotesCommand {
    NONE,

    // Notes
    CREATE_NOTE,
    READ_NOTE,
    UPDATE_NOTE,
    DELETE_NOTE,
    SEARCH_NOTE,

    // Folders
    CREATE_FOLDER,
    UPDATE_FOLDER,
    GET_FOLDER_INFO,
    GET_FOLDER_CHILDREN,
    DELETE_FOLDER
}