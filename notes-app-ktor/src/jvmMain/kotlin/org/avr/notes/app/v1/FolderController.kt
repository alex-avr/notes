package org.avr.notes.app.v1

import io.ktor.server.application.*
import org.avr.notes.api.v1.models.FolderCreateRequest
import org.avr.notes.api.v1.models.FolderCreateResponse
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.logging.common.INotesLoggerWrapper

suspend fun createFolder(call: ApplicationCall, appSettings: NotesAppSettings, logger: INotesLoggerWrapper) {
    processFolderRequestV1<FolderCreateRequest, FolderCreateResponse>(call, appSettings, logger, "folder-create", FolderCommand.CREATE_FOLDER)
}

suspend fun updateFolder(call: ApplicationCall, appSettings: NotesAppSettings, logger: INotesLoggerWrapper) {
    //call.parameters["folderId"]
}

suspend fun getFolderInfo(call: ApplicationCall, appSettings: NotesAppSettings, logger: INotesLoggerWrapper) {

}

suspend fun deleteFolder(call: ApplicationCall, appSettings: NotesAppSettings, logger: INotesLoggerWrapper) {

}

suspend fun getFolderChildren(call: ApplicationCall, appSettings: NotesAppSettings, logger: INotesLoggerWrapper) {

}

