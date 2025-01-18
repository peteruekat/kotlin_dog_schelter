package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded

@Entity(tableName = "vets")
data class VetEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    @Embedded
    val address: Address,
    @Embedded
    val company: Company
)

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    @Embedded
    val geo: Geo
)

data class Geo(
    val lat: String,
    val lng: String
)

data class Company(
    val companyName: String,
    val catchPhrase: String,
    val bs: String
)