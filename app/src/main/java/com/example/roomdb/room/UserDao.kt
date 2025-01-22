package com.example.roomdb.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    // Add or update the existing user data
    @Upsert
    fun insert(user: User) : Long

    @Delete
    fun delete(user: User)


    @Query(value = "SELECT * FROM user ")
    fun findAll(): Cursor
}