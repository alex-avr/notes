package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.datetime.Clock
import org.avr.notes.api.v1.NoteRequestParameters
import org.avr.notes.api.v1.models.INoteRequest
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.asNotesError
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.log.mappers.toLog
import org.avr.notes.logging.common.INotesLoggerWrapper
import org.avr.notes.mappers.fromRequestData
import org.avr.notes.mappers.toTransport
import org.avr.notes.stub.NoteStub

/**
 * Контроллер для запросов, касающихся заметок
 */
class NoteController(
    private val appSettings: NotesAppSettings,
    private val logger: INotesLoggerWrapper
) {
    suspend fun createNote(call: ApplicationCall) = processNoteRequestV1(
        call,
        NoteCommand.CREATE_NOTE
    )

    suspend fun updateNote(call: ApplicationCall) = processNoteRequestV1(
        call,
        NoteCommand.UPDATE_NOTE
    )

    suspend fun readNote(call: ApplicationCall) = processNoteRequestV1(
        call,
        NoteCommand.READ_NOTE
    )

    suspend fun deleteNote(call: ApplicationCall) = processNoteRequestV1(
        call,
        NoteCommand.DELETE_NOTE
    )

    suspend fun searchNotes(call: ApplicationCall) = processNoteRequestV1(
        call,
        NoteCommand.SEARCH_NOTES
    )

    private suspend fun processNoteRequestV1(
        call: ApplicationCall,
        command: NoteCommand
    ) {
        val logId = command.name.toLowerCasePreservingASCIIRules()
        val ctx = NoteContext(
            processingStartTime = Clock.System.now(),
        )
        val processor = appSettings.noteProcessor
        try {
            logger.doWithLogging(id = logId) {
                logger.info(
                    msg = "Got request with command: $command",
                    data = ctx.toLog("${logId}-got")
                )

                val debugParameters = call.getDebugParametersFromHeaders()
                val (requestParameters, requestBody) = receiveNoteRequest(command, call)
                ctx.fromRequestData(command, debugParameters, requestParameters, requestBody)

                // todo: подключить обработчики Chain of responsibilities
                // todo: обработка разных типолв заглушек
                processor.exec(ctx)
                ctx.noteResponse = NoteStub.get()

                logger.info(
                    msg = "Request with $command command has been handled",
                    data = ctx.toLog("${logId}-handled")
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
                processor.exec(ctx)
                call.respond(ctx.toTransport())
            }
        }
    }
    private suspend fun receiveNoteRequest(command: NoteCommand, call: ApplicationCall): Pair<NoteRequestParameters, INoteRequest?> {
        try {
            val requestReceiver = NoteRequestReceiverHolder.findNoteRequestReceiver(command)
            return requestReceiver.invoke(call)
        } catch (e: NoSuchElementException ) {
            throw IllegalStateException("Internal error: a handler for command \"$command\" not found")
        }
    }
}

