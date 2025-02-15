package com.zhouguan.learnjetpack.service

import com.zhouguan.learnjetpack.entity.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("user/{userId}")
    fun getUser(@Path("userId") userId: String): Call<User>
}
