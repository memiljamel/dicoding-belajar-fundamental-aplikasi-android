package com.example.githubusers.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.githubusers.data.local.entity.UserEntity

class UserDiffCallback(
    private val oldUserList: List<UserEntity>,
    private val newUserList: List<UserEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldUserList.size
    }

    override fun getNewListSize(): Int {
        return newUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUserList[oldItemPosition].id == newUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = oldUserList[oldItemPosition]
        val newEmployee = newUserList[newItemPosition]

        return oldEmployee.login == newEmployee.login && oldEmployee.htmlUrl == newEmployee.htmlUrl
    }
}