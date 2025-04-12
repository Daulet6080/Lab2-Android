package com.example.lab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatlibrary.ChatLibrary

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messagingModule = ChatLibrary()
        messagingModule.start(this)
    }
}
