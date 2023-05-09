package org.avr.notes.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class NotesLoggerProvider(
    private val provider: (String) -> INotesLoggerWrapper = { INotesLoggerWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}