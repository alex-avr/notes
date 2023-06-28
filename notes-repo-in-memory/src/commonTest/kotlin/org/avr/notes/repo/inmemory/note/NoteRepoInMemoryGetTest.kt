package org.avr.notes.repo.inmemory.note

import org.avr.notes.common.repo.note.INoteRepository
import org.avr.notes.repo.inmemory.RepoInMemory
import org.avr.notes.repo.test.RepoNoteGetTest
import kotlin.time.Duration.Companion.minutes

class NoteRepoInMemoryGetTest : RepoNoteGetTest() {
    override val repo: INoteRepository
        get() = RepoInMemory(
            initObjects = initObjects,
            ttl = 2.minutes
        )
}