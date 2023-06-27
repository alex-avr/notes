package org.avr.notes.biz.workers.folder

import kotlinx.datetime.Instant
import org.avr.notes.common.FolderContext
import org.avr.notes.common.NONE
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.common.stubs.NotesStubs
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker
import org.avr.notes.stub.FolderStub

fun ICorChainDsl<FolderContext>.stubFolderCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == NotesStubs.SUCCESS && state == NotesState.RUNNING }
    handle {
        state = NotesState.FINISHING
        val stub = FolderStub.prepareResult {
            folderRequest.id.takeIf { it != FolderId.NONE }?.also { this.id = it }
            folderRequest.parentFolderId.takeIf { it != FolderId.NONE }?.also { this.parentFolderId = it }
            folderRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            folderRequest.createTime.takeIf { it != Instant.NONE }?.also { this.createTime = it }
            folderRequest.version.takeIf { it != 0 }?.also { this.version = it }
        }
        folderResponse = stub
    }
}
