package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.VetEntity
import com.example.myapplication.domain.VetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VetViewModel @Inject constructor(
    private val repository: VetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(VetsState())
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            repository.getAllVets()
                .collect { vets ->
                    _state.value = state.value.copy(
                        vets = vets,
                        isLoading = false
                    )
                }
        }
        refreshVets()
    }

    fun refreshVets() {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(isLoading = true)
                repository.refreshVets()
            } catch (e: Exception) {
                _state.value = state.value.copy(
                    isLoading = false,
                    error = "Nie udało się odświeżyć danych"
                )
            }
        }
    }
}

data class VetsState(
    val vets: List<VetEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)