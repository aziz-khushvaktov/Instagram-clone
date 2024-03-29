package com.example.instaclonekotlin.utils

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.model.ScreenSize

object Utils {


    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context): String {
        val device_id: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return device_id
    }

    fun screenSize(context: Application): ScreenSize {
        val displayMetrics = DisplayMetrics()
        val windowsManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels
        return ScreenSize(deviceWidth,deviceHeight)
    }

    fun dialogSingle(context: Context?, title: String?, callBack: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_single)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        d_title.text = title
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callBack.onCallBack(true)
        }
        dialog.show()
    }

    fun dialogDouble(context: Context?, title: String?, callBack: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_double)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        val d_cancel = dialog.findViewById<TextView>(R.id.d_cancel)
            d_title.text = title
        d_cancel.setOnClickListener {
            dialog.dismiss()
        }
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callBack.onCallBack(true)
        }
        dialog.show()
    }
}

interface DialogListener {
    fun onCallBack(isChosen: Boolean)
}