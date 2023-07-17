package org.avr.notes.app.common.config

import io.ktor.server.config.*

data class PostgresConfig(
    val url: String = "jdbc:postgresql://localhost:5432/notes",
    val user: String = "postgres",
    val password: String = "notes-pass",
    val schema: String = "notes"
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "${ConfigPaths.repository}.psql"
    }
}
