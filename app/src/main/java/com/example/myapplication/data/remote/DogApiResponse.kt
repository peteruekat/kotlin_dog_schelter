package com.example.myapplication.data.remote

// Klas - odpowiedź z API dla pojedynczego zdjęcia psa
data class DogApiResponse(
    val message: String,  // URL zdjęcia psa
    val status: String    // status odpowiedzi ("success" albo "error")
)

// Klasa -  odpowiedź z API dla listy ras
data class BreedListResponse(
    val message: Map<String, List<String>>,  // mapa ras
    val status: String
)