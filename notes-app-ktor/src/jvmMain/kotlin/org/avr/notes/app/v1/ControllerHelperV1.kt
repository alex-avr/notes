package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import org.avr.notes.api.v1.models.IFolderRequest
import org.avr.notes.api.v1.models.IFolderResponse
import org.avr.notes.api.v1.models.INoteRequest
import org.avr.notes.api.v1.models.INoteResponse
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.asNotesError
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.logging.common.INotesLoggerWrapper
import org.avr.notes.mappers.fromTransport
import org.avr.notes.mappers.toTransport

suspend inline fun <reified Q : IFolderRequest, @Suppress("unused") reified R : IFolderResponse> processFolderRequestV1(
    call: ApplicationCall,
    appSettings: NotesAppSettings,
    logger: INotesLoggerWrapper,
    logId: String,
    command: FolderCommand? = null,
    folderId: FolderId? = null
) {
    val ctx = FolderContext(
        processingStartTime = Clock.System.now(),
    )
    //val processor = appSettings.folderProcessor
    try {
        logger.doWithLogging(id = logId) {
            val request = call.receive<Q>()
            ctx.fromTransport(request, folderId)
            logger.info(
                msg = "$command got request",
                //data = ctx.toLog("${logId}-got")
            )
            //processor.exec(ctx)
            logger.info(
                msg = "$command request has been handled",
                //data = ctx.toLog("${logId}-handled")
            )
            call.respond(ctx.toTransport())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
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

suspend inline fun <reified Q : INoteRequest, @Suppress("unused") reified R : INoteResponse> processNoteRequestV1(
    call: ApplicationCall,
    appSettings: NotesAppSettings,
    logger: INotesLoggerWrapper,
    logId: String,
    command: NoteCommand? = null,
) {
    val ctx = NoteContext(
        processingStartTime = Clock.System.now(),
    )
    //val processor = appSettings.noteProcessor
    try {
        logger.doWithLogging(id = logId) {
            val request = call.receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command got request",
                //data = ctx.toLog("${logId}-got")
            )
            //processor.exec(ctx)
            logger.info(
                msg = "$command request has been handled",
                //data = ctx.toLog("${logId}-handled")
            )
            call.respond(ctx.toTransport())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
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