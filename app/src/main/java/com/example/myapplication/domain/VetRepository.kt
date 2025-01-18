package com.example.myapplication.domain

import com.example.myapplication.data.local.VetDao
import com.example.myapplication.data.local.VetEntity
import com.example.myapplication.data.remote.VetRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VetRepository @Inject constructor(
    private val vetDao: VetDao,
    private val remoteDataSource: VetRemoteDataSource
) {

    fun getAllVets(): Flow<List<VetEntity>> {
        return vetDao.getAllVets()
    }


    suspend fun refreshVets() {
        try {
            val remoteVets = remoteDataSource.getVets()
            val vetEntities = remoteVets.map { remoteDataSource.mapToVetEntity(it) }
            vetDao.insertVets(vetEntities)
        } catch (e: Exception) {

        }
    }
}