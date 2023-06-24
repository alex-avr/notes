package org.avr.notes.biz.general

import org.avr.notes.common.FolderContext
import org.avr.notes.common.models.NotesState
import org.avr.notes.common.models.NotesWorkMode
import org.avr.notes.cor.ICorChainDsl
import org.avr.notes.cor.worker

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