package com.example.myapplication.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogRemoteDataSource @Inject constructor(
    private val api: DogApi  // Hilt wstrzyknie nam implementację API
) {
    // Pobiera URL losowego zdjęcia psa
    suspend fun getRandomDogImage(): String {
        return api.getRandomDogImage().message
    }

    // Pobiera URL losowego zdjęcia psa konkretnej rasy
    suspend fun getRandomDogImageByBreed(breed: String): String {
        return api.getRandomDogImageByBreed(breed).message
    }

    // Pobiera mapę wszystkich ras psów
    suspend fun getAllBreeds(): Map<String, List<String>> {
        return api.getAllBreeds().message
    }
}