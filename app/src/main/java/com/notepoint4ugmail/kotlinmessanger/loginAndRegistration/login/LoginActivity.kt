package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.notepoint4ugmail.kotlinmessanger.MainActivity
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            val email = user_email_edit_login.text.toString()
            val password = user_password_edit_login.text.toString()

            if (email.isEmpty() || password.isEmpty()) return@setOnClickListener

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this,"Welcome to Kotlin messenger",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Login failed: ${it.exception}",Toast.LENGTH_SHORT).show()
                }
            }
        }

        registration_text.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
