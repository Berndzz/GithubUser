package com.hardus.githubuser.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class FavoriteEntity(
    @field:ColumnInfo(name = "id")
    @field: PrimaryKey(autoGenerate = false)
    val id: Int,

    @field:ColumnInfo(name = "username")
    val username: String,

    @field: ColumnInfo(name = "urlToImage")
    var urlToImage: String? = null,

): Parcelable