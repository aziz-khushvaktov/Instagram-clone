package com.example.instaclonekotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclonekotlin.databinding.ItemPostProfileBinding
import com.example.instaclonekotlin.fragment.ProfileFragment
import com.example.instaclonekotlin.model.Post
import com.example.instaclonekotlin.utils.Utils

class ProfileAdapter(var fragment: ProfileFragment, var items: ArrayList<Post>): BaseAdapter() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(ItemPostProfileBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var post = items[position]
        if (holder is PostViewHolder) {
            holder.binding.apply {
                setViewHeight(ivPost)
                tvCaption.text = post.caption
                Glide.with(fragment).load(post.postImg).into(ivPost)
            }
        }
    }

    class PostViewHolder(var binding: ItemPostProfileBinding): RecyclerView.ViewHolder(binding.root) {

    }
    /**
     * Set ShapeableImageView height as a half of screen width
     */
    private fun setViewHeight(view: View) {
        val params: ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(fragment.requireActivity().application).width / 2
        view.layoutParams = params
    }
}