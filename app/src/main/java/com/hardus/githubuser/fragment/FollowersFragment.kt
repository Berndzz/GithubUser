package com.hardus.githubuser.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardus.githubuser.DetailActivity
import com.hardus.githubuser.adapter.ListUserAdapter
import com.hardus.githubuser.databinding.FragmentFollowersBinding
import com.hardus.githubuser.respons.ItemsItem
import com.hardus.githubuser.util.Constant.EXTRA_USER
import com.hardus.githubuser.util.Constant.USER_
import com.hardus.githubuser.util.SettingPreferences
import com.hardus.githubuser.viewModel.DetailViewModel
import com.hardus.githubuser.viewModel.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FollowersFragment : Fragment() {


    private lateinit var binding: FragmentFollowersBinding
    private lateinit var followersViewModel: DetailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)

        val viewModelFactory =
            ViewModelFactory(this@FollowersFragment.requireActivity().application, "", pref)
        followersViewModel = ViewModelProvider(
            this@FollowersFragment.requireActivity(),
            viewModelFactory
        )[DetailViewModel::class.java]

        val username = arguments?.getString(USER_) ?: ""
        followersViewModel.followers.observe(viewLifecycleOwner) { followers ->
            if (followers == null) {
                followersViewModel.getListFollowers(username)

            } else {
                showFollowers(followers)
            }
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        return binding.root

    }


    private fun showFollowers(user: List<ItemsItem>) {

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        val adapter = ListUserAdapter(user)
        binding.rvFollowers.adapter = adapter
        val itemDecoration =
            DividerItemDecoration(activity, LinearLayoutManager(activity).orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intentDetail = Intent(activity, DetailActivity::class.java)
                intentDetail.putExtra(EXTRA_USER, data)
                startActivity(intentDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}