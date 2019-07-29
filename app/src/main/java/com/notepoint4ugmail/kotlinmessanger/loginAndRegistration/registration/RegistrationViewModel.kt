package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import java.util.*

class RegistrationViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _regSuccess = MutableLiveData<Boolean>()
    val regSuccess: LiveData<Boolean>
        get() = _regSuccess

    private val _regMessage = MutableLiveData<String>()
    val regMessage: LiveData<String>
        get() = _regMessage

    val imageUri = MutableLiveData<Uri>()

    fun registerUser(name:String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _regMessage.value = it.result?.user?.uid.toString()
                    Timber.d("Registration success with Uid: ${it.result?.user?.uid}")
                    //On successful registration upload the image here
                    uploadProfileImageToFirebaseStorage(name,email)
                }
            }
            .addOnFailureListener {
                Timber.d("Registration failed: ${it.message}")
                _regMessage.value = it.message
            }

    }


    private fun uploadProfileImageToFirebaseStorage(name:String, email: String) {
        val fileName = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance().getReference("/images/$fileName")

        //putFile() which takes the Uri as input parameter
        val uri = imageUri.value
        uri?.let {
            reference.putFile(it)
                .addOnSuccessListener {
                    Timber.d("Successfully uploaded: ${it.metadata?.path}")
                    //Get the file stored location:
                    reference.downloadUrl.addOnSuccessListener {
                        it?.let {
                            Timber.d("File path: $it")
                            saveUserToFirebaseDatabase(name,email, it.toString())
                        }
                    }
                }
                .addOnFailureListener {
                    Timber.d("Upload failed: ${it.message}")
                }
        }
    }


    private fun saveUserToFirebaseDatabase(name:String, email: String, imageUrl: String){
        val uId = firebaseAuth.uid
        uId?.let {
            val databaseReference = FirebaseDatabase.getInstance().getReference("/users/$uId")
            val user = User(uId,name,email,imageUrl)
            databaseReference.setValue(user)
                .addOnCompleteListener {
                    Timber.d("Saved to database")
                    _regSuccess.value = true
                }
                .addOnFailureListener {
                    Timber.d("Failed to Saved to database : ${it.message}")
                    _regSuccess.value = false
                }
        }

    }



}