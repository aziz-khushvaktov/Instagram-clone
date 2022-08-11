package com.example.instaclonekotlin.fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.activity.BaseActivity
import com.example.instaclonekotlin.activity.MainActivity
import com.example.instaclonekotlin.adapter.ProfileAdapter
import com.example.instaclonekotlin.databinding.FragmentProfileBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.DBManager
import com.example.instaclonekotlin.manager.StorageManager
import com.example.instaclonekotlin.manager.handler.DBPostsHandler
import com.example.instaclonekotlin.manager.handler.DBUserHandler
import com.example.instaclonekotlin.manager.handler.DBUsersHandler
import com.example.instaclonekotlin.manager.handler.StorageHandler
import com.example.instaclonekotlin.model.Post
import com.example.instaclonekotlin.model.User
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

/**
 * In ProfileFragment, user can check his/her posts and counts and can change profile photo)
 */

class ProfileFragment : BaseFragment() {

    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    var pickedPhoto: Uri? = null
    var allPhotos: ArrayList<Uri> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        initViews(binding.root)
        return binding.root
    }

    private fun initViews(view: View) {
        loadMyPosts()
        loadUserInfo()
        loadMyFollowers()
        loadMyFollowing()

        binding.apply {
            rvProfile.layoutManager = GridLayoutManager(context, 2)

            ivProfile.setOnClickListener { pickFishButPhoto() }

            ivLogOut.setOnClickListener {
                AuthManager.signOut()
                callSignInActivity(requireActivity())
            }
        }
    }

    private fun loadMyFollowing() {
        val uid = AuthManager.currentUser()!!.uid
        DBManager.loadFollowing(uid,object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                binding.tvFollowing.text = users.size.toString()
            }

            override fun onError(e: java.lang.Exception) {

            }

        })
    }

    private fun loadMyFollowers() {
        val uid = AuthManager.currentUser()!!.uid
        DBManager.loadFollowers(uid,object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                binding.tvFollowers.text = users.size.toString()
            }

            override fun onError(e: java.lang.Exception) {

            }

        })
    }

    private fun loadMyPosts() {
        val uid = AuthManager.currentUser()!!.uid
        DBManager.loadPosts(uid, object : DBPostsHandler{
            override fun onSuccess(post: ArrayList<Post>) {
                binding.apply {
                    tvPosts.text = post.size.toString()
                }
                refreshAdapter(post)
            }

            override fun onError(e: Exception) {

            }

        })
    }

    private fun loadUserInfo() {
        showDialog(requireActivity())
        DBManager.loadUser(AuthManager.currentUser()!!.uid, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                if (user != null) {
                    showUserInfo(user)
                }
            }

            override fun onError(e: Exception) {
                dismissDialog()
            }

        })
    }

    private fun showUserInfo(user: User) {
        dismissDialog()
        binding.apply {
            tvFullName.text = user.fullName
            tvEmail.text = user.email
            Glide.with(this@ProfileFragment).load(user.userImg)
                .error(R.drawable.img_default_user)
                .into(ivProfile)
        }
    }

    /**
     * Picked Photo using FishBun library
     */

    private fun pickFishButPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMinCount(1)
            .setMaxCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                uploadUserPhoto()
            }
        }

    private fun uploadUserPhoto() {
        (requireActivity() as MainActivity).showDialog(requireActivity())
        if (pickedPhoto == null) return
        StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                DBManager.updateUserImg(imgUrl)
                Glide.with(this@ProfileFragment).load(pickedPhoto)
                    .placeholder(R.drawable.img_default_user)
                    .error(R.drawable.img_default_user)
                    .into(binding.ivProfile)
                (requireActivity() as MainActivity).dismissDialog()
            }

            override fun onError(e: Exception) {
                (requireActivity() as MainActivity).dismissDialog()
            }

        })
    }

    private fun refreshAdapter(items: ArrayList<Post>) {
        val profileAdapter = ProfileAdapter(this, items)
        binding.rvProfile.adapter = profileAdapter
    }


}