package com.github.serverfrog.bitburnerplugin.services.server


import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

class ApiServer {
    private lateinit var apiEngine: NettyApplicationEngine

    fun start() {
        apiEngine = embeddedServer(Netty, host = "localhost", port = 9101, module = Application::myApplicationModule).start(wait = true)
    }

    fun stop() {
        apiEngine.stop()
    }
}

fun main() {
    val server = ApiServer()
    server.start()

}

fun Application.myApplicationModule() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val connections = Collections.synchronizedSet<SocketConnection?>(LinkedHashSet())
        webSocket("/") {
            val connection = SocketConnection(this)
            connections += connection
            try {
                for(frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    println(receivedText)
                    connections.forEach {
                        if (it != connection) {
                            it.session.send(receivedText)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $connection!")
                connections -= connection
            }
        }
    }
}