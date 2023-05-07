package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.datetime.Clock
import org.avr.notes.api.v1.models.IFolderRequest
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.common.FolderContext
import org.avr.notes.common.helpers.asNotesError
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.logging.common.INotesLoggerWrapper
import org.avr.notes.mappers.fromRequestData
import org.avr.notes.mappers.toTransport


class FolderController(
    private val appSettings: NotesAppSettings,
    private val logger: INotesLoggerWrapper
) {
    suspend fun createFolder(call: ApplicationCall) {
        processFolderRequestV1(
            call,
            FolderCommand.CREATE_FOLDER
        )
    }

    suspend fun updateFolder(call: ApplicationCall) {
        processFolderRequestV1(
            call,
            FolderCommand.UPDATE_FOLDER
        )
    }

    suspend fun getFolderInfo(call: ApplicationCall) {
        processFolderRequestV1(
            call,
            FolderCommand.GET_FOLDER_INFO
        )
    }

    suspend fun deleteFolder(call: ApplicationCall) {
        processFolderRequestV1(
            call,
            FolderCommand.DELETE_FOLDER
        )
    }

    suspend fun getFolderChildren(call: ApplicationCall) {
        processFolderRequestV1(
            call,
            FolderCommand.GET_FOLDER_CHILDREN
        )
    }

    private suspend fun processFolderRequestV1(
        call: ApplicationCall,
        command: FolderCommand
    ) {
        val logId = command.name.toLowerCasePreservingASCIIRules()
        val ctx = FolderContext(
            processingStartTime = Clock.System.now(),
        )
        //val processor = appSettings.folderProcessor
        try {
            logger.doWithLogging(id = logId) {
                logger.info(
                    msg = "Got request with command: $command",
                    //data = ctx.toLog("${logId}-got")
                )

                val debugParameters = DebugHeaderHelper.getDebugParametersFromHeaders(call)
                val (requestParameters, requestBody) = receiveFolderRequest(command, call)
                ctx.fromRequestData(command, debugParameters, requestParameters, requestBody)

                //processor.exec(ctx)
                logger.info(
                    msg = "Request with $command command has been handled",
                    //data = ctx.toLog("${logId}-handled")
                )
                call.respond(ctx.toTransport())
            }
        } catch (e: Throwable) {
            logger.doWithLogging(id = "${logId}_failure") {
                command.also { ctx.command = it }
                logger.error(
                    msg = "$command handling failed",
                )
                ctx.state = NotesState.FAILING
                ctx.errors.add(e.asNotesError())
                //processor.exec(ctx)
                call.respond(ctx.toTransport())
            }
        }
    }

    private suspend fun receiveFolderRequest(command: FolderCommand, call: ApplicationCall): Pair<FolderRequestParameters, IFolderRequest?> {
        try {
            val requestReceiver = FolderRequestReceiverHolder.findFolderRequestReceiver(command)
            return requestReceiver.invoke(call)
        } catch (e: NoSuchElementException ) {
            throw IllegalStateException("Internal error: a handler for command \"$command\" not found")
        }
    }
}