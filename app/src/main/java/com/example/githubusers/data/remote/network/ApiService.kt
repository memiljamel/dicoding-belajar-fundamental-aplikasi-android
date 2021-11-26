package com.example.githubusers.data.remote.network

import com.example.githubusers.data.remote.response.UserItemDetails
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    @Headers("Authorization: token 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")
    fun getUserList(): Call<List<UserItemList>>

    @GET("users/{login}")
    @Headers("Authorization: token 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")
    fun getUserDetails(@Path("login") login: String): Call<UserItemDetails>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchResults(@Query("q") login: String): Call<UserResponse>

    @GET("users/{login}/followers")
    @Headers("Authorization: token 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")
    fun getFollowerList(@Path("login") login: String): Call<List<UserItemList>>

    @GET("users/{login}/following")
    @Headers("Authorization: token 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")
    fun getFollowingList(@Path("login") login: String): Call<List<UserItemList>>
}