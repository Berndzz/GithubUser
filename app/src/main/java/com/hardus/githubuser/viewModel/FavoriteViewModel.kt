package com.hardus.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hardus.githubuser.data.FavoriteRepository
import com.hardus.githubuser.database.entity.FavoriteEntity

class FavoriteViewModel(application: Application) :
    AndroidViewModel(application) {

    private val mFavoriteRepo: FavoriteRepository = FavoriteRepository(application)

    val favoriteUserList: LiveData<List<FavoriteEntity>> = mFavoriteRepo.getFavoriteUser()

}