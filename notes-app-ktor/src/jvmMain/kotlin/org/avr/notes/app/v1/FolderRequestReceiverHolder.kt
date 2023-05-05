package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import org.avr.notes.api.v1.models.FolderCreateRequest
import org.avr.notes.api.v1.models.FolderUpdateRequest
import org.avr.notes.api.v1.models.IFolderRequest
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.mappers.MissingRequestParameterException

typealias FolderRequestReceiver = suspend (call: ApplicationCall) -> Pair<FolderRequestParameters, IFolderRequest?>

/**
 * Класс, который занимается хранением и поиском обработчиков-разборщиком входящих запросов для папок
 */
object FolderRequestReceiverHolder {
    private const val REQUEST_PARAM_FOLDER_ID = "folderId"

    private val folderRequestTagToTypeMap = mapOf<FolderCommand, FolderRequestReceiver>(
        FolderCommand.CREATE_FOLDER to FolderRequestReceiverHolder::folderCreateRequestReceiver,
        FolderCommand.UPDATE_FOLDER to FolderRequestReceiverHolder::folderUpdateRequestReceiver,
        FolderCommand.DELETE_FOLDER to FolderRequestReceiverHolder::folderDeleteRequestReceiver,
        FolderCommand.GET_FOLDER_INFO to FolderRequestReceiverHolder::folderGetInfoRequestReceiver,
        FolderCommand.GET_FOLDER_CHILDREN to FolderRequestReceiverHolder::folderGetChildrenRequestReceiver
    )

    fun findFolderRequestReceiver(command: FolderCommand): FolderRequestReceiver = folderRequestTagToTypeMap.getValue(command)

    private suspend fun folderCreateRequestReceiver(call: ApplicationCall): Pair<FolderRequestParameters, IFolderRequest?> {
        val requestBody = call.receive<FolderCreateRequest>()
        return Pair(FolderRequestParameters(), requestBody)
    }

    private suspend fun folderUpdateRequestReceiver(call: ApplicationCall): Pair<FolderRequestParameters, IFolderRequest?> {
        val folderId = call.parameters[REQUEST_PARAM_FOLDER_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_FOLDER_ID
        )
        val folderRequestParameters = FolderRequestParameters(folderId)
        val requestBody: IFolderRequest = call.receive<FolderUpdateRequest>()
        return Pair(folderRequestParameters, requestBody)
    }

    private suspend fun folderDeleteRequestReceiver(call: ApplicationCall): Pair<FolderRequestParameters, IFolderRequest?> {
        val folderId = call.parameters[REQUEST_PARAM_FOLDER_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_FOLDER_ID
        )
        val folderRequestParameters = FolderRequestParameters(folderId)
        return Pair(folderRequestParameters, null)
    }

    private suspend fun folderGetInfoRequestReceiver(call: ApplicationCall): Pair<FolderRequestParameters, IFolderRequest?> {
        val folderId = call.parameters[REQUEST_PARAM_FOLDER_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_FOLDER_ID
        )
        val folderRequestParameters = FolderRequestParameters(folderId)
        return Pair(folderRequestParameters, null)
    }

    private suspend fun folderGetChildrenRequestReceiver(call: ApplicationCall): Pair<FolderRequestParameters, IFolderRequest?> {
        val folderId = call.parameters[REQUEST_PARAM_FOLDER_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_FOLDER_ID
        )
        val folderRequestParameters = FolderRequestParameters(folderId)
        return Pair(folderRequestParameters, null)
    }
}