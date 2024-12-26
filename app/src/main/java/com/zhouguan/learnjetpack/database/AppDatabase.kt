package com.zhouguan.learnjetpack.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zhouguan.learnjetpack.dao.BookDao
import com.zhouguan.learnjetpack.dao.UserDao
import com.zhouguan.learnjetpack.entity.Book
import com.zhouguan.learnjetpack.entity.User

//@Database(version = 1, entities = [User::class])
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//
//    companion object {
//        private var instance: AppDatabase? = null
//
//        @Synchronized
//        fun getDatabase(context: Context): AppDatabase {
//            instance?.let {
//                return it
//            }
//
//            return Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "app_database"
//            )
//                .build()
//                .apply {
//                    instance = this
//                }
//        }
//    }
//}

@Database(version = 3, entities = [User::class, Book::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao

    companion object {
        // 升级数据库修改表结构
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table Book add column author text not null default 'unknown'")
            }
        }

        // 升级添加数据库表
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "create table Book (id integer primary key autoincrement not null, name text not null, pages integer not null)"
                )
            }
        }

        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                .apply {
                    instance = this
                }
        }
    }
}