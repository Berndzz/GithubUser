package com.hardus.githubuser.util

import com.hardus.githubuser.BuildConfig
import com.hardus.githubuser.R

object Constant {
    const val BASE_URL : String = BuildConfig.URL
    const val USERNAME = "Berndzz"
    const val TOKEN  = BuildConfig.KEY
    const val TAG = "DetailViewModel"
    const val EXTRA_USER = "extra_user"
    const val EXTRA_FAVORITE = "extra_favorite"
    val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )
    const val USER_ = "username"
}