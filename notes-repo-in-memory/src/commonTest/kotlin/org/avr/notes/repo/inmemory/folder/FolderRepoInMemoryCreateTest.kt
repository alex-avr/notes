package org.avr.notes.repo.inmemory.folder

import org.avr.notes.common.repo.folder.IFolderRepository
import org.avr.notes.repo.inmemory.RepoInMemory
import org.avr.notes.repo.test.RepoFolderCreateTest
import kotlin.time.Duration.Companion.minutes

class FolderRepoInMemoryCreateTest : RepoFolderCreateTest() {
    override val repo: IFolderRepository
        get() = RepoInMemory(
            initObjects = initObjects,
            ttl = 2.minutes
        )
}