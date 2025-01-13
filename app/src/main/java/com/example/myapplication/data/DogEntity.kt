package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val breed: String = "",
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)