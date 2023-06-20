package org.avr.notes.biz.workers.folder

import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.stubs.NotesStubs
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker
import org.avr.notes.stub.FolderStub

fun ICorChainDsl<FolderContext>.stubFolderGetChildrenSuccess(title: String) = worker {
    this.title = title
    on { stubCase == NotesStubs.SUCCESS && state == NotesState.RUNNING }
    handle {
        state = NotesState.FINISHING
        folderChildrenResponse.addAll(FolderStub.folderWithChildren())
    }
}