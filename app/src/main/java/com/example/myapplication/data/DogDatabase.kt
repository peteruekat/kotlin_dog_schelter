package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DogEntity::class, VetEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dogDao: DogDao
    abstract val vetDao: VetDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}