package com.example.instaclonekotlin.manager.handler

import com.example.instaclonekotlin.model.User
import java.lang.Exception

interface DBUserHandler {
    fun onSuccess(user: User? = null)
    fun onError(e: Exception)
}