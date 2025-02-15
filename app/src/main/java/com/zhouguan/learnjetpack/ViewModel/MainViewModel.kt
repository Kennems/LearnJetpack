package com.zhouguan.learnjetpack.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.zhouguan.learnjetpack.entity.Repository
import com.zhouguan.learnjetpack.entity.User

//class MainViewModel(countReserved: Int) : ViewModel() {
//    val counter = MutableLiveData<Int>()
//
//    init {
//        counter.value = countReserved
//    }
//
//    fun plusOne() {
//        val count = counter.value ?: 0
//        counter.value = count + 1
//    }
//
//    fun clear() {
//        counter.value = 0
//    }
//}
class MainViewModel(countReserved: Int) : ViewModel() {
    val userLiveData = MutableLiveData<User>()
    private val userIdLiveData = MutableLiveData<String>()

    /**
     * map() 方法就是专门用于解决这种问题的，它可以将User类型的LiveData自由地转型成任意
     * 其他类型的LiveData
     *
     * 当userLiveData的数据发生变化时，map()方法
     * 会监听到变化并执行转换函数中的逻辑，然后再将转换之后的数据通知给userName的观察者
     */
    val userName: LiveData<String> = userLiveData.map { user ->
        "${user.firstName} ${user.lastName}"
    }

    /**
     * switchMap的使用场景：
     *  如果ViewModel中的某个LiveData对象是调用另外的方法获取的，那么我们就可以借助
     * switchMap()方法，将这个LiveData对象转换成另外一个可观察的LiveData对象
     */
    val user: LiveData<User> = userIdLiveData.switchMap { userId ->
        Repository.getUser(userId)
    }

    val count: LiveData<Int>
        get() = _count

    val _count = MutableLiveData<Int>()

    init {
        _count.value = countReserved
    }

    fun plusOne() {
        val count = _count.value ?: 0
        _count.value = count + 1
    }

    fun clear() {
        _count.value = 0
    }

//    fun getUser(userId: String): LiveData<User> {
//        return Repository.getUser(userId)
//    }

    fun getUser(userId: String) {
        userIdLiveData.value = userId
    }
}