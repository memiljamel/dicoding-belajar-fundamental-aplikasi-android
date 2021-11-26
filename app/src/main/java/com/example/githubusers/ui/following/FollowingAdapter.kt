package com.example.githubusers.ui.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusers.R
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.databinding.ListItemUserBinding

class FollowingAdapter(private val followerList: List<UserItemList>) :
    RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listItemUserBinding =
            ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(listItemUserBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val follower = followerList[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int {
        return followerList.size
    }

    inner class ListViewHolder(private val listItemUserBinding: ListItemUserBinding) :
        RecyclerView.ViewHolder(listItemUserBinding.root) {
        fun bind(follower: UserItemList) {
            Glide.with(itemView.context)
                .load(follower.avatarUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_custom_loading))
                .error(R.drawable.ic_custom_error)
                .into(listItemUserBinding.sivAvatar)
            listItemUserBinding.tvLogin.text = follower.login
            listItemUserBinding.htmlUrl.text = follower.htmlUrl
        }
    }
}