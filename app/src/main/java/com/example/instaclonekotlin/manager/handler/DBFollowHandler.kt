package com.example.instaclonekotlin.manager.handler

interface DBFollowHandler {
    fun onSuccess(isDone: Boolean)
    fun onError(e: Exception)
}