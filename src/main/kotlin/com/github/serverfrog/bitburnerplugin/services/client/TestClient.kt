package com.github.serverfrog.bitburnerplugin.services.client

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import io.ktor.websocket.Frame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

fun main() {
    SwingUtilities.invokeLater {
        createAndShowGUI()
    }
}

fun createAndShowGUI() {
    val frame = JFrame("WebSocket Client Example")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.layout = BorderLayout()

    val messageTextArea = JTextArea()
    frame.add(messageTextArea, BorderLayout.CENTER)

    val sendButton = JButton("Send")
    sendButton.addActionListener(SendButtonListener(messageTextArea))
    frame.add(sendButton, BorderLayout.SOUTH)

    frame.pack()
    frame.isVisible = true
}

class SendButtonListener(private val messageTextArea: JTextArea) : ActionListener {
    private lateinit var webSocketSession: DefaultClientWebSocketSession

    override fun actionPerformed(e: ActionEvent) {
        if (!::webSocketSession.isInitialized || !webSocketSession.isActive) {
            GlobalScope.launch(Dispatchers.IO) {
                val client = HttpClient {
                    install(WebSockets)
                }
                webSocketSession = client.webSocketSession(
                    method = HttpMethod.Get,
                    host = "localhost",
                    port = 9101,
                    path = "/"
                )
                    // Handle incoming messages
                for (frame in webSocketSession.incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val message = frame.readText()
                            updateMessageTextArea("Received: $message")
                        }
                        is Frame.Close -> {
                            val closeReason = frame.readReason()
                            updateMessageTextArea("Closed: $closeReason")
                        }

                        else -> {println("other stuff")}
                    }
                }

            }
        } else {
            val message = JOptionPane.showInputDialog("Enter a message:")
            if (message != null && message.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
                    webSocketSession.send(message)
                    updateMessageTextArea("Sent: $message")
                }
            }
        }
    }

    private fun updateMessageTextArea(message: String) {
        SwingUtilities.invokeLater {
            messageTextArea.append(message + "\n")
        }
    }
}