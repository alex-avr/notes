package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.FolderChildType
import org.avr.notes.common.models.folder.FolderCommand

private fun folderRequestDataToInternal(idString: String?, parentFolderIdString: String?, folderData: FolderData?, version: Int?): Folder = Folder(
    id = getFolderId(idString),
    parentFolderId = getFolderId(parentFolderIdString),
    title = folderData?.title ?: "",
    createTime = instantFromString(folderData?.createTime),
    updateTime = instantFromString(folderData?.updateTime),
    version = version ?: 1,
    folderChildType = FolderChildType.FOLDER,
    parent = null
)

fun FolderContext.fromTransport(request: IFolderRequest) = when (request) {
    is FolderCreateRequest -> fromTransport(request)
    is FolderUpdateRequest -> fromTransport(request)
    is FolderGetInfoRequest -> fromTransport(request)
    is FolderGetChildrenRequest -> fromTransport(request)
    is FolderDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request.javaClass)
}
private fun FolderContext.fromTransport(request: FolderCreateRequest) {
    command = FolderCommand.CREATE_FOLDER
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(null, request.parentFolderId, request.folderData, null)
}

private fun FolderContext.fromTransport(request: FolderUpdateRequest) {
    command = FolderCommand.UPDATE_FOLDER
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderInfo?.folderId, request.folderInfo?.parentFolderId, request.folderData, request.folderInfo?.version)
}

private fun FolderContext.fromTransport(request: FolderGetInfoRequest) {
    command = FolderCommand.GET_FOLDER_INFO
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderId, null, null, null)
}

private fun FolderContext.fromTransport(request: FolderGetChildrenRequest) {
    command = FolderCommand.GET_FOLDER_CHILDREN
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderId, null, null, null)
}

private fun FolderContext.fromTransport(request: FolderDeleteRequest) {
    command = FolderCommand.DELETE_FOLDER
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderId, null, null, null)
}