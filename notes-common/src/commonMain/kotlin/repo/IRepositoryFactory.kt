package org.avr.notes.common.repo

import org.avr.notes.common.repo.folder.IFolderRepository
import org.avr.notes.common.repo.note.INoteRepository

/**
 * Точка входа для получения конкретных репозиториев
 */
interface IRepositoryFactory {
    val folderRepository: IFolderRepository
    val noteRepository: INoteRepository

    companion object {
        val NONE = object : IRepositoryFactory {
            override val folderRepository: IFolderRepository
                get() = TODO("Not yet implemented")
            override val noteRepository: INoteRepository
                get() = TODO("Not yet implemented")

        }
    }
}