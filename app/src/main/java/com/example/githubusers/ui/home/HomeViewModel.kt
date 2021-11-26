package com.example.githubusers.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.remote.network.ApiConfig
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel() : ViewModel() {

    companion object {
        private const val TAG = "home_view_model"
    }

    private val _listResponse = MutableLiveData<List<UserItemList>>()
    val listResponse: LiveData<List<UserItemList>> get() = _listResponse

    private val _searchResponse = MutableLiveData<List<UserItemList>>()
    val searchResponse: LiveData<List<UserItemList>> get() = _searchResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        getListResponse()
    }

    private fun getListResponse() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList()
        client.enqueue(object : Callback<List<UserItemList>> {
            override fun onResponse(
                call: Call<List<UserItemList>>,
                response: Response<List<UserItemList>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItemList>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSearchResponse(login: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchResults(login)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResponse.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}