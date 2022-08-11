package com.example.instaclonekotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ActivitySignUpBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.DBManager
import com.example.instaclonekotlin.manager.handler.AuthHandler
import com.example.instaclonekotlin.manager.handler.DBUserHandler
import com.example.instaclonekotlin.model.User
import com.example.instaclonekotlin.utils.Extensions.toast
import java.lang.Exception

/**
 * In SignUpActivity, user can sign up with fullName, email and password
 */

class SignUpActivity : BaseActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            bSignUp.setOnClickListener {
                var fullName = etFullName.text.toString().trim()
                var email = etEmail.text.toString().trim()
                var password = etPassword.text.toString().trim()
                if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    val user = User(fullName, email,password,"")
                    firebaseSignUp(user)
                }
            }

            tvSignIn.setOnClickListener { finish() }
        }
    }

    private fun firebaseSignUp(user: User) {
        showDialog(this)
        AuthManager.signUp(user.email,user.password,object : AuthHandler {
            override fun onSuccess(uid: String) {
                user.uid = uid
                dismissDialog()
                // Save user to database
                toast(getString(R.string.str_signIn_success))
                storeUserToDB(user)
            }

            override fun onError(exception: Exception?) {
                toast(getString(R.string.str_signIn_failed))
                dismissDialog()
            }

        })
    }

    private fun storeUserToDB(user: User) {
        DBManager.storeUser(user, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                dismissDialog()
                callMainActivity(context)
            }

            override fun onError(e: Exception) {

            }

        })
    }
}