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
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderIdCorrect(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = FolderId("123-234-abc-ABC"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderIdTrim(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = FolderId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(NotesState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderIdEmpty(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = FolderId(""),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationFolderIdFormat(command: FolderCommand, processor: FolderProcessor) = runTest {
    val ctx = FolderContext(
        command = command,
        state = NotesState.NONE,
        workMode = NotesWorkMode.TEST,
        folderRequest = Folder(
            id = FolderId("!@#\$%^&*(),.{}"),
            title = "abc"
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(NotesState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
