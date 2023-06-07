package org.avr.notes.app.v1

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import org.avr.notes.api.v1.apiV1Mapper
import org.avr.notes.api.v1.models.ObjectKind
import org.avr.notes.api.v1.models.ResponseResult
import org.avr.notes.api.v1.models.WsInitResponse
import org.avr.notes.api.v1.models.WsRequest
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.common.FolderContext
import org.avr.notes.common.NoteContext
import org.avr.notes.common.helpers.addError
import org.avr.notes.common.helpers.asNotesError
import org.avr.notes.logging.common.LogLevel
import org.avr.notes.mappers.InvalidObjectKindException
import org.avr.notes.mappers.fromTransport
import org.avr.notes.mappers.toTransport
import org.avr.notes.mappers.toTransportWsInit
import org.avr.notes.stub.FolderStub
import org.avr.notes.stub.NoteStub
import java.util.*

val sessions: MutableSet<WebSocketSession> = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())

suspend fun WebSocketSession.wsHandlerV1(appSettings: NotesAppSettings) {
    val logger = appSettings.corSettings.loggerProvider.logger("WsController")
    logger.log("WebSocket session created", LogLevel.TRACE)

    sessions.add(this)

    val initResponse = apiV1Mapper.writeValueAsString(WsInitResponse(ResponseResult.SUCCESS))
    outgoing.send(Frame.Text(initResponse))

    logger.trace("Initial response has been sent")

    incoming.receiveAsFlow().mapNotNull { it ->
        val inputFrame = it as? Frame.Text ?: return@mapNotNull

        logger.trace("Got request from client")

        try {
            val jsonData = inputFrame.readText()

            logger.debug("Request test of ${jsonData.length} chars length")

            val request = apiV1Mapper.readValue(jsonData, WsRequest::class.java)
            val result: String = when (request.objectKind) {
                ObjectKind.FOLDER -> handleFolderRequest(request)
                ObjectKind.NOTE -> handleNoteRequest(request)
                else -> throw InvalidObjectKindException(request.objectKind?.toString() ?: "<null>")
            }

            outgoing.send(Frame.Text(result))

            logger.trace("A response has been sent to a client")
        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (e: Throwable) {
            logger.error(msg = "An exception occurred while processing request on WebSocket", e = e)
        }
    }.collect()
}

fun handleNoteRequest(request: WsRequest): String {
    val context = NoteContext()
    try {
        context.apply { fromTransport(request) }
        context.noteResponse = NoteStub.get()
        return apiV1Mapper.writeValueAsString(context.toTransport())
    } catch (e: Exception) {
        context.addError(e.asNotesError())
        return apiV1Mapper.writeValueAsString(context.toTransportWsInit())
    }
}

fun handleFolderRequest(request: WsRequest): String {
    val context = FolderContext()
    try {
        context.apply { fromTransport(request) }
        context.folderResponse = FolderStub.getInfo()
        return apiV1Mapper.writeValueAsString(context.toTransport())
    } catch (e: Exception) {
        return apiV1Mapper.writeValueAsString(context.toTransportWsInit())
    }
}






