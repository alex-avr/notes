package org.avr.notes.app.stub

import io.ktor.client.plugins.websocket.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import org.avr.notes.api.v1.apiV1Mapper
import org.avr.notes.api.v1.models.WsInitResponse
import org.avr.notes.app.moduleJvm
import kotlin.test.assertIs

inline fun <reified T> testMethod(
    request: Any,
    crossinline assertBlock: (T) -> Unit
) = testApplication {
    application {
        moduleJvm()
    }
    environment {
        config = MapApplicationConfig()
    }
    val client = createClient {
        install(WebSockets)
    }

    client.webSocket("/v1/ws") {
        withTimeout(30000) {
            val message = incoming.receive() as Frame.Text
            val response = apiV1Mapper.readValue(message.readText(), WsInitResponse::class.java)
            assertIs<WsInitResponse>(response)
        }
        send(Frame.Text(apiV1Mapper.writeValueAsString(request)))
        withTimeout(30000) {
            val message = incoming.receive() as Frame.Text
            val response = apiV1Mapper.readValue(message.readText(), T::class.java)

            assertBlock(response)
        }
    }
}