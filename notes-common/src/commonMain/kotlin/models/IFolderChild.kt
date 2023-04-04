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
interface IFolderChild {
    /**
     * Тип дочернего объекта папки. Сейчас может быть либо папка, либо заметка
     */
    val folderChildType: FolderChildType

    /**
     * Cсылка на родительский объект (папку). Возвращает null, если родтеля нет (т.е. это папка верхнего уровня)
     * @return
     */
    val parent: IFolderChild?
}