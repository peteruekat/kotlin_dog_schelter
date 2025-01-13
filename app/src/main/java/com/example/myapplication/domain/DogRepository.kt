package com.example.myapplication.domain

import com.example.myapplication.data.local.DogDao
import com.example.myapplication.data.local.DogEntity
import com.example.myapplication.data.remote.DogRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogRepository @Inject constructor(
    private val dogDao: DogDao,
    private val remoteDataSource: DogRemoteDataSource  // dodajemy nową zależność
) {
    fun getAllDogs(): Flow<List<DogEntity>> {
        return dogDao.getAllDogs()
    }

    suspend fun getDogById(id: String): DogEntity? {
        return dogDao.getDogById(id)
    }

    suspend fun insertDog(dog: DogEntity) {
        dogDao.insertDog(dog)
    }

    suspend fun deleteDog(dog: DogEntity) {
        dogDao.deleteDog(dog)
    }

    suspend fun fetchRandomDogImage(): String {
        return remoteDataSource.getRandomDogImage()
    }

    suspend fun fetchRandomDogImageByBreed(breed: String): String {
        return remoteDataSource.getRandomDogImageByBreed(breed)
    }

    suspend fun fetchAllBreeds(): Map<String, List<String>> {
        return remoteDataSource.getAllBreeds()
    }
}