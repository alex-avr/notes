package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.common.NotesContext
import org.avr.notes.common.models.NotesCommand
import org.avr.notes.common.models.folder.Folder

private fun folderRequestDataToInternal(idString: String?, parentFolderIdString: String?, folderData: FolderData?, version: Int?): Folder = Folder(
    id = getFolderId(idString),
    parentFolderId = getFolderId(parentFolderIdString),
    title = folderData?.title ?: "",
    createTime = instantFromString(folderData?.createTime),
    updateTime = instantFromString(folderData?.updateTime),
    version = version ?: 1
)

fun NotesContext.fromTransport(request: IFolderRequest) = when (request) {
    is FolderCreateRequest -> fromTransport(request)
    is FolderUpdateRequest -> fromTransport(request)
    is FolderGetInfoRequest -> fromTransport(request)
    is FolderGetChildrenRequest -> fromTransport(request)
    is FolderDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request.javaClass)
}
private fun NotesContext.fromTransport(request: FolderCreateRequest) {
    command = NotesCommand.CREATE_FOLDER
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(null, request.parentFolderId, request.folderData, null)
}

private fun NotesContext.fromTransport(request: FolderUpdateRequest) {
    command = NotesCommand.UPDATE_FOLDER
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderInfo?.folderId, request.folderInfo?.parentFolderId, request.folderData, request.folderInfo?.version)
}

private fun NotesContext.fromTransport(request: FolderGetInfoRequest) {
    command = NotesCommand.GET_FOLDER_INFO
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderId, null, null, null)
}

private fun NotesContext.fromTransport(request: FolderGetChildrenRequest) {
    command = NotesCommand.GET_FOLDER_CHILDREN
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderId, null, null, null)
}

private fun NotesContext.fromTransport(request: FolderDeleteRequest) {
    command = NotesCommand.DELETE_FOLDER
    requestId = getRequestId(request)

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    folderRequest = folderRequestDataToInternal(request.folderId, null, null, null)
}