package org.avr.notes.repo.stubs

import org.avr.notes.common.repo.IRepositoryFactory
import org.avr.notes.common.repo.folder.IFolderRepository
import org.avr.notes.common.repo.note.INoteRepository

class StubRepositoryFactory : IRepositoryFactory {
    override val folderRepository: IFolderRepository
        get() = StubFolderRepository()
    override val noteRepository: INoteRepository
        get() = StubNoteRepository()
}