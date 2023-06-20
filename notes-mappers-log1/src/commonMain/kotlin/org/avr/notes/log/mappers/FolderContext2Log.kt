package org.avr.notes.log.mappers

import kotlinx.datetime.Clock
import org.avr.notes.api.logs.models.CommonLogModel
import org.avr.notes.api.logs.models.FolderChildInfo
import org.avr.notes.api.logs.models.FolderLog
import org.avr.notes.api.logs.models.FolderLogModel
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.IFolderChild
import org.avr.notes.common.models.Note
import org.avr.notes.common.models.folder.FolderCommand

fun FolderContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "notes",
    errors = errors.map { it.toLog() },
    objectKind = CommonLogModel.ObjectKind.FOLDER,
    folderData = toLog()
)

private fun FolderContext.toLog(): FolderLogModel = FolderLogModel(
    requestId = requestId.asString(),
    operation = commandToLogOperation(command),
    requestFolder = this.folderRequest.toLog(),
    responseFolder = this.folderResponse.toLog(),
    responseChildrenLog = makeFolderChildrenLog(this.folderChildrenResponse)
)

fun makeFolderChildrenLog(folderChildrenList: MutableList<IFolderChild>): List<FolderChildInfo> {
    return folderChildrenList.map { child ->
        when (child) {
            is Folder -> child.toChildInfoLog()
            is Note -> child.toChildInfoLog()
        }
    }
}

fun Folder.toChildInfoLog() = FolderChildInfo(
    id = id.asString(),
    parentFolderId = parentFolderId?.asString(),
    title = title,
    version = version.toString(),
    createTime = createTime.toString(),
    updateTime = updateTime.toString()
)

private fun commandToLogOperation(command: FolderCommand): FolderLogModel.Operation? =
    when (command) {
        FolderCommand.CREATE_FOLDER -> FolderLogModel.Operation.CREATE
        FolderCommand.UPDATE_FOLDER -> FolderLogModel.Operation.UPDATE
        FolderCommand.GET_FOLDER_INFO -> FolderLogModel.Operation.GET_INFO
        FolderCommand.GET_FOLDER_CHILDREN -> FolderLogModel.Operation.GET_CHILDREN
        FolderCommand.DELETE_FOLDER -> FolderLogModel.Operation.DELETE
        FolderCommand.NONE -> null
    }

private fun Folder.toLog() = FolderLog(
    id = id.asString(),
    parentFolderId = parentFolderId?.asString(),
    title = title,
    version = version.toString(),
    createTime = createTime.toString(),
    updateTime = updateTime.toString()
)
