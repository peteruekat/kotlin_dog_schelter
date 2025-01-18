package com.example.myapplication.data.remote

import retrofit2.http.GET

interface VetApi {
    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("users")
    suspend fun getVets(): List<VetResponse>
}

data class VetResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val address: AddressResponse,
    val company: CompanyResponse
)

data class AddressResponse(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GeoResponse
)

data class GeoResponse(
    val lat: String,
    val lng: String
)

data class CompanyResponse(
    val name: String,
    val catchPhrase: String,
    val bs: String
)