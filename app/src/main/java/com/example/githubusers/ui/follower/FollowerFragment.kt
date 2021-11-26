package com.example.githubusers.ui.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.remote.response.UserItemList
import com.example.githubusers.databinding.FragmentFollowerBinding
import com.example.githubusers.ui.detail.DetailActivity

class FollowerFragment : Fragment() {

    private lateinit var fragmentFollowerBinding: FragmentFollowerBinding
    private val followerViewModel by activityViewModels<FollowerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowerBinding = FragmentFollowerBinding.inflate(inflater, container, false)
        return fragmentFollowerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = activity?.intent?.getStringExtra(DetailActivity.EXTRA_USER_LOGIN) as String
        followerViewModel.getListResponse(login)

        followerViewModel.listResponse.observe(
            viewLifecycleOwner,
            { followers -> setListResponse(followers) })
        followerViewModel.isLoading.observe(
            viewLifecycleOwner,
            { isLoading -> showLoading(isLoading) })
    }

    private fun setListResponse(followers: List<UserItemList>) {
        val linearLayoutManager = LinearLayoutManager(activity)
        fragmentFollowerBinding.rvFollowerList.layoutManager = linearLayoutManager
        val followerAdapter = FollowerAdapter(followers)
        fragmentFollowerBinding.rvFollowerList.adapter = followerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentFollowerBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}