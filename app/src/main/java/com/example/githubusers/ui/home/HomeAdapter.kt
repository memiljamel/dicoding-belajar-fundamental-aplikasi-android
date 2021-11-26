package com.example.githubusers.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusers.R
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.databinding.ListItemUserBinding

class HomeAdapter(private val userList: List<UserItemList>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listItemUserBinding =
            ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(listItemUserBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(userList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ListViewHolder(private val listItemUserBinding: ListItemUserBinding) :
        RecyclerView.ViewHolder(listItemUserBinding.root) {
        fun bind(users: UserItemList) {
            Glide.with(itemView.context)
                .load(users.avatarUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_custom_loading))
                .error(R.drawable.ic_custom_error)
                .into(listItemUserBinding.sivAvatar)
            listItemUserBinding.tvLogin.text = users.login
            listItemUserBinding.htmlUrl.text = users.htmlUrl
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(users: UserItemList)
    }
}