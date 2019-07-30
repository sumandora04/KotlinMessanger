package com.notepoint4ugmail.kotlinmessanger.newMessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.notepoint4ugmail.kotlinmessanger.model.User
import timber.log.Timber

class NewMessageViewModel : ViewModel() {
    private var _userList = MutableLiveData<ArrayList<User?>>()
    val userList: LiveData<ArrayList<User?>>
        get() = _userList

    init {
//        fetchUserFromFirebaseDatabase()
        fetchUserListFromFirebaseDatabase()
    }

    private fun fetchUserListFromFirebaseDatabase(){
        val userList = ArrayList<User?>()
        val reference = FirebaseDatabase.getInstance().getReference("/users")
        reference.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                userList.add(user)
                _userList.value = userList
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

        })
    }


    private fun fetchUserFromFirebaseDatabase() {
        val reference = FirebaseDatabase.getInstance().getReference("/users")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<User?>()

                p0.children.forEach {
                    list.add(it.getValue(User::class.java))
                    Timber.d("ValueEventListener_in_loop: $p0")
                    _userList.value = list
                }
                Timber.d("ValueEventListener"+list.toString())

            }

            override fun onCancelled(p0: DatabaseError) {
                Timber.d("ValueEventListener: ${p0.message}")
            }
        })
    }
}