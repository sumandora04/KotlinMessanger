package com.notepoint4ugmail.kotlinmessanger.chatLogs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notepoint4ugmail.kotlinmessanger.model.User

class ChatLogFactory(
    private val user: User,
    private val application: Application
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatLogViewModel::class.java)) {
            return ChatLogViewModel(user, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Exception")
    }

}