package org.avr.notes.mappers

import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.note.NoteCommand

class RequestValidationException(message: String) : RuntimeException(message)

class MissingRequiredRequestHeader(headerName: String) : RuntimeException("Missing required header \"${headerName}\"")
class MissingRequestParameterException(parameterName: String) : RuntimeException("Missing required header \"${parameterName}\"")
class UnknownRequestException(clazz: Class<*>) : RuntimeException("Class $clazz can't be mapped and not supported")
class InvalidObjectKindException(kind: String) : RuntimeException("Invalid object kind: $kind")
class UnknownNoteCommandException(command: NoteCommand) : RuntimeException("Unknown command for note: $command")
class UnknownFolderCommandException(command: FolderCommand) : RuntimeException("Unknown command for folder: $command")