package com.example.githubusers.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.local.entity.UserEntity
import com.example.githubusers.data.local.room.UserDao
import com.example.githubusers.data.local.room.UserDatabase

class FavoriteViewModel(context: Context) : ViewModel() {

    private val userDb: UserDatabase = UserDatabase.getDatabase(context)
    private val userDao: UserDao = userDb.userDao()

    fun getFavoriteUser() = userDao.getAllUsers()

    fun getUserByUsername(username: String) = userDao.getUserByUsername(username)

    fun checkIsFavorite(username: String) = userDao.isUserFavorite(username)

    fun insert(user: UserEntity) {
        userDao.insert(user)
    }

    fun delete(user: UserEntity) {
        userDao.delete(user)
    }
}