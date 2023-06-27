package org.avr.notes.common.repo.folder

/**
 * Интерфейс репозитория для работы с папками
 */
interface IFolderRepository {
    suspend fun createFolder(request: DbFolderRequest): DbFolderResponse

    suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse

    suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse

    suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse

    suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse
}