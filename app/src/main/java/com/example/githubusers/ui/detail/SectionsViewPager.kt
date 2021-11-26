package com.example.githubusers.ui.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusers.ui.follower.FollowerFragment
import com.example.githubusers.ui.following.FollowingFragment

class SectionsViewPager(appCompatActivity: AppCompatActivity) :
    FragmentStateAdapter(appCompatActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }

        return fragment as Fragment
    }
}