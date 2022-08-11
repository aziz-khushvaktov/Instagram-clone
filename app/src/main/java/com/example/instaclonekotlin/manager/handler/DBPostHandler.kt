package com.example.instaclonekotlin.manager.handler

import com.example.instaclonekotlin.model.Post

interface DBPostHandler {
    fun onSuccess(post: Post)
    fun onError(e: Exception)
}