package com.notepoint4ugmail.kotlinmessanger.messageList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.notepoint4ugmail.kotlinmessanger.model.ChatMessage
import com.notepoint4ugmail.kotlinmessanger.model.User
import timber.log.Timber

class ChatListViewModel:ViewModel() {
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val dbReference = FirebaseDatabase.getInstance().getReference("/latest_messages/$currentUserId")

    private val _latestMessageList = MutableLiveData<ChatMessage>()
    val latestMessageList:LiveData<ChatMessage>
    get() = _latestMessageList

    private val _chatPartnerDetail = MutableLiveData<User>()
    val chatPartnerDetail:LiveData<User>
        get() = _chatPartnerDetail

    fun onSignOut(){
        FirebaseAuth.getInstance().signOut()
    }

    init {
        listenForLatestMessage()
    }


    private fun listenForLatestMessage(){
        val latestMsgHashMap:HashMap<String,ChatMessage> = HashMap()
        dbReference.addChildEventListener(object :ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage: ChatMessage? = p0.getValue(ChatMessage::class.java)?:return
                chatMessage?.let {
                    latestMsgHashMap[p0.key!!] = chatMessage
                    latestMsgHashMap.values.forEach {
                        _latestMessageList.value = it
                     getChatPartnerDetails(it)
                        Timber.d("onChildAdded: message: $it")
                    }
                    Timber.d("onChildAdded: ${latestMsgHashMap.values}")
                }
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                chatMessage?.let {
                    latestMsgHashMap[p0.key!!] = chatMessage
                    latestMsgHashMap.values.forEach {
                        _latestMessageList.value = it
                         getChatPartnerDetails(it)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })


        //initialiseLatestMessage(latestMsgHashMap)
    }

    fun getChatPartnerDetails(chatMessage: ChatMessage)/*:User?*/{
        var chatPartnerId:String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        }else{
            chatPartnerId = chatMessage.fromId
        }

        val userRef = FirebaseDatabase.getInstance().getReference("users/$chatPartnerId")
        var user:User? = null

        userRef.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java)
                _chatPartnerDetail.value =user
            }

            override fun onCancelled(p0: DatabaseError) {
                Timber.d("Error: ${p0.message}")
            }

        })

      //  return user
    }

}