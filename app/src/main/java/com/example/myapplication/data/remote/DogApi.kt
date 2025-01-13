package com.example.myapplication.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    // Pobiera listę wszystkich ras psów
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): BreedListResponse

    // Pobiera losowe zdjęcie psa konkretnej rasy
    @GET("breed/{breed}/images/random")
    suspend fun getRandomDogImageByBreed(
        @Path("breed") breed: String
    ): DogApiResponse

    // Pobiera losowe zdjęcie dowolnego psa
    @GET("breeds/image/random")
    suspend fun getRandomDogImage(): DogApiResponse

    companion object {
        // Bazowy URL API
        const val BASE_URL = "https://dog.ceo/api/"
    }
}