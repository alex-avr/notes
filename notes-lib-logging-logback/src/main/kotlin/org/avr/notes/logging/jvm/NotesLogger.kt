package org.avr.notes.logging.jvm

import ch.qos.logback.classic.Logger
import org.avr.notes.logging.common.INotesLoggerWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal NotesLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun notesLoggerLogback(logger: Logger): INotesLoggerWrapper = NotesLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun notesLoggerLogback(clazz: KClass<*>): INotesLoggerWrapper = notesLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun notesLoggerLogback(loggerId: String): INotesLoggerWrapper = notesLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)