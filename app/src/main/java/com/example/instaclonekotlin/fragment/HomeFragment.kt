package com.example.instaclonekotlin.fragment

import android.content.Context
import android.os.Bundle
import android.os.FileUriExposedException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.util.Util
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.adapter.HomeAdapter
import com.example.instaclonekotlin.databinding.FragmentHomeBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.DBManager
import com.example.instaclonekotlin.manager.handler.DBPostHandler
import com.example.instaclonekotlin.manager.handler.DBPostsHandler
import com.example.instaclonekotlin.model.Post
import com.example.instaclonekotlin.utils.DialogListener
import com.example.instaclonekotlin.utils.Utils
import java.lang.RuntimeException

/**
 * In HomeFragment, user can check his/her posts and friends' posts
 */

class HomeFragment : BaseFragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    lateinit var homeAdapter: HomeAdapter
    private val TAG = HomeFragment::class.java.simpleName
    private var listener: HomeListener? = null
    private var feeds = ArrayList<Post>()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if(isVisibleToUser && feeds.size > 0) {
            loadMyFeeds()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        initViews(binding.root)
        return binding.root
    }


    private fun initViews(view: View) {
        binding.apply {
            ivCamera.setOnClickListener { listener!!.scrollToUpload() }
            loadMyFeeds()
        }
    }

    private fun loadMyFeeds() {
        showDialog(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DBManager.loadFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(post: ArrayList<Post>) {
                dismissDialog()
                feeds.clear()
                feeds.addAll(post)
                refreshAdapter(feeds)
            }

            override fun onError(e: Exception) {

            }

        })
    }


    private fun refreshAdapter(items: ArrayList<Post>) {
        homeAdapter = HomeAdapter(this,items)
        binding.rvHome.adapter = homeAdapter
    }

    /**
     * onAttach is for communications of Fragments
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if(context is HomeListener) {
            context
        }else {
            throw RuntimeException("$context must implement HomeListener")
        }
    }

    /**
     * onDetach is for communications of Fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun likeOrUnlikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DBManager.likeFeedPost(uid,post)
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

    private fun deletePost(post: Post) {
        DBManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadMyFeeds()
            }

            override fun onError(e: Exception) {}

        })
    }


    /**
     * This interface is create for communications with UploadFragment
     */
    interface HomeListener {
        fun scrollToUpload()
    }

}