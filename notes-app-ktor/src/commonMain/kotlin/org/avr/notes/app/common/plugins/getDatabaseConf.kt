package org.avr.notes.app.common.plugins

import io.ktor.server.application.*
import org.avr.notes.common.repo.IRepositoryFactory

enum class DbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}

expect fun Application.getDatabaseConf(type: DbType): IRepositoryFactory
