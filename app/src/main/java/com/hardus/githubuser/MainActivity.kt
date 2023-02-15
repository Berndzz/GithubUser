package com.hardus.githubuser

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardus.githubuser.adapter.ListUserAdapter
import com.hardus.githubuser.databinding.ActivityMainBinding
import com.hardus.githubuser.respons.ItemsItem
import com.hardus.githubuser.util.Constant.EXTRA_USER
import com.hardus.githubuser.util.SettingPreferences
import com.hardus.githubuser.viewModel.MainViewModel
import com.hardus.githubuser.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_github_cop)
        supportActionBar?.title = "  " + getString(R.string.supportActionBarMain)

        val pref = SettingPreferences.getInstance(dataStore)

        val viewModelFactory = ViewModelFactory(this@MainActivity.application, "", pref)
        mainViewModel =
            ViewModelProvider(this@MainActivity, viewModelFactory)[MainViewModel::class.java]

        binding.rvGithubUsers.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)


        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvGithubUsers.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvGithubUsers.layoutManager = LinearLayoutManager(this)
        }

        mainViewModel.itemSearch.observe(this) { itemItems ->
            setItemsData(itemItems)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isDarkMode.observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.btnSearch.setOnClickListener { view ->
            mainViewModel.searchItems(binding.edSearch.text.toString())
            val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.icon_github, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_item_favorite -> {
                val toFavorite = Intent(this, FavoriteActivity::class.java)
                startActivity(toFavorite)
                true
            }
            R.id.action_dark_mode -> {
                val isDarkMode = mainViewModel.checkIsDarkModeSetting()!!
                mainViewModel.saveThemeSetting(!isDarkMode)
                invalidateOptionsMenu()
                true
            }
            else -> true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isDarkMode: Boolean = mainViewModel.checkIsDarkModeSetting() ?: return true
        val mode = menu?.findItem(R.id.action_dark_mode)
        if (isDarkMode) {
            mode?.setIcon(R.drawable.ic_light_mode)
        } else {
            mode?.setIcon(R.drawable.ic_night_mode)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setItemsData(itemUsers: List<ItemsItem>) {
        binding.rvGithubUsers.layoutManager = LinearLayoutManager(this)
        val adapter = ListUserAdapter(itemUsers)
        binding.rvGithubUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)
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

