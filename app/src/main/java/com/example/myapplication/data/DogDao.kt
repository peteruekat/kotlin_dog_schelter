package com.example.myapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {
    @Query("SELECT * FROM dogs")
    fun getAllDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs WHERE id = :id")
    suspend fun getDogById(id: String): DogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDog(dog: DogEntity)

    @Delete
    suspend fun deleteDog(dog: DogEntity)

    @Query("DELETE FROM dogs")
    suspend fun deleteAllDogs()
}