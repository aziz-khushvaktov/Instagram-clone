package com.example.instaclonekotlin.manager.handler

interface StorageHandler {
    fun onSuccess(imgUrl: String)
    fun onError(e: Exception)
}