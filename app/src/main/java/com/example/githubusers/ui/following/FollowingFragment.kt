package com.example.githubusers.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.databinding.FragmentFollowingBinding
import com.example.githubusers.ui.detail.DetailActivity

class FollowingFragment : Fragment() {

    private lateinit var fragmentFollowingBinding: FragmentFollowingBinding
    private val followingViewModel by activityViewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater, container, false)
        return fragmentFollowingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = activity?.intent?.getStringExtra(DetailActivity.EXTRA_USER_LOGIN) as String
        followingViewModel.getListResponse(login)

        followingViewModel.listResponse.observe(
            viewLifecycleOwner,
            { following -> setListResponse(following) })
        followingViewModel.isLoading.observe(
            viewLifecycleOwner,
            { isLoading -> showLoading(isLoading) })
    }

    private fun setListResponse(following: List<UserItemList>) {
        val linearLayoutManager = LinearLayoutManager(activity)
        fragmentFollowingBinding.rvFollowingList.layoutManager = linearLayoutManager
        val followingAdapter = FollowingAdapter(following)
        fragmentFollowingBinding.rvFollowingList.adapter = followingAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentFollowingBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}