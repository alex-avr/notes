package org.avr.notes.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import org.avr.notes.api.v1.models.INoteRequest
import org.avr.notes.api.v1.models.NoteCreateRequest
import org.avr.notes.api.v1.models.NoteUpdateRequest
import org.avr.notes.common.models.note.NoteCommand
import org.avr.notes.mappers.MissingRequestParameterException

typealias NoteRequestReceiver = suspend (call: ApplicationCall) -> Pair<NoteRequestParameters, INoteRequest?>

/**
 * Класс, который занимается хранением и поиском обработчиков-разборщиком входящих запросов для заметок
 */
object NoteRequestReceiverHolder {
    private const val REQUEST_PARAM_NOTE_ID = "noteId"
    private const val REQUEST_PARAM_NOTE_SEARCH_STRING = "searchString"

    private val noteRequestTagToTypeMap = mapOf<NoteCommand, NoteRequestReceiver>(
        NoteCommand.CREATE_NOTE     to NoteRequestReceiverHolder::noteCreateRequestReceiver,
        NoteCommand.READ_NOTE       to NoteRequestReceiverHolder::noteGetRequestReceiver,
        NoteCommand.UPDATE_NOTE     to NoteRequestReceiverHolder::noteUpdateRequestReceiver,
        NoteCommand.DELETE_NOTE     to NoteRequestReceiverHolder::noteDeleteRequestReceiver,
        NoteCommand.SEARCH_NOTES    to NoteRequestReceiverHolder::noteSearchRequestReceiver
    )

    fun findNoteRequestReceiver(command: NoteCommand): NoteRequestReceiver = noteRequestTagToTypeMap.getValue(command)

    private suspend fun noteCreateRequestReceiver(call: ApplicationCall): Pair<NoteRequestParameters, INoteRequest?> {
        val requestBody = call.receive<NoteCreateRequest>()
        return Pair(NoteRequestParameters(), requestBody)
    }

    private suspend fun noteGetRequestReceiver(call: ApplicationCall): Pair<NoteRequestParameters, INoteRequest?> {
        val noteId = call.parameters[REQUEST_PARAM_NOTE_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_NOTE_ID
        )
        val noteRequestParameters = NoteRequestParameters(noteId)
        return Pair(noteRequestParameters, null)
    }

    private suspend fun noteUpdateRequestReceiver(call: ApplicationCall): Pair<NoteRequestParameters, INoteRequest?> {
        val noteId = call.parameters[REQUEST_PARAM_NOTE_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_NOTE_ID
        )
        val requestBody = call.receive<NoteUpdateRequest>()
        val noteRequestParameters = NoteRequestParameters(noteId)
        return Pair(noteRequestParameters, requestBody)
    }

    private suspend fun noteDeleteRequestReceiver(call: ApplicationCall): Pair<NoteRequestParameters, INoteRequest?> {
        val noteId = call.parameters[REQUEST_PARAM_NOTE_ID] ?: throw MissingRequestParameterException(
            REQUEST_PARAM_NOTE_ID
        )
        val noteRequestParameters = NoteRequestParameters(noteId)
        return Pair(noteRequestParameters, null)
    }

    private suspend fun noteSearchRequestReceiver(call: ApplicationCall): Pair<NoteRequestParameters, INoteRequest?> {
        val noteSearchString = call.parameters[REQUEST_PARAM_NOTE_SEARCH_STRING] ?: throw MissingRequestParameterException(REQUEST_PARAM_NOTE_SEARCH_STRING)
        val noteRequestParameters = NoteRequestParameters(noteSearchFilter = noteSearchString)
        return Pair(noteRequestParameters, null)
    }
}