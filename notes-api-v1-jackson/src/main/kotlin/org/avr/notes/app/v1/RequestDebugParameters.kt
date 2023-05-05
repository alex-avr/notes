package org.avr.notes.api.v1.org.avr.notes.app.v1

import org.avr.notes.api.v1.models.RequestStubType
import org.avr.notes.api.v1.models.RequestWorkMode

data class RequestDebugParameters(
    val requestId: String,
    val workMode: RequestWorkMode,
    val stubType: RequestStubType
)