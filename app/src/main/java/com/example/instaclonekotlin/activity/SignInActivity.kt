package com.example.instaclonekotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ActivitySignInBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.handler.AuthHandler
import com.example.instaclonekotlin.utils.Extensions.toast
import java.lang.Exception

/**
 * In SignInActivity, user can login using email and password
 */

class SignInActivity : BaseActivity() {

    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private val TAG = SignInActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            bSignIn.setOnClickListener {
                if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                    firebaseSignIn(etEmail.text.toString().trim(),etPassword.text.toString().trim())
                }
            }

            tvSignUp.setOnClickListener { callSignUpActivity(context)}
        }
    }

    private fun firebaseSignIn(email: String, password: String) {
        showDialog(this)
        AuthManager.signIn(email,password,object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissDialog()
                toast(getString(R.string.str_signIn_success))
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                toast(getString(R.string.str_signIn_failed))
                dismissDialog()
            }

        })
    }
}