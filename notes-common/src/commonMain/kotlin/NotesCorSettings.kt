package org.avr.notes.common

import org.avr.notes.common.repo.IRepositoryFactory
import org.avr.notes.logging.common.NotesLoggerProvider

data class NotesCorSettings(
    val loggerProvider: NotesLoggerProvider = NotesLoggerProvider(),
    val repoStub: IRepositoryFactory = IRepositoryFactory.NONE,
    val repoTest: IRepositoryFactory = IRepositoryFactory.NONE,
    val repoProd: IRepositoryFactory = IRepositoryFactory.NONE,
) {
    companion object {
        val NONE = NotesCorSettings()
    }
}
