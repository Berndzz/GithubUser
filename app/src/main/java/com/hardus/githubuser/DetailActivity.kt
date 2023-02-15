package com.hardus.githubuser

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hardus.githubuser.R.string.supportActionBarDetail
import com.hardus.githubuser.adapter.SectionPagerAdapter
import com.hardus.githubuser.database.entity.FavoriteEntity
import com.hardus.githubuser.databinding.ActivityDetailBinding
import com.hardus.githubuser.respons.ItemsItem
import com.hardus.githubuser.respons.ResponseDetailUsers
import com.hardus.githubuser.util.Constant.EXTRA_FAVORITE
import com.hardus.githubuser.util.Constant.EXTRA_USER
import com.hardus.githubuser.util.Constant.TAB_TITLES
import com.hardus.githubuser.util.Helper
import com.hardus.githubuser.util.SettingPreferences
import com.hardus.githubuser.viewModel.DetailViewModel
import com.hardus.githubuser.viewModel.ViewModelFactory


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private val helper = Helper
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(supportActionBarDetail)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val pref = SettingPreferences.getInstance(dataStore)
        val user = intent.getParcelableExtra<ItemsItem>(EXTRA_USER)
        val favoriteUser = intent.getParcelableExtra<FavoriteEntity>(EXTRA_FAVORITE)
        val username: String = user?.login ?: favoriteUser?.username.toString()

        val viewModelFactory = ViewModelFactory(this@DetailActivity.application, username, pref)
        detailViewModel =
            ViewModelProvider(this@DetailActivity, viewModelFactory)[DetailViewModel::class.java]

        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    detailViewModel.getListFollowers(username)
                } else {
                    detailViewModel.getListFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                
            }
        })

        // detail viewModel
        detailViewModel.detailUser.observe(this) { detail ->
            parserDetailUser(detail)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getDetailUser(username)

        detailViewModel.favoriteUserExist.observe(this) { favoriteExist ->
            if (favoriteExist) {
                binding.imbFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        baseContext,
                        R.drawable.favorite_white_24
                    )
                )
                binding.imbFavorite.changeIconColor(R.color.red)
            } else {
                binding.imbFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        baseContext,
                        R.drawable.favorite_border
                    )
                )
                binding.imbFavorite.changeIconColor(R.color.black)
            }
        }

        binding.imbFavorite.setOnClickListener {
            val favUser = favoriteUser ?: FavoriteEntity(user!!.id, user.login, user.avatarUrl)
            if (detailViewModel.checkFavoriteUserExist()!!) {
                detailViewModel.deleteFavoriteUser(favUser)
            } else {
                detailViewModel.addFavoriteUser(favUser)
            }
        }
    }

    private fun parserDetailUser(rdu: ResponseDetailUsers) {

        binding.apply {
            tvDetailName.text = rdu.name
            tvDetailUsername.text = rdu.login
            tvNumberFollowers.text = helper.getFormattedNumber(rdu.followers)
            tvNumberFollowing.text = helper.getFormattedNumber(rdu.following)
            tvNumberRepository.text = helper.getFormattedNumber(rdu.publicRepos)
            tvDetailCompany.text = rdu.company
            tvDetailLocation.text = rdu.location
            Glide.with(this@DetailActivity)
                .load(rdu.avatarUrl)
                .into(ivImgDetail)
            Glide.with(this@DetailActivity)
                .load(rdu.avatarUrl)
                .into(ivImgBackground)

            if (rdu.company.isNullOrEmpty()) {
                tvDetailCompany.text = getString(R.string.no_data)
            }
            if (rdu.location.isNullOrEmpty()) {
                tvDetailLocation.text = getString(R.string.no_data)
            }
            if (rdu.followers == 0) {
                tvNumberFollowers.text = getString(R.string.no_data)
            }
            if (rdu.following == 0) {
                tvNumberFollowing.text = getString(R.string.no_data)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun ImageButton.changeIconColor(@ColorRes clr: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, clr))
    }


}

