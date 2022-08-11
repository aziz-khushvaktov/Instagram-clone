package com.example.instaclonekotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.util.Util
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.adapter.FavoriteAdapter
import com.example.instaclonekotlin.databinding.FragmentFavoriteBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.DBManager
import com.example.instaclonekotlin.manager.handler.DBPostHandler
import com.example.instaclonekotlin.manager.handler.DBPostsHandler
import com.example.instaclonekotlin.model.Post
import com.example.instaclonekotlin.utils.DialogListener
import com.example.instaclonekotlin.utils.Utils

/**
 * In FavoriteFragment, user can check all liked posts
 */

class FavoriteFragment : BaseFragment() {

    private val binding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        initViews(binding.root)
        return binding.root

    }

    private fun initViews(view: View) {
        loadLikedFeeds()
    }

    private fun loadLikedFeeds() {
        showDialog(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DBManager.loadLikedFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(post: ArrayList<Post>) {
                dismissDialog()
                refreshAdapter(post)
            }

            override fun onError(e: Exception) {
                dismissDialog()
            }

        })
    }

    private fun refreshAdapter(items: ArrayList<Post>) {
        favoriteAdapter = FavoriteAdapter(this,items)
        binding.rvFavorite.adapter = favoriteAdapter
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DBManager.likeFeedPost(uid,post)

        loadLikedFeeds()
    }

    fun showDeleteDialog(post: Post) {
        Utils.dialogDouble(requireContext(),getString(R.string.str_delete_post), object : DialogListener {
            override fun onCallBack(isChosen: Boolean) {
                if (isChosen) {
                    deletePost(post)
                }
            }

        })
    }

    fun deletePost(post: Post) {
        DBManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadLikedFeeds()
            }

            override fun onError(e: Exception) {

            }

        })
    }

}