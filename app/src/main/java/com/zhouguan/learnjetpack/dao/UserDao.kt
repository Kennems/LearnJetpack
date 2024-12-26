package com.zhouguan.learnjetpack.dao;

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zhouguan.learnjetpack.entity.User

/**
 * 内部根据业务需求对各种数据库操作进行封装
 */
@Dao
interface UserDao {
    // 增
    @Insert
    fun insertUser(user: User): Long

    // 改
    @Update
    fun updateUser(newUser: User)

    // 查
    @Query("select * from User")
    fun loadAllUsers(): List<User>

    @Query("select * from User where age > :age")
    fun loadUsersOlderThan(age: Int): List<User>

    // 删
    @Delete
    fun deleteUser(user: User)

    // 删
    @Query("delete from User where lastName = :lastName")
    fun deleteUserByLastName(lastName: String): Int
}
