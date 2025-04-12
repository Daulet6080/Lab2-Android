package com.example.chatlibrary

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var socketConnection: ChatWebSocket
    private lateinit var chatAdapter: ChatMessageAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private val chatHistory = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView = findViewById(R.id.recyclerView)
        val inputField = findViewById<EditText>(R.id.editTextMessage)
        val sendIcon = findViewById<ImageButton>(R.id.buttonSend)

        chatAdapter = ChatMessageAdapter(chatHistory)
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        socketConnection = ChatWebSocket { incomingMessage ->
            runOnUiThread {
                chatHistory.add(incomingMessage)
                chatAdapter.notifyItemInserted(chatHistory.size - 1)
                chatRecyclerView.scrollToPosition(chatHistory.size - 1)
            }
        }

        socketConnection.connect()

        sendIcon.setOnClickListener {
            val messageContent = inputField.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                val userMessage = ChatMessage(messageContent, true)
                chatHistory.add(userMessage)
                chatAdapter.notifyItemInserted(chatHistory.size - 1)
                chatRecyclerView.scrollToPosition(chatHistory.size - 1)

                socketConnection.sendMessage(messageContent)
                inputField.text.clear()
            }
        }
    }

    private fun insertMessage(msg: ChatMessage) {
        chatHistory.add(msg)
        chatAdapter.notifyItemInserted(chatHistory.size - 1)
        chatRecyclerView.scrollToPosition(chatHistory.size - 1)
    }

    override fun onDestroy() {
        socketConnection.close()
        super.onDestroy()
    }
}
