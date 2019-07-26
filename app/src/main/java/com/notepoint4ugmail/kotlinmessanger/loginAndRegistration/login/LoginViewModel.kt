package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class LoginViewModel : ViewModel() {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private var _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn:LiveData<Boolean>
    get() = _isLoggedIn

    private val _loginMessage = MutableLiveData<String>()
    val loginMessage:LiveData<String>
        get() = _loginMessage

    init {
        checkLoginStatus()
    }

    fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("Login successful with UID: ${it.result?.user?.uid}")
                    _loginSuccess.value = true
                    _loginMessage.value = it.result?.user?.uid
                }
            }
            .addOnFailureListener{
                _loginSuccess.value = false
                _loginMessage.value = it.message
                Timber.d("Login failed: ${it.message}")
            }
    }

    private fun checkLoginStatus() {
        _isLoggedIn.value = firebaseAuth.currentUser != null
    }

}