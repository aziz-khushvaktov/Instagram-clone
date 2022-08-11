package com.example.instaclonekotlin.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.FragmentUploadBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.DBManager
import com.example.instaclonekotlin.manager.StorageManager
import com.example.instaclonekotlin.manager.handler.DBPostHandler
import com.example.instaclonekotlin.manager.handler.DBUserHandler
import com.example.instaclonekotlin.manager.handler.StorageHandler
import com.example.instaclonekotlin.model.Post
import com.example.instaclonekotlin.model.User
import com.example.instaclonekotlin.utils.Utils
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * In UploadFragment, user can upload his/her post with image and caption
 */

class UploadFragment : BaseFragment() {

    private val binding by lazy { FragmentUploadBinding.inflate(layoutInflater) }
    private var pickedPhoto: Uri? = null
    private var allPhotos: ArrayList<Uri> = ArrayList()

    private var listener: UploadListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        initViews(binding.root)
        return binding.root
    }

    /**
     * onAttach is for communication of fragments
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if(context is UploadListener) {
            context
        }else {
            throw RuntimeException("$context must implement UploadListener")
        }
    }

    /**
     * onDetach is for communication of fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initViews(view: View) {
        binding.apply {
            setViewHeight(flView)

            ivClose.setOnClickListener { hidePickedPhoto() }

            ivPick.setOnClickListener { pickFishBunPhoto() }

            ivUpload.setOnClickListener { uploadNewPost() }
        }
    }

    /**
     * Pick photo using FishBun library
     */
    private fun pickFishBunPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private var photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            allPhotos = it.data!!.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            pickedPhoto = allPhotos[0]
            showPickedPhoto()
        }
    }

    private fun showPickedPhoto() {
        binding.apply {
            flPhoto.isVisible = true
            ivPhoto.setImageURI(pickedPhoto)
        }
    }
    private fun hidePickedPhoto() {
        pickedPhoto = null
        allPhotos.clear()
        binding.flPhoto.isVisible = false
    }

    private fun uploadNewPost() {
        val caption = binding.etCaption.text.toString().trim()
        if (caption.isNotEmpty() && pickedPhoto != null) {
            uploadPostPhoto(caption,pickedPhoto!!)
            
        }
    }

    private fun uploadPostPhoto(caption: String, uri: Uri) {
        showDialog(requireActivity())
        StorageManager.uploadPostPhoto(uri,object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                val post = Post(caption,imgUrl)
                post.currentDate = getCurrentTime()
                val uid = AuthManager.currentUser()!!.uid

                DBManager.loadUser(uid, object : DBUserHandler {
                    override fun onSuccess(user: User?) {
                        post.uid = uid
                        post.fullName = user!!.fullName
                        post.userImg = user.userImg
                        storePostToDb(post)
                    }

                    override fun onError(e: java.lang.Exception) {

                    }

                })
            }

            override fun onError(e: Exception) {

            }

        })
    }

    private fun storePostToDb(post: Post) {
        DBManager.storePosts(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                storeFeedToDb(post)
            }

            override fun onError(e: Exception) {

            }

        })
    }

    private fun storeFeedToDb(post: Post) {
        DBManager.storeFeeds(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                dismissDialog()
                resetAll()
                listener!!.scrollToHome()
            }

            override fun onError(e: Exception) {

            }

        })
    }

    private fun resetAll() {
        binding.apply {
            allPhotos.clear()
            pickedPhoto = null
            etCaption.text.clear()
            flPhoto.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        return sdf.format(Date())
    }

    /**
     * Set view height as screen width
     */
    private fun setViewHeight(view: FrameLayout) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(requireActivity().application).width
        view.layoutParams = params
    }

    /**
     * This interface is create for communications with HomeFragment
     */
    interface UploadListener {
        fun scrollToHome()
    }
}