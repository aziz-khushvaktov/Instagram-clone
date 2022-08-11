package com.example.instaclonekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ItemPostFavoriteBinding
import com.example.instaclonekotlin.fragment.FavoriteFragment
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.model.Post

class FavoriteAdapter(var fragment: FavoriteFragment, var items: ArrayList<Post>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(ItemPostFavoriteBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            val iv_post = holder.binding.ivPost
            holder.binding.apply {
                Glide.with(fragment).load(post.postImg).into(iv_post)
                tvCaption.text = post.caption
                tvFullName.text = post.fullName
                tvTime.text = post.currentDate
                Glide.with(fragment).load(post.userImg).into(ivProfile)

                ivLike.setOnClickListener {
                    if (post.isLiked) {
                        post.isLiked = false
                        ivLike.setImageResource(R.drawable.ic_favorite)
                    } else {
                        post.isLiked = true
                        ivLike.setImageResource(R.drawable.ic_favorite_filled)
                    }
                    fragment.likeOrUnlikePost(post)
                }

                val uid = AuthManager.currentUser()!!.uid
                if (uid == post.uid) {
                    ivMore.isVisible = true
                }else {
                    ivMore.isVisible = false
                }

                ivMore.setOnClickListener { fragment.showDeleteDialog(post) }
            }

        }
    }

    override fun getItemCount(): Int = items.size

    class PostViewHolder(val binding: ItemPostFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}