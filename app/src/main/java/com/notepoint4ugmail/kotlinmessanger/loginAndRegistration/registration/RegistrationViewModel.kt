package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class RegistrationViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _regSuccess = MutableLiveData<Boolean>()
    val regSuccess: LiveData<Boolean>
        get() = _regSuccess

    private val _regMessage = MutableLiveData<String>()
    val regMessage:LiveData<String>
    get() = _regMessage

    fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _regSuccess.value = true
                    _regMessage.value = it.result.toString()
                    Timber.d("Registration success with Uid: ${it.result?.user?.uid}")
                    //On successful registration upload the image here
                    uploadProfileImageToFirebaseStorage()
                }
            }
            .addOnFailureListener {
                Timber.d("Registration failed: ${it.message}")
                _regSuccess.value = false
                _regMessage.value = it.message
            }

    }


    fun uploadProfileImageToFirebaseStorage(){

    }


}