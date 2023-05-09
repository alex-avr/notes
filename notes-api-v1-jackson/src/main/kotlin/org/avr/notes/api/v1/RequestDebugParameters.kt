package org.avr.notes.api.v1

import org.avr.notes.api.v1.models.RequestStubType
import org.avr.notes.api.v1.models.RequestWorkMode

/**
 * Информация о режиме обработки запроса
 */
data class RequestDebugParameters(
    /**
     * Идентификатор запроса
     */
    val requestId: String,

    /**
     * Режим обработки запроса - producation или заглушка
     */
    val workMode: RequestWorkMode,

    /**
     * Тип ответа-заглушки, который должен быть возвращен в качестве ответа
     */
    val stubType: RequestStubType
)