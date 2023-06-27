package org.avr.notes.repo.inmemory.model

sealed interface IEntity {
    val id: String
    val parentFolderId: String?
}