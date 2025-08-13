package com.reyaz.feature.rent.presentation.flatscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.rent.domain.model.Flat
import com.reyaz.feature.rent.domain.repository.FlatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlatScreenViewModel(
    private val flatRepository: FlatRepository
) : ViewModel() {

    //this is list of flats having mutable state flow property and private
    private val _flats = MutableStateFlow<List<Flat>>(emptyList())
    //it is exposed to ui where it will be collected
    val flats = _flats.asStateFlow()

    init {
        viewModelScope.launch {
            getAllFlats()
        }
    }

    suspend fun getAllFlats(){
        flatRepository.getAllFlats().collect {
            _flats.value = it //it correspond to list of flat
        }
    }
    /*
    * this view model will only be having the getall flat function*/

}