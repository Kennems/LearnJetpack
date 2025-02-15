package com.zhouguan.learnjetpack.entity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Callback
import com.zhouguan.learnjetpack.service.UserService
import com.zhouguan.learnjetpack.util.ServiceCreator
import retrofit2.Call
import retrofit2.Response

object Repository {

    private const val TAG = "Repository"

    fun getUser(userId: String): LiveData<User> {
        val liveData = MutableLiveData<User>()

        val userService = ServiceCreator.create(UserService::class.java)

        userService.getUser(userId).enqueue(object : Callback<User>{
            override fun onResponse(
                call: Call<User?>,
                response: Response<User?>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                liveData.value = response.body()
            }

            override fun onFailure(
                call: Call<User?>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure: ")
                liveData.value = User("Kennem", "Bright", 22)
            }
        })
        return liveData
    }
}