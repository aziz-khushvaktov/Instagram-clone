package com.example.instaclonekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ItemPostHomeBinding
import com.example.instaclonekotlin.fragment.HomeFragment
import com.example.instaclonekotlin.manager.AuthManager
import com.example.instaclonekotlin.model.Post

class HomeAdapter(var fragment: HomeFragment, var items: ArrayList<Post>): BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(ItemPostHomeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            val iv_post = holder.binding.ivPost
            holder.binding.apply {
                tvFullName.text = post.fullName
                tvCaption.text = post.caption
                tvTime.text = post.currentDate
                Glide.with(fragment).load(post.userImg).placeholder(R.drawable.img_default_user)
                    .error(R.drawable.img_default_user).into(ivProfile)
                Glide.with(fragment).load(post.postImg).into(iv_post)

                ivLike.setOnClickListener {
                    if (post.isLiked) {
                        post.isLiked = false
                        ivLike.setImageResource(R.drawable.ic_favorite)
                    }else {
                        post.isLiked = true
                        ivLike.setImageResource(R.drawable.ic_favorite_filled)
                    }
                    fragment.likeOrUnlikePost(post)
                }
                if (post.isLiked) {
                    ivLike.setImageResource(R.drawable.ic_favorite_filled)
                }else {
                    ivLike.setImageResource(R.drawable.ic_favorite)
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

    class PostViewHolder(val binding: ItemPostHomeBinding): RecyclerView.ViewHolder(binding.root){

    }
}