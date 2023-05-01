package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.avr.notes.app.common.NotesAppSettings


fun Route.v1Folders(appSettings: NotesAppSettings) {
    val loggerFolders = appSettings.corSettings.loggerProvider.logger(Route::v1Folders::class)
    route("folders") {
        post {
            createFolder(call, appSettings, loggerFolders)
        }

        put("/{folderId}") {
            updateFolder(call, appSettings, loggerFolders)
        }

        get("/{folderId}") {
            getFolderInfo(call, appSettings, loggerFolders)
        }

        delete("/{folderId}") {
            deleteFolder(call, appSettings, loggerFolders)
        }

        get("/{folderId}/children") {
            getFolderChildren(call, appSettings, loggerFolders)
        }
    }
}

fun Route.v1Notes(appSettings: NotesAppSettings) {
    val loggerNotes = appSettings.corSettings.loggerProvider.logger(Route::v1Notes::class)
    route("notes") {
        post {
            createNote(call, appSettings, loggerNotes)
        }

        put("/{noteId}") {
            updateNote(call, appSettings, loggerNotes)
        }

        get("/{noteId}") {
            updateNote(call, appSettings, loggerNotes)
        }

        delete("/{noteId}") {
            updateNote(call, appSettings, loggerNotes)
        }

        get("/search") {
            searchNotes(call, appSettings, loggerNotes)
        }
    }
}