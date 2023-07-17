package org.avr.notes.repo.stubs

import org.avr.notes.common.repo.folder.*
import org.avr.notes.stub.FolderStub

class StubFolderRepository : IFolderRepository {
    override suspend fun createFolder(request: DbFolderRequest): DbFolderResponse {
        return DbFolderResponse(
            data = FolderStub.create(),
            isSuccess = true
        )
    }

    override suspend fun getFolderInfo(request: DbFolderIdRequest): DbFolderResponse {
        return DbFolderResponse(
            data = FolderStub.getInfo(),
            isSuccess = true
        )
    }

    override suspend fun getFolderChildren(request: DbFolderIdRequest): DbFolderChildrenResponse {
        return DbFolderChildrenResponse(
            data = FolderStub.folderWithChildren(),
            isSuccess = true
        )
    }

    override suspend fun updateFolder(request: DbFolderRequest): DbFolderResponse {
        return DbFolderResponse(
            data = FolderStub.update(),
            isSuccess = true
        )
    }

    override suspend fun deleteFolder(request: DbFolderIdRequest): DbFolderResponse {
        return DbFolderResponse(
            data = FolderStub.getInfo(),
            isSuccess = true
        )
    }
}