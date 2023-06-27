package org.avr.notes.biz.workers.note

import kotlinx.datetime.Instant
import org.avr.notes.common.NONE
import org.avr.notes.common.NoteContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.models.note.NoteId
import org.avr.notes.common.stubs.NotesStubs
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker
import org.avr.notes.stub.NoteStub

fun ICorChainDsl<NoteContext>.stubNoteGetSuccess(title: String) = worker {
    this.title = title
    on { stubCase == NotesStubs.SUCCESS && state == NotesState.RUNNING }
    handle {
        state = NotesState.FINISHING
        val stub = NoteStub.prepareResult {
            noteRequest.id.takeIf { it != NoteId.NONE }?.also { this.id = it }
            noteRequest.parentFolderId.takeIf { it != FolderId.NONE }?.also { this.parentFolderId = it }
            noteRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            noteRequest.body.takeIf { it.isNotBlank() }?.also { this.body = it }
            noteRequest.createTime.takeIf { it != Instant.NONE }?.also { this.createTime = it }
            noteRequest.version.takeIf { it != 0 }?.also { this.version = it }
        }
        noteResponse = stub
    }
}