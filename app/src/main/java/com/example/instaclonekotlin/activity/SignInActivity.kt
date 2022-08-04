package com.example.instaclonekotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ActivitySignInBinding

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
            bSignIn.setOnClickListener { callMainActivity(context) }

            tvSignUp.setOnClickListener { callSignUpActivity(context)}
        }
    }
}