package org.avr.notes.app

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.avr.notes.app.common.NotesAppSettings
import org.avr.notes.app.common.plugins.initAppSettings
import org.avr.notes.app.common.plugins.swagger
import org.avr.notes.app.v1.apiV1Mapper
import org.avr.notes.app.v1.v1Folders
import org.avr.notes.app.v1.v1Notes
import org.avr.notes.logging.jvm.NotesLogWrapperLogback
import org.slf4j.event.Level

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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

    install(CORS) {
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("X-Request-Id")
        allowHeader("X-Work-Mode")
        allowHeader("X-Stub-Type")
        exposeHeader("X-Request-Id")
        exposeHeader("X-Work-Mode")
        exposeHeader("X-Stub-Type")

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
    }

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
