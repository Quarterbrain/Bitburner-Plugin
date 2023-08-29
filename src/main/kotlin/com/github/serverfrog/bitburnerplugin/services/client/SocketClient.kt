package com.github.serverfrog.bitburnerplugin.services.client

import com.github.serverfrog.bitburnerplugin.services.common.GetFileMessage
import com.github.serverfrog.bitburnerplugin.services.common.GetFileResult
import com.github.serverfrog.bitburnerplugin.services.common.RpcMessage
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicInteger

class SocketClient {

    private val messageId = AtomicInteger()

    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json {
                encodeDefaults = true
            })
        }
    }

    val session = runBlocking {
        client.webSocketSession(method = HttpMethod.Get, host = "127.0.0.1", port = 9101, path = "/")
    }

    fun getId() = messageId.incrementAndGet()

    suspend inline fun <reified T> send(msg: T ) : String? where T : RpcMessage{
        session.sendSerialized(msg)

        val frame = session.incoming.receive()
        if (frame !is Frame.Text) return null

        return frame.readText()
    }
}
fun main() {
    val client = SocketClient()
    runBlocking {
        //client.send(GetFileNamesMessage(id =client.getId()))

        val resp = client.send(GetFileMessage(id = client.getId(), params = GetFileMessage.Params(filename = "genericHack.js")))


        val someObject = Json.decodeFromString<GetFileResult>(resp!!)

        println(someObject)
//        println("whatever1")
//        client.test()
//        println("whatever2")
//        client.test()
//        println("whatever3")
//        client.test()
//        println("whatever4")
    }
}