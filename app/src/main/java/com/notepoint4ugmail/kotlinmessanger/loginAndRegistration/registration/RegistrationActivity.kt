package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.login.LoginActivity
import kotlinx.android.synthetic.main.activity_registration.*
import timber.log.Timber

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //Initialise the firebase auth:
        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            val email = user_email_edit_login.text.toString()
            val password = user_password_edit_login.text.toString()
            val userName = user_name_edit_reg.text.toString()

            if (email.isEmpty() || password.length < 6) return@setOnClickListener

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("User created successfully!!!")
                    val user = auth.currentUser
                    Timber.d("Current user: ${user?.uid}")
                    Timber.d("Current user UID: ${it.result?.user?.uid}")
                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Timber.d("User creation failed.")
                    Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registration_text.setOnClickListener {
            Timber.d("Go to login activity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
