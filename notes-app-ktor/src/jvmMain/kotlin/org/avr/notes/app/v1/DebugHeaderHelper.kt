package org.avr.notes.app.v1

import io.ktor.server.application.*
import org.avr.notes.api.v1.DebugHeaders
import org.avr.notes.api.v1.RequestDebugParameters
import org.avr.notes.api.v1.models.RequestStubType
import org.avr.notes.api.v1.models.RequestWorkMode
import org.avr.notes.mappers.MissingRequiredRequestHeader

/**
 * Получение отладочных заголовков из запроса
 */
fun ApplicationCall.getDebugParametersFromHeaders(): RequestDebugParameters {
    val headers = this.request.headers
    val requestId = headers[DebugHeaders.HEADER_REQUEST_ID] ?: throw MissingRequiredRequestHeader(
        DebugHeaders.HEADER_REQUEST_ID
    )
    val workMode = headers[DebugHeaders.HEADER_WORK_MODE]?.let { RequestWorkMode.decode(it) } ?: RequestWorkMode.PROD
    val stubType = headers[DebugHeaders.HEADER_STUB_TYPE]?.let { RequestStubType.decode(it) } ?: RequestStubType.NONE
    return RequestDebugParameters(requestId, workMode, stubType)
}
