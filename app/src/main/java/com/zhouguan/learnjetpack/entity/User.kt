package com.zhouguan.learnjetpack.entity;

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 实体类
 */
@Entity
data class User(var firstName: String, var lastName: String, var age: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}