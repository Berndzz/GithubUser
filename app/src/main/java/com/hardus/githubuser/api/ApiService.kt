package com.hardus.githubuser.api

import com.hardus.githubuser.respons.*
import com.hardus.githubuser.util.Constant.TOKEN
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $TOKEN")
    fun getSearchItem(
        @Query("q") q: String
    ): Call<ResponseSearch>


    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUsers>


    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN")
    fun getFollowers(
        @Path("username") followersApi: String
    ): Call<List<ItemsItem>>


    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN")
    fun getFollowing(
        @Path("username") followingApi: String
    ): Call<List<ItemsItem>>
}