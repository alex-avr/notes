package org.avr.notes.repo.postgresql

open class SqlProperties (
    val url: String = "jdbc:postgresql://localhost:5432/notes",
    val user: String = "postgres",
    val password: String = "notes-pass",
    val schema: String = "notes",
    // Удалять таблицы при старте - нужно для тестирования
    val dropDatabase: Boolean = false,
)