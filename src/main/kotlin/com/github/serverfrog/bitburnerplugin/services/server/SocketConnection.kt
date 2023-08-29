package com.github.serverfrog.bitburnerplugin.services.server

import io.ktor.websocket.*
import java.util.*

data class SocketConnection (val session: DefaultWebSocketSession, val id: UUID = UUID.randomUUID())