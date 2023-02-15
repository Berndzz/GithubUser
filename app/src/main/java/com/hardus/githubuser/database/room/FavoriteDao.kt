package com.hardus.githubuser.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hardus.githubuser.database.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite ORDER BY username DESC")
    fun getFavorite(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(fav: FavoriteEntity)

    @Delete
    fun deleteFavorite(fav: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE username = :username )")
    fun isFavoriteSave(username: String): LiveData<Boolean>
}