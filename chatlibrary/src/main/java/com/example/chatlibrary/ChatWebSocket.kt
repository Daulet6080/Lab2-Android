package com.example.chatlibrary



import okhttp3.*
import okio.ByteString

class ChatWebSocket(private val onMessageReceived: (ChatMessage) -> Unit) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .url("wss://echo.websocket.org")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {

            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessageReceived(ChatMessage(text, false))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {

                if (bytes.utf8() == "\u00cb") {
                    onMessageReceived(ChatMessage("📩 Получено специальное сообщение от сервера!", false))
                } else {
                    onMessageReceived(ChatMessage("Получены байты: ${bytes.hex()}", false))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onMessageReceived(ChatMessage("❌ Ошибка соединения: ${t.message}", false))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "User closed the chat")
        webSocket = null
    }
}
