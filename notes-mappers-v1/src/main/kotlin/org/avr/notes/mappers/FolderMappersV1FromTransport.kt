package org.avr.notes.mappers

import org.avr.notes.api.v1.FolderRequestParameters
import org.avr.notes.api.v1.RequestDebugParameters
import org.avr.notes.api.v1.models.*
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.FolderChildType
import org.avr.notes.common.models.NotesRequestId
import org.avr.notes.common.models.folder.FolderCommand

private fun folderRequestDataToInternal(idString: String?, parentFolderIdString: String?, folderData: FolderData?, version: Int?): Folder = Folder(
    id = getFolderId(idString),
    parentFolderId = getFolderId(parentFolderIdString),
    title = folderData?.title ?: "",
    createTime = instantFromString(folderData?.createTime),
    updateTime = instantFromString(folderData?.updateTime),
    version = version ?: 1,
    folderChildType = FolderChildType.FOLDER
)

fun FolderContext.fromRequestData(folderCommand: FolderCommand, debugParameters: RequestDebugParameters, requestParameters: FolderRequestParameters, requestBody: IFolderRequest?) {
    requestId = NotesRequestId(debugParameters.requestId)
    workMode = debugParameters.workMode.transportToWorkMode()
    stubCase = debugParameters.stubType.transportToStubCase()

    command = folderCommand

    when (folderCommand) {
        FolderCommand.CREATE_FOLDER -> fromFolderCreateRequestData(requestBody as FolderCreateRequest)
        FolderCommand.UPDATE_FOLDER -> fromFolderUpdateRequestData(requestParameters, requestBody as FolderUpdateRequest)
        FolderCommand.DELETE_FOLDER -> fromFolderDeleteRequestData(requestParameters)
        FolderCommand.GET_FOLDER_CHILDREN -> fromFolderGetChildrenRequestData(requestParameters)
        FolderCommand.GET_FOLDER_INFO -> fromFolderGetInfoRequestData(requestParameters)
        else -> throw UnknownFolderCommandException(command)
    }
}
private fun FolderContext.fromFolderCreateRequestData(request: FolderCreateRequest) {
    folderRequest = folderRequestDataToInternal(null, request.parentFolderId, request.folderData, null)
}

private fun FolderContext.fromFolderUpdateRequestData(requestParameters: FolderRequestParameters, request: FolderUpdateRequest) {
    val folderId = requestParameters.folderId ?: throw MissingRequestParameterException("folderId")

    folderRequest = folderRequestDataToInternal(folderId, request.folderInfo?.parentFolderId, request.folderData, request.folderInfo?.version)
}

private fun FolderContext.fromFolderDeleteRequestData(requestParameters: FolderRequestParameters) {
    val folderId = requestParameters.folderId ?: throw MissingRequestParameterException("folderId")

    folderRequest = folderRequestDataToInternal(folderId, null, null, null)
}

private fun FolderContext.fromFolderGetChildrenRequestData(requestParameters: FolderRequestParameters) {
    val folderId = requestParameters.folderId ?: throw MissingRequestParameterException("folderId")

    folderRequest = folderRequestDataToInternal(folderId, null, null, null)
}
private fun FolderContext.fromFolderGetInfoRequestData(requestParameters: FolderRequestParameters) {
    val folderId = requestParameters.folderId ?: throw MissingRequestParameterException("folderId")

    folderRequest = folderRequestDataToInternal(folderId, null, null, null)
}

fun FolderContext.fromTransport(request: WsRequest) {
    val folderRequestData = request.folderRequestData
    val folderCommand = folderCommandByRequest(folderRequestData)

    val requestDebugParameters = request.requestDebugParameters()

    fromRequestData(folderCommand = folderCommand,
        debugParameters = requestDebugParameters,
        requestParameters = FolderRequestParameters(
            folderId = request.requestParameters?.itemId
        ),
        requestBody = request.folderRequestData
    )
}

private fun folderCommandByRequest(folderRequestData: IFolderRequest?): FolderCommand {
    return when (folderRequestData) {
        is FolderCreateRequest -> FolderCommand.CREATE_FOLDER
        is FolderUpdateRequest -> FolderCommand.UPDATE_FOLDER
        is FolderDeleteRequest -> FolderCommand.DELETE_FOLDER
        is FolderGetInfoRequest -> FolderCommand.GET_FOLDER_INFO
        is FolderGetChildrenRequest -> FolderCommand.GET_FOLDER_CHILDREN
        null -> throw MissingRequestParameterException("folderRequestData")
        else -> throw UnknownRequestException(folderRequestData.javaClass)
    }
}



