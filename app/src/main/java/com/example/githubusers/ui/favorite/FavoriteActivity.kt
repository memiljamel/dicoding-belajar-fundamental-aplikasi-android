package com.example.githubusers.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.local.entity.UserEntity
import com.example.githubusers.databinding.ActivityFavoriteBinding
import com.example.githubusers.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var activityFavoriteBinding: ActivityFavoriteBinding
    private val favoriteViewModel by lazy { FavoriteViewModel(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(activityFavoriteBinding.root)

        setSupportActionBar(activityFavoriteBinding.toolbar)
        getFavoriteUserList()
    }

    private fun getFavoriteUserList() {
        favoriteViewModel.getFavoriteUser().observe(this, { users ->
            if (users.isEmpty()) {
                activityFavoriteBinding.tvEmpty.visibility = View.VISIBLE
                activityFavoriteBinding.rvFavUserList.visibility = View.GONE
            } else {
                setListResponse(users)
                activityFavoriteBinding.tvEmpty.visibility = View.GONE
            }
        })
    }

    private fun setListResponse(users: List<UserEntity>) {
        val linearLayoutManager = LinearLayoutManager(this)
        activityFavoriteBinding.rvFavUserList.layoutManager = linearLayoutManager
        val favoriteUserAdapter = FavoriteUserAdapter(users)
        activityFavoriteBinding.rvFavUserList.adapter = favoriteUserAdapter

        favoriteUserAdapter.setOnItemClickCallback(object :
            FavoriteUserAdapter.OnItemClickCallback {
            override fun onItemClicked(users: UserEntity) {
                showDetailsActivity(users)
            }
        })
    }

    private fun showDetailsActivity(users: UserEntity) {
        Toast.makeText(this, users.login, Toast.LENGTH_SHORT).show()

        val detailIntent = Intent(this, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_USER_LOGIN, users.login)
        startActivity(detailIntent)
    }

    override fun onResume() {
        super.onResume()
        getFavoriteUserList()
    }
}