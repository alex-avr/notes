package org.avr.notes.repo.postgresql

import org.avr.notes.common.repo.folder.IFolderRepository
import org.avr.notes.repo.test.RepoFolderCreateTest

class RepoSqlFolderCreateTest : RepoFolderCreateTest() {
    override val repo: IFolderRepository
        get() = SqlTestCompanion.repoUnderTestContainer().folderRepository
}