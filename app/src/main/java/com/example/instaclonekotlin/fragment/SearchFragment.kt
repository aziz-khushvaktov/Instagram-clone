package com.example.instaclonekotlin.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instaclonekotlin.adapter.SearchAdapter
import com.example.instaclonekotlin.databinding.FragmentSearchBinding
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.manager.DBManager
import com.example.instaclonekotlin.manager.handler.DBFollowHandler
import com.example.instaclonekotlin.manager.handler.DBUserHandler
import com.example.instaclonekotlin.manager.handler.DBUsersHandler
import com.example.instaclonekotlin.model.User

/**
 * In SearchFragment, user can search friends and  follow or unfollow them
 */

class SearchFragment : BaseFragment() {

    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private var items = ArrayList<User>()
    var users = ArrayList<User>() // It's for list of users which is seen when search

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initViews(binding.root)
        return binding.root
    }

    private fun initViews(view: View) {
        loadUsers()
        binding.apply {
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val keyword = s.toString().trim()
                    usersByKeyword(keyword)
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }

    private fun usersByKeyword(keyword: String) {
        if (keyword.isEmpty()) {
            refreshAdapter(items)
        }
        users.clear()
        for (user in items) {
            if (user.fullName.toLowerCase().startsWith(keyword.toLowerCase())) {
                users.add(user)
                //items.addAll(users)
                refreshAdapter(users)
            }
        }
        refreshAdapter(users)
    }

    private fun refreshAdapter(items: ArrayList<User>) {
        val searchAdapter = SearchAdapter(this, items)
        binding.rvSearch.adapter = searchAdapter
    }

    private fun loadUsers() {
        showDialog(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DBManager.loadUsers(object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                DBManager.loadFollowing(uid, object : DBUsersHandler{
                    override fun onSuccess(following: ArrayList<User>) {
                        items.clear()
                        items.addAll(mergedUsers(uid,users,following))
                        refreshAdapter(items)
                    }

                    override fun onError(e: java.lang.Exception) {}

                })
                dismissDialog()
            }

            override fun onError(e: Exception) {
                dismissDialog()
            }

        })
    }

    private fun mergedUsers(uid: String, users: ArrayList<User>, following: ArrayList<User>): Collection<User> {
        val items = ArrayList<User>()

        for (u in users) {
            val user = u
            for (f in following) {
                if (u.uid == f.uid) {
                    user.isFollowed = true
                    break
                }
            }
            if (uid != user.uid) {
                items.add(user)
            }
        }

        return items
    }

    fun followOrUnfollow(to: User) {
        val uid = AuthManager.currentUser()!!.uid
        if (!to.isFollowed) {
            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }

    private fun followUser(uid: String, to: User) {
        DBManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DBManager.followUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isDone: Boolean) {
                        to.isFollowed = true
                        // store Posts to my feed
                        DBManager.storePostsToMyFeed(uid,to)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }
            override fun onError(e: Exception) {

            }
        })
    }

    private fun unFollowUser(uid: String, to: User) {
        DBManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DBManager.unFollowUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isDone: Boolean) {
                        to.isFollowed = false
                        // remove Posts to my feed
                        DBManager.removePostsToMyFeed(uid,to)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }

}