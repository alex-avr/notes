package org.avr.notes.app

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.avr.notes.api.v1.apiV1Mapper
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.app.common.plugins.initAppSettings
import org.avr.notes.app.common.plugins.swagger
import org.avr.notes.app.v1.v1Folders
import org.avr.notes.app.v1.v1Notes
import org.avr.notes.logging.jvm.NotesLogWrapperLogback
import org.slf4j.event.Level

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"
@Suppress("unused") // Referenced in application.conf_
fun Application.moduleJvm(appSettings: NotesAppSettings = initAppSettings()) {
    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? NotesLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }
    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }
    install(DefaultHeaders)

    routing {
        route("v1") {
            v1Folders(appSettings)
            v1Notes(appSettings)
//            webSocket("/ws") {
//                wsHandlerV1(appSettings)
//            }
        }
        swagger(appSettings)
        static("static") {
            resources("static")
        }
    }
}