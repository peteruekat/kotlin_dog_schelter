package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DogEntity::class],
    version = 1
)
abstract class DogDatabase : RoomDatabase() {
    abstract val dogDao: DogDao

    companion object {
        const val DATABASE_NAME = "dogs_db"
    }
}