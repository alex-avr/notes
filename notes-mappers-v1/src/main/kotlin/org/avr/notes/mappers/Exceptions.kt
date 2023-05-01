package org.avr.notes.mappers

import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.note.NoteCommand

class MissingRequestParameterException(message: String) : RuntimeException(message)
class UnknownRequestException(clazz: Class<*>) : RuntimeException("Class $clazz can't be mapped and not supported")
class UnknownNoteCommandException(command: NoteCommand) : RuntimeException("Unknown command for note: $command")
class UnknownFolderCommandException(command: FolderCommand) : RuntimeException("Unknown command for folder: $command")