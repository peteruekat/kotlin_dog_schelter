package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.DogEntity
import com.example.myapplication.domain.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class DogViewModel @Inject constructor(
    private val repository: DogRepository
) : ViewModel() {

    private val _dogs = MutableStateFlow<List<DogEntity>>(emptyList())
    val dogs = _dogs.asStateFlow()

    // Dodajemy nowy stan dla ras psów
    private val _breeds = MutableStateFlow<List<String>>(emptyList())
    val breeds = _breeds.asStateFlow()

    init {
        getDogs()
        fetchBreeds() // Dodajemy wywołanie nowej metody
    }

    private fun getDogs() {
        repository.getAllDogs().onEach { dogList ->
            _dogs.value = dogList
        }.launchIn(viewModelScope)
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            try {
                val breedsMap = repository.fetchAllBreeds()
                _breeds.value = breedsMap.keys.toList().sorted()
            } catch (e: Exception) {
                println("Błąd przy pobieraniu ras: ${e.message}")
            }
        }
    }

    fun updateDogWithNewImage(dog: DogEntity, onImageUpdated: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Pobieramy nowe zdjęcie dla wybranej rasy
                val newImageUrl = if (dog.breed.isNotEmpty()) {
                    repository.fetchRandomDogImageByBreed(dog.breed)
                } else {
                    repository.fetchRandomDogImage()
                }

                // Aktualizujemy psa z nowym zdjęciem
                repository.insertDog(dog.copy(imageUrl = newImageUrl))

                // Informujemy UI o nowym URL
                onImageUpdated(newImageUrl)
            } catch (e: Exception) {
                println("Błąd przy aktualizacji zdjęcia: ${e.message}")
            }
        }
    }

    fun addDog(name: String, breed: String) {
        viewModelScope.launch {
            try {
                // Jeśli wybrano rasę, pobieramy zdjęcie dla tej rasy
                val imageUrl = if (breed.isNotEmpty()) {
                    repository.fetchRandomDogImageByBreed(breed)
                } else {
                    repository.fetchRandomDogImage()
                }

                repository.insertDog(
                    DogEntity(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        breed = breed, // Dodajemy rasę
                        imageUrl = imageUrl
                    )
                )
            } catch (e: Exception) {
                println("Błąd przy dodawaniu psa: ${e.message}")

                // W przypadku błędu, dodajemy psa bez zdjęcia
                repository.insertDog(
                    DogEntity(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        breed = breed, // Dodajemy rasę
                        imageUrl = ""
                    )
                )
            }
        }
    }

    fun toggleFavorite(dog: DogEntity) {
        viewModelScope.launch {
            repository.insertDog(
                dog.copy(isFavorite = !dog.isFavorite)
            )
        }
    }

    fun deleteDog(dog: DogEntity) {
        viewModelScope.launch {
            repository.deleteDog(dog)
        }
    }

    fun insertDog(dog: DogEntity) {
        viewModelScope.launch {
            repository.insertDog(dog)
        }
    }

    fun getDogById(id: String) {
        viewModelScope.launch {
            repository.getDogById(id)
        }
    }

    fun updateDog(dog: DogEntity) {
        viewModelScope.launch {
            repository.insertDog(dog)
        }
    }
}