package org.avr.notes.biz.workers.note

import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.stubs.NotesStubs
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker
import org.avr.notes.stub.NoteStub

fun ICorChainDsl<NoteContext>.stubNoteDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == NotesStubs.SUCCESS && state == NotesState.RUNNING }
    handle {
        state = NotesState.FINISHING
        val stub = NoteStub.prepareResult {
            noteRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        noteResponse = stub
    }
}