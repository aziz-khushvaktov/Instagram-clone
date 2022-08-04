package com.example.instaclonekotlin.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    lateinit var context: Context
    private val TAG = BaseActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }

    fun callSignInActivity(context: Context) {
        val intent = Intent(context,SignInActivity::class.java)
        startActivity(intent)
    }

    fun callSignUpActivity(context: Context) {
        val intent = Intent(context,SignUpActivity::class.java)
        startActivity(intent)
    }

    fun callMainActivity(context: Context) {
        val intent = Intent(context,MainActivity::class.java)
        startActivity(intent)
    }
}