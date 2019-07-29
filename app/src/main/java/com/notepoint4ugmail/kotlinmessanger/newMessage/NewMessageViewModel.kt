package com.notepoint4ugmail.kotlinmessanger.newMessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration.User
import timber.log.Timber

class NewMessageViewModel : ViewModel() {
    private var _userList = MutableLiveData<ArrayList<User?>>()
    val userList: LiveData<ArrayList<User?>>
        get() = _userList

    init {
        fetchUserFromFirebaseDatabase()
    }

    private fun fetchUserFromFirebaseDatabase() {
        val reference = FirebaseDatabase.getInstance().getReference("/users")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<User?>()

                p0.children.forEach {
                    list.add(p0.getValue(User::class.java))
                }
                _userList.value = list
            }

            override fun onCancelled(p0: DatabaseError) {
                Timber.d(p0.message)
            }
        })
    }
}