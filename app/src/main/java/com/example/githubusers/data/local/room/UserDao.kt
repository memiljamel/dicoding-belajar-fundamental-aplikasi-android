package com.example.githubusers.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubusers.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE login=(:username)")
    fun getUserByUsername(username: String): LiveData<UserEntity>?

    @Query("SELECT EXISTS(SELECT * FROM UserEntity WHERE login=(:username))")
    fun isUserFavorite(username: String): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)
}