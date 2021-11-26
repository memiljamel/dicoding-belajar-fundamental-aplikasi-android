package com.example.githubusers.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.remote.network.ApiConfig
import com.example.githubusers.data.remote.response.UserItemDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    companion object {
        private const val TAG = "detail_view_model"
    }

    private val _detailResponse = MutableLiveData<UserItemDetails>()
    val detailResponse: LiveData<UserItemDetails> get() = _detailResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getDetailResponse(login: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetails(login)
        client.enqueue(object : Callback<UserItemDetails> {
            override fun onResponse(
                call: Call<UserItemDetails>,
                response: Response<UserItemDetails>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserItemDetails>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}