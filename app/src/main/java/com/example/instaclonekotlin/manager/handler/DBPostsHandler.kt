package com.example.instaclonekotlin.manager.handler

import com.example.instaclonekotlin.model.Post

interface DBPostsHandler {
    fun onSuccess(post: ArrayList<Post>)
    fun onError(e: Exception)
}