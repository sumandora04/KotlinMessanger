package com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.notepoint4ugmail.kotlinmessanger.MainActivity
import com.notepoint4ugmail.kotlinmessanger.R
import com.notepoint4ugmail.kotlinmessanger.databinding.ActivityRegistrationBinding
import com.notepoint4ugmail.kotlinmessanger.loginAndRegistration.login.LoginActivity
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegistrationBinding
    private lateinit var regViewModel:RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        regViewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)

        binding.registerViewModel = regViewModel
        binding.lifecycleOwner = this

        regViewModel.regSuccess.observe(this, Observer {
            if (it){
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Welcome to Kotlin-Messenger ${regViewModel.regMessage}", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        regViewModel.regMessage.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        binding.registrationButton.setOnClickListener {
            val email = user_email_edit_login.text.toString()
            val password = user_password_edit_login.text.toString()
            val userName = user_name_edit_reg.text.toString()

            if (userName.isEmpty() || email.isEmpty() || password.length < 6) return@setOnClickListener

            regViewModel.registerUser(userName,email,password)
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        binding.selectPhotoRegistrationButton.setOnClickListener {
            selectPhoto()
        }

        regViewModel.imageUri.observe(this, Observer {
            it?.let {
                setImageToImageView(it)
            }
        })
    }

    private fun selectPhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpeg"
        startActivityForResult(intent,101)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==101 && resultCode==Activity.RESULT_OK && data!=null){
            val selectedPhotoUri = data.data

            selectedPhotoUri?.let {
                setImageToImageView(it)
            }
            regViewModel.imageUri.value = selectedPhotoUri
        }
    }

    private fun setImageToImageView(uri:Uri){
        binding.selectPhotoRegistrationImage.visibility = View.VISIBLE
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        binding.selectPhotoRegistrationImage.setImageBitmap(bitmap)
        binding.selectPhotoRegistrationButton.visibility = View.INVISIBLE
    }
}
