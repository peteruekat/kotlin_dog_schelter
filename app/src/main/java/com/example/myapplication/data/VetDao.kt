package com.example.myapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VetDao {
    @Query("SELECT * FROM vets")
    fun getAllVets(): Flow<List<VetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVets(vets: List<VetEntity>)

    @Query("DELETE FROM vets")
    suspend fun deleteAllVets()
}