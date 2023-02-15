package com.hardus.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.hardus.githubuser.database.entity.FavoriteEntity
import com.hardus.githubuser.database.room.FavoriteDao
import com.hardus.githubuser.database.room.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavoriteRepository(application: Application) {
    private var mFavDao: FavoriteDao
    private var executeService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = FavoriteDatabase.getDatabase(application)
        mFavDao = database.favoriteDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>> = mFavDao.getFavorite()

    fun insertFavorite(user: FavoriteEntity) {
        executeService.execute { mFavDao.insertFavorite(user) }
    }
    fun getFavoriteUsername(username: String): LiveData<Boolean> =
        mFavDao.isFavoriteSave(username)

    fun deleteFavorite(user: FavoriteEntity) {
        executeService.execute { mFavDao.deleteFavorite(user) }
    }

}