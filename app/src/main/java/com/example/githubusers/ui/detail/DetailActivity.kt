package com.example.githubusers.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusers.R
import com.example.githubusers.data.local.entity.UserEntity
import com.example.githubusers.data.remote.response.UserItemDetails
import com.example.githubusers.databinding.ActivityDetailBinding
import com.example.githubusers.databinding.DetailsContentUserBinding
import com.example.githubusers.ui.favorite.FavoriteViewModel
import com.example.githubusers.ui.setting.SettingActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_LOGIN = "extra_user_login"
        const val EXTRA_PROFILE_URL = "extra_profile_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private lateinit var detailsContentUserBinding: DetailsContentUserBinding
    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val favoriteViewModel by lazy { FavoriteViewModel(applicationContext) }

    private var isFavorite: Boolean? = null
    private var userEntity = UserEntity()
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        detailsContentUserBinding = activityDetailBinding.detailsContent
        setContentView(activityDetailBinding.root)

        setSupportActionBar(activityDetailBinding.toolbar)

        username = intent.getStringExtra(EXTRA_USER_LOGIN) as String
        detailViewModel.getDetailResponse(username)

        val sectionsViewPager = SectionsViewPager(this)
        activityDetailBinding.viewPager.adapter = sectionsViewPager
        TabLayoutMediator(
            activityDetailBinding.tabLayout,
            activityDetailBinding.viewPager
        ) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.detailResponse.observe(this, { user -> setDetailResponse(user) })
        detailViewModel.isLoading.observe(this, { isLoading -> showLoading(isLoading) })
    }

    private fun setDetailResponse(user: UserItemDetails) {
        Glide.with(this)
            .load(user.avatarUrl)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_custom_loading))
            .error(R.drawable.ic_custom_error)
            .into(detailsContentUserBinding.sivAvatar)
        detailsContentUserBinding.tvName.text = user.name
        detailsContentUserBinding.tvLogin.text = user.login

        userEntity = UserEntity(
            login = user.login,
            name = user.name,
            avatarUrl = user.avatarUrl,
            htmlUrl = intent.getStringExtra(EXTRA_PROFILE_URL)
        )
        checkIsFavorite()

        activityDetailBinding.fabFavorite.setOnClickListener {
            runBlocking {
                launch(Dispatchers.IO) {
                    favoriteViewModel.delete(userEntity)
                    isFavorite = if (isFavorite == true) {
                        favoriteViewModel.delete(userEntity)
                        false
                    } else {
                        favoriteViewModel.insert(userEntity)
                        true
                    }
                }
            }

            val textResource: String = if (isFavorite == true) {
                showAddFavoriteButton()
                getString(R.string.added_to_favorite)
            } else {
                showRemoveFavoriteButton()
                getString(R.string.removed_from_favorite)
            }
            Toast.makeText(this, textResource, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIsFavorite() {
        userEntity.login?.let {
            favoriteViewModel.checkIsFavorite(it).observe(this, isFavoriteObserver)
        }
    }

    private val isFavoriteObserver: Observer<Boolean> =
        Observer { isFav ->
            isFav?.let {
                isFavorite = isFav
                if (isFav) {
                    showRemoveFavoriteButton()
                    favoriteViewModel.getUserByUsername(username)
                        ?.observe(this, { user -> user?.let { userEntity = it } })
                } else {
                    showAddFavoriteButton()
                }
            }
        }

    private fun showAddFavoriteButton() {
        activityDetailBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }

    private fun showRemoveFavoriteButton() {
        activityDetailBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
    }

    private fun showLoading(isLoading: Boolean) {
        activityDetailBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}