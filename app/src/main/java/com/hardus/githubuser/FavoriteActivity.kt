package com.hardus.githubuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardus.githubuser.adapter.FavoriteAdapter
import com.hardus.githubuser.data.FavoriteRepository
import com.hardus.githubuser.database.entity.FavoriteEntity
import com.hardus.githubuser.databinding.ActivityFavoriteBinding
import com.hardus.githubuser.util.Constant.EXTRA_FAVORITE
import com.hardus.githubuser.util.SettingPreferences
import com.hardus.githubuser.viewModel.FavoriteViewModel
import com.hardus.githubuser.viewModel.ViewModelFactory



class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.supportActionBarFavorite)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val pref = SettingPreferences.getInstance(dataStore)
        binding.rvFavorite.setHasFixedSize(true)

        val viewModelFactory = ViewModelFactory(this@FavoriteActivity.application, "", pref)
        favoriteViewModel = ViewModelProvider(
            this@FavoriteActivity,
            viewModelFactory
        )[FavoriteViewModel::class.java]

        favoriteViewModel.favoriteUserList.observe(this) { favList ->
            if (favList.isEmpty()) {
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.rvFavorite.visibility = View.VISIBLE
                showFavoriteList(favList)
            }
        }
    }

    private fun showFavoriteList(favList: List<FavoriteEntity>) {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        val listFavoriteAdapter =
            FavoriteAdapter(favList, FavoriteRepository(this@FavoriteActivity.application))
        binding.rvFavorite.adapter = listFavoriteAdapter

        listFavoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteEntity) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_FAVORITE, data)
                startActivity(intent)
            }

        })
    }

}