package com.example.instaclonekotlin.manager

import android.net.Uri
import com.example.instaclonekotlin.manager.handler.StorageHandler
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.rpc.context.AttributeContext

private var USER_PHOTO_PATH = "users"
private var POST_PHOTO_PATH = "posts"

object StorageManager {
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    fun uploadUserPhoto(uri: Uri, handler: StorageHandler) {
        val fileName = AuthManager.currentUser()!!.uid + "png"
        val uploadTask: UploadTask = storageReference.child(USER_PHOTO_PATH).child(fileName).putFile(uri)
        uploadTask.addOnSuccessListener { it ->
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                val imgUrl = it.toString()
                handler.onSuccess(imgUrl)

            }.addOnFailureListener {
                handler.onError(it)
            }
        }.addOnFailureListener { e ->
            handler.onError(e)
        }
    }

    fun uploadPostPhoto(uri: Uri, handler: StorageHandler) {
        val fileName = AuthManager.currentUser()!!.uid + System.currentTimeMillis().toString() + "png"
        val uploadTask: UploadTask = storageReference.child(POST_PHOTO_PATH).child(fileName).putFile(uri)
        uploadTask.addOnSuccessListener { it ->
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                val imgUrl = it.toString()
                handler.onSuccess(imgUrl)
            }.addOnFailureListener {
                handler.onError(it)
            }
        }.addOnFailureListener{e->
            handler.onError(e)
        }
    }

}