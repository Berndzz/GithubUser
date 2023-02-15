package com.hardus.githubuser.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hardus.githubuser.api.ApiConfig
import com.hardus.githubuser.data.FavoriteRepository
import com.hardus.githubuser.database.entity.FavoriteEntity
import com.hardus.githubuser.respons.ItemsItem
import com.hardus.githubuser.respons.ResponseDetailUsers
import com.hardus.githubuser.util.Constant.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application, username: String) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<ResponseDetailUsers>()
    val detailUser: LiveData<ResponseDetailUsers> = _detailUser

    private val _followers = MutableLiveData<List<ItemsItem>?>(null)
    val followers: LiveData<List<ItemsItem>?> = _followers

    private val _following = MutableLiveData<List<ItemsItem>?>(null)
    val following: LiveData<List<ItemsItem>?> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteRepo: FavoriteRepository = FavoriteRepository(application)
    val favoriteUserExist: LiveData<Boolean> = mFavoriteRepo.getFavoriteUsername(username)


    fun addFavoriteUser(favUser: FavoriteEntity) {
        mFavoriteRepo.insertFavorite(favUser)
    }

    fun deleteFavoriteUser(favUser: FavoriteEntity) {
        mFavoriteRepo.deleteFavorite(favUser)
    }

    fun checkFavoriteUserExist(): Boolean? {
        return favoriteUserExist.value
    }

    fun getDetailUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(user)
        client.enqueue(object : Callback<ResponseDetailUsers> {
            override fun onResponse(
                call: Call<ResponseDetailUsers>,
                response: Response<ResponseDetailUsers>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetailUsers>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getListFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")

                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message.toString()}")
            }

        })
    }

    fun getListFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    _following.value = response.body()
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message.toString()}")
            }

        })
    }
}
