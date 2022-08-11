package com.example.instaclonekotlin.manager.handler

import com.example.instaclonekotlin.model.User
import java.lang.Exception

interface DBUsersHandler {
    fun onSuccess(users: ArrayList<User>)
    fun onError(e: Exception)
}