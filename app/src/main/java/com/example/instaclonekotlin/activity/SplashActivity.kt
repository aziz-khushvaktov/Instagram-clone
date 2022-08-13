package com.example.instaclonekotlin.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.PrefsManager
import com.example.instaclonekotlin.utils.Logger
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


/**
 * In SplashActivity, user can visit to SignIn activity or MainActivity
 */

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val TAG = SplashActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        initViews()
    }

    private fun initViews() {
        loadFCMToken()
        object : CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                if (AuthManager.isSignedIn()) {
                    callMainActivity(context)
                } else {
                    callSignInActivity(context)
                }
            }
        }.start()
    }

    private fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logger.d(TAG, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            // Save it in locally to use later
            val token = task.result
            Logger.d("token", token.toString())
            PrefsManager(this).storeDeviceToken(token.toString())
        })
    }
}
