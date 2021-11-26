package com.example.githubusers.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.R
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.databinding.ActivityHomeBinding
import com.example.githubusers.ui.detail.DetailActivity
import com.example.githubusers.ui.favorite.FavoriteActivity
import com.example.githubusers.ui.setting.SettingActivity
import com.example.githubusers.ui.setting.SettingPreferences
import com.example.githubusers.ui.setting.SettingViewModel
import com.example.githubusers.ui.setting.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        setSupportActionBar(activityHomeBinding.toolbar)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        activityHomeBinding.searchView.setSearchableInfo(
            searchManager.getSearchableInfo(
                componentName
            )
        )
        activityHomeBinding.searchView.queryHint = resources.getString(R.string.search)
        activityHomeBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                homeViewModel.getSearchResponse(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref)).get(SettingViewModel::class.java)

        settingViewModel.getThemeSetting().observe(this, { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        homeViewModel.listResponse.observe(this, { users -> setListResponse(users) })
        homeViewModel.searchResponse.observe(this, { users -> setListResponse(users) })
        homeViewModel.isLoading.observe(this, { isLoading -> showLoading(isLoading) })
    }

    private fun setListResponse(users: List<UserItemList>) {
        val linearLayoutManager = LinearLayoutManager(this)
        activityHomeBinding.rvUserList.layoutManager = linearLayoutManager
        val homeAdapter = HomeAdapter(users)
        activityHomeBinding.rvUserList.adapter = homeAdapter

        homeAdapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(users: UserItemList) {
                showDetailsActivity(users)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        activityHomeBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDetailsActivity(users: UserItemList) {
        Toast.makeText(this, users.login, Toast.LENGTH_SHORT).show()

        val detailIntent = Intent(this, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_USER_LOGIN, users.login)
        detailIntent.putExtra(DetailActivity.EXTRA_PROFILE_URL, users.htmlUrl)
        startActivity(detailIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingActivity::class.java)
                startActivity(settingsIntent)
            }
            R.id.favorite ->{
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}