package com.hardus.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.hardus.githubuser.api.ApiConfig
import com.hardus.githubuser.respons.ItemsItem
import com.hardus.githubuser.respons.ResponseSearch
import com.hardus.githubuser.util.Constant
import com.hardus.githubuser.util.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _itemsSearch = MutableLiveData<List<ItemsItem>>()
    val itemSearch: LiveData<List<ItemsItem>> = _itemsSearch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val isDarkMode: LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    init {
        findItems()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun checkIsDarkModeSetting(): Boolean? {
        return isDarkMode.value
    }

    private fun findItems() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchItem(Constant.USERNAME)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _itemsSearch.value = response.body()?.items
                } else {
                    Log.e(Constant.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                Log.e(Constant.TAG, "onFailure : ${t.message.toString()}")
            }
        })
    }

    fun searchItems(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchItem(user)
        client.enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _itemsSearch.value = response.body()?.items
                } else {
                    Log.e(Constant.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                _isLoading.value = false
                Log.e(Constant.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}