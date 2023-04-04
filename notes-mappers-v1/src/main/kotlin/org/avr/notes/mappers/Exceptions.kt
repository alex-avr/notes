package org.avr.notes.mappers

import org.avr.notes.common.models.NotesCommand

class UnknownRequestException(clazz: Class<*>) : RuntimeException("Class $clazz can't be mapped and not supported")
class UnknownCommandException(command: NotesCommand) : RuntimeException("Unknown command $command")