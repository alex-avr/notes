package org.avr.notes.mappers

import org.avr.notes.api.v1.models.*
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.folder.FolderErrorCode
import org.avr.notes.common.models.folder.FolderId

private fun Folder.toTransport(): FolderResponseObject = FolderResponseObject(
    title = this.title.takeIf { it.isNotBlank() },
    createTime = this.createTime.toString(),
    updateTime = this.updateTime.toString(),
    folderId = this.id.asString(),
    parentFolderId = this.parentFolderId?.asString() ?: "",
    version = this.version
)

private fun FolderContext.hasUpdateConflict() =
    this.errors.find { it.code == FolderErrorCode.CONFLICT.toString() } != null

fun FolderContext.toTransport(): IFolderResponse = when(command) {
    FolderCommand.CREATE_FOLDER -> toTransportCreate()
    FolderCommand.UPDATE_FOLDER -> if (hasUpdateConflict()) toTransportUpdateConflict() else toTransportUpdate()
    FolderCommand.GET_FOLDER_INFO -> toTransportRead()
    FolderCommand.GET_FOLDER_CHILDREN -> toTransportGetChildren()
    FolderCommand.DELETE_FOLDER -> toTransportDelete()
    FolderCommand.NONE -> throw UnknownFolderCommandException(command)
}

private fun FolderContext.toTransportCreate(): IFolderResponse = FolderCreateResponse(
    responseType = "create",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    folderId = folderResponse.id.asString(),
    parentFolderId = folderResponse.parentFolderId?.asString() ?: "",
    version = folderResponse.version
)
private fun FolderContext.toTransportRead(): IFolderResponse = FolderGetInfoResponse(
    responseType = "read",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    folderInfo = folderResponse.toTransport()
)

private fun FolderContext.toTransportUpdate(): IFolderResponse = FolderUpdateResponse(
    responseType = "update",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    folderId = folderResponse.id.asString(),
    parentFolderId = folderResponse.parentFolderId?.asString() ?: "",
    version = folderResponse.version
)

private fun FolderContext.toTransportUpdateConflict(): IFolderResponse = FolderUpdateConflictResponse(
    responseType = "updateConflict",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    folderInfo = folderResponse.toTransport()
)

private fun FolderContext.folderChildrenToTransport(folderChildrenList: MutableList<IFolderChild>): List<FolderChildrenResponseMultiChildrenInner>? {
    return folderChildrenList.map { it ->
        when (it) {
            is Folder -> FolderChildrenResponseMultiChildrenInner(
                createTime = it.createTime.toString(),
                updateTime = it.updateTime.toString(),
                folderId = it.id.asString(),
                title = it.title,
                parentFolderId = it.parentFolderId.takeIf { id -> id != FolderId.NONE }?.asString(),
                version = it.version
            )
            is Note -> FolderChildrenResponseMultiChildrenInner(
                createTime = it.createTime.toString(),
                updateTime = it.updateTime.toString(),
                noteId = it.id.asString(),
                title = it.title,
                body = it.body,
                parentFolderId = it.parentFolderId.takeIf { id -> id != FolderId.NONE }?.asString(),
                version = it.version
            )
        }
    }
}

private fun FolderContext.toTransportGetChildren(): IFolderResponse = FolderGetChildrenResponse(
    responseType = "getChildren",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors(),
    children = folderChildrenToTransport(folderChildrenResponse)
)

private fun FolderContext.toTransportDelete(): IFolderResponse = FolderDeleteResponse(
    responseType = "delete",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = responseResultFromState(this.state),
    errors = this.errors.toTransportErrors()
)
