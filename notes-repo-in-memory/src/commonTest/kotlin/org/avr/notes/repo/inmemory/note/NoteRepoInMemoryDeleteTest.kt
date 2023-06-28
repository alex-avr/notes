package org.avr.notes.repo.inmemory.note

import org.avr.notes.common.repo.note.INoteRepository
import org.avr.notes.repo.inmemory.RepoInMemory
import org.avr.notes.repo.test.RepoNoteDeleteTest
import kotlin.time.Duration.Companion.minutes

class NoteRepoInMemoryDeleteTest : RepoNoteDeleteTest() {
    override val repo: INoteRepository
        get() = RepoInMemory(
            initObjects = initObjects,
            ttl = 2.minutes
        )
}