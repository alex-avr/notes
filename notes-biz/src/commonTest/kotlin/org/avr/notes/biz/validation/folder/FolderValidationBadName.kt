package org.avr.notes.biz.validation.folder

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.avr.notes.biz.FolderProcessor
import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.Folder
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.common.models.folder.FolderCommand
import org.avr.notes.common.models.folder.FolderId
import org.avr.notes.stub.FolderStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = FolderStub.getInfo()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderNameCorrect(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = stub.id,
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
    assertEquals("abc", ctx.folderValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderNameTrim(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = stub.id,
            title = " \n\t abc \t\n "
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
    assertEquals("abc", ctx.folderValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderNameEmpty(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = stub.id,
            title = ""
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderNameSymbols(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = FolderId("123"),
            title = "!@#$%^&*(),.{}"
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
