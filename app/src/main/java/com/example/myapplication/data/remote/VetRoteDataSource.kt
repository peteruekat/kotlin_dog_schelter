package com.example.myapplication.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VetRemoteDataSource @Inject constructor(
    private val api: VetApi
) {
    suspend fun getVets(): List<VetResponse> {
        return api.getVets()
    }

    fun mapToVetEntity(response: VetResponse) = com.example.myapplication.data.local.VetEntity(
        id = response.id,
        name = response.name,
        username = response.username,
        email = response.email,
        phone = response.phone,
        website = response.website,
        address = com.example.myapplication.data.local.Address(
            street = response.address.street,
            suite = response.address.suite,
            city = response.address.city,
            zipcode = response.address.zipcode,
            geo = com.example.myapplication.data.local.Geo(
                lat = response.address.geo.lat,
                lng = response.address.geo.lng
            )
        ),
        company = com.example.myapplication.data.local.Company(
            companyName = response.company.name,
            catchPhrase = response.company.catchPhrase,
            bs = response.company.bs
        )
    )
}