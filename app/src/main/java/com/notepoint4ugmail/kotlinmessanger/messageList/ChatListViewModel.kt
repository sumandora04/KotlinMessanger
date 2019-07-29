package com.notepoint4ugmail.kotlinmessanger.messageList

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ChatListViewModel:ViewModel() {

    fun onSignOut(){
        FirebaseAuth.getInstance().signOut()
    }
}