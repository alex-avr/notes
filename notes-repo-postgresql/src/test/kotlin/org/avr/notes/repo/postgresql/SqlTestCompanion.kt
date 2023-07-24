package org.avr.notes.repo.postgresql

import org.avr.notes.common.models.IFolderChild
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "notes-pass"
    private const val SCHEMA = "notes"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        initObjects: Collection<IFolderChild> = emptyList()
    ): RepoSql {
        return RepoSql(SqlProperties(url, USER, PASS, SCHEMA, dropDatabase = true),
            initObjects)
    }
}