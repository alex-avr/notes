package org.avr.notes.biz.general

//fun ICorChainDsl<FolderContext>.prepareResult(title: String) = worker {
//    this.title = title
//    description = "Подготовка данных для ответа клиенту на запрос"
//    on { workMode != NotesWorkMode.STUB }
//    handle {
//        folderResponse = adRepoDone
//        adsResponse = adsRepoDone
//        state = when (val st = state) {
//            NotesState.RUNNING -> NotesState.FINISHING
//            else -> st
//        }
//    }
//}