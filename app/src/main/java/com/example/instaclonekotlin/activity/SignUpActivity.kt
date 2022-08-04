package com.example.instaclonekotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ActivitySignUpBinding

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

    }
}