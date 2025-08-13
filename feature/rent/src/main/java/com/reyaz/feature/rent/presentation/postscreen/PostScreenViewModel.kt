package com.reyaz.feature.rent.presentation.postscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.rent.domain.model.Flat
import com.reyaz.feature.rent.domain.repository.FlatRepository
import kotlinx.coroutines.launch

class PostScreenViewModel(
    private val flatRepository: FlatRepository
): ViewModel() {

    fun addFlat(flat: Flat){
        viewModelScope.launch {
            flatRepository.addFlat(flat)
        }
    }

}