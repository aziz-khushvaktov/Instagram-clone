package com.example.instaclonekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclonekotlin.R
import com.example.instaclonekotlin.databinding.ItemUserSearchBinding
import com.example.instaclonekotlin.fragment.SearchFragment
import com.example.instaclonekotlin.model.User

class SearchAdapter(var fragment: SearchFragment, var items: ArrayList<User>): BaseAdapter() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = items[position]
        if (holder is UserViewHolder) {
            holder.binding.apply {
                tvFullName.text = user.fullName
                tvEmail.text = user.email
                Glide.with(fragment).load(user.userImg)
                    .placeholder(R.drawable.img_default_user)
                    .error(R.drawable.img_default_user)
                    .into(ivProfile)

                if (!user.isFollowed) {
                    tvFollow.text = fragment.getText(R.string.str_follow)
                }else {
                    tvFollow.text = fragment.getText(R.string.str_following)
                }

                tvFollow.setOnClickListener {
                    if (!user.isFollowed) {
                        tvFollow.text = fragment.getText(R.string.str_following)
                    }else {
                        tvFollow.text = fragment.getText(R.string.str_follow)
                    }
                }
                fragment.followOrUnfollow(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context)))
    }

    class UserViewHolder(var binding: ItemUserSearchBinding): RecyclerView.ViewHolder(binding.root) {

    }
}