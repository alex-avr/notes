package org.avr.notes.app.common.plugins

import io.ktor.server.application.*
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.biz.FolderProcessor
import org.avr.notes.biz.NoteProcessor
import org.avr.notes.common.NotesCorSettings
import org.avr.notes.repo.stubs.StubRepositoryFactory

fun Application.initAppSettings(): NotesAppSettings {
    val corSettings = NotesCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(DbType.TEST),
        repoProd = getDatabaseConf(DbType.PROD),
        repoStub = StubRepositoryFactory(),
    )
    return NotesAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        folderProcessor = FolderProcessor(settings = corSettings),
        noteProcessor = NoteProcessor(settings = corSettings),
    )
}

