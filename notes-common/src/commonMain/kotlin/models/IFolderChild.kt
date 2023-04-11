package org.avr.notes.common.models

/**
 * Тип дочернего объекта для папки
 */
enum class FolderChildType {
    /**
     * Папка
     */
    FOLDER,

    /**
     * Заметка
     */
    NOTE
}

/**
 * Интерфейс дочернего объекта для папки
 */
sealed interface IFolderChild {
    /**
     * Тип дочернего объекта папки. Сейчас может быть либо папка, либо заметка
     */
    val folderChildType: FolderChildType
}