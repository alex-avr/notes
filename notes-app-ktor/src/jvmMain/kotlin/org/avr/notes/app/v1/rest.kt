package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.avr.notes.app.common.NotesAppSettings


fun Route.v1Folders(appSettings: NotesAppSettings) {
    val loggerFolders = appSettings.corSettings.loggerProvider.logger(Route::v1Folders::class)
    val folderController = FolderController(appSettings, loggerFolders)

    route("folders") {
        post {
            folderController.createFolder(call)
        }

        put("/{folderId}") {
            folderController.updateFolder(call)
        }

        get("/{folderId}") {
            folderController.getFolderInfo(call)
        }

        delete("/{folderId}") {
            folderController.deleteFolder(call)
        }

        get("/{folderId}/children") {
            folderController.getFolderChildren(call)
        }
    }
}

fun Route.v1Notes(appSettings: NotesAppSettings) {
    val loggerNotes = appSettings.corSettings.loggerProvider.logger(Route::v1Notes::class)
    val noteController = NoteController(appSettings, loggerNotes)
    route("notes") {
        post {
            noteController.createNote(call)
        }

        put("/{noteId}") {
            noteController.updateNote(call)
        }

        get("/{noteId}") {
            noteController.readNote(call)
        }

        delete("/{noteId}") {
            noteController.deleteNote(call)
        }

        get("/search") {
            noteController.searchNotes(call)
        }
    }
}