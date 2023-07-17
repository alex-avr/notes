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

    companion object {
        val NONE = object : IFolderRepository {
            override suspend fun createFolder(request: DbFolderRequest): DbFolderResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse {
                TODO("Not yet implemented")
            }

        }
    }
}