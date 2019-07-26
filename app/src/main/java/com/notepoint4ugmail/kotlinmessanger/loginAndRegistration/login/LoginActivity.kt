package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.notepoint4ugmail.kotlinmessanger.MainActivity
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.databinding.ActivityLoginBinding
import com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration.RegistrationActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        loginViewModel.loginSuccess.observe(this, Observer {
            if (it){
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Welcome to Kotlin-Messenger ${loginViewModel.loginMessage}",Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        loginViewModel.isLoggedIn.observe(this, Observer {
            if (it){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        binding.loginButton.setOnClickListener {
            val email = binding.userEmailEditLogin.text.toString()
            val password = binding.userPasswordEditLogin.text.toString()

            if (email.isEmpty() || password.isEmpty()) return@setOnClickListener

            loginViewModel.loginUser(email,password)
        }

        loginViewModel.loginMessage.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        binding.registrationText.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
            finish()
        }
    }

}
