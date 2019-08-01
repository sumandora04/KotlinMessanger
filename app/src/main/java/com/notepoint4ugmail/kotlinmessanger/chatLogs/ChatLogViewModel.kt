package com.notepoint4ugmail.kotlinmessanger.chatLogs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.notepoint4ugmail.kotlinmessanger.model.ChatMessage
import com.notepoint4ugmail.kotlinmessanger.model.User
import timber.log.Timber

class ChatLogViewModel(val userItem: User, application: Application) : AndroidViewModel(application) {

    private val _sendMessageSuccess = MutableLiveData<Boolean>()
    val sendMessageSuccess: LiveData<Boolean>
        get() = _sendMessageSuccess

    private val _messageList = MutableLiveData<ChatMessage>()
    val messageList: LiveData<ChatMessage>
        get() = _messageList

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser

    init {
        // Timber.d("SelectedUserDetail: $userItem")
        listenForMessages()
        _sendMessageSuccess.value = false
        fetchCurrentUserDetails()
    }


    fun performSendMessage(text: String) {
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val id = reference.key
        val fromId = FirebaseAuth.getInstance().uid
        val toId = userItem.uid
        val timeStamp = System.currentTimeMillis() / 1000

        if (id == null || fromId == null) return

        val chatMessage = ChatMessage(id, text, fromId, toId, timeStamp)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Timber.d("Saved to firebase db")
                _sendMessageSuccess.value = true
            }
            .addOnFailureListener {
                Timber.d("Failed to save: ${it.message}")
                _sendMessageSuccess.value = false
            }
    }

    fun resetStatus() {
        _sendMessageSuccess.value = false
    }


    fun listenForMessages() {
        val reference = FirebaseDatabase.getInstance().getReference("/messages")

        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(ChatMessage::class.java)
                Timber.d("Messages:$message")
                _messageList.value = message
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }


            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }


    fun fetchCurrentUserDetails() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                _currentUser.value = p0.getValue(User::class.java)

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }
}