package org.avr.notes.app.common.plugins

import io.ktor.server.application.*
import org.avr.notes.app.common.config.ConfigPaths
import org.avr.notes.app.common.config.PostgresConfig
import org.avr.notes.common.repo.IRepositoryFactory
import org.avr.notes.repo.inmemory.RepoInMemory
import org.avr.notes.repo.postgresql.RepoSql
import org.avr.notes.repo.postgresql.SqlProperties
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

actual fun Application.getDatabaseConf(type: DbType): IRepositoryFactory {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres', 'cassandra', 'gremlin'"
        )
    }
}

private fun Application.initInMemory(): IRepositoryFactory {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return RepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

private fun Application.initPostgres(): IRepositoryFactory {
    val config = PostgresConfig(environment.config)
    return RepoSql(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}