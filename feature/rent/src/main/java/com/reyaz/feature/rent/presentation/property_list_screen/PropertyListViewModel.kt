package com.reyaz.feature.rent.presentation.property_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PropertyListViewModel(
    private val propertyRepository: PropertyRepository
) : ViewModel() {
    //this class will handle the fetching property list from firebase

    private val _propertiesState =
        MutableStateFlow<PropertyListUiState<List<Property>>>(PropertyListUiState.Loading)

    //this is private variable ,ensuring data flow from up to down
    val propertiesState = _propertiesState.asStateFlow()

    init {
        //fetch the data when object of viewmodel is created
        viewModelScope.launch {
            getAllProperty()
        }
    }

    //once this function is private set it will not be called from outside this class
    private suspend fun getAllProperty() {
        propertyRepository
            .getAllProperty()
            .onStart {
                _propertiesState.value = PropertyListUiState.Loading
                //on starting setting value loading
            }
            .catch { e ->//will catch if there is any error // _
                //here in future implement event by channeling to ui
                _propertiesState.value = PropertyListUiState.Error(e.message ?: "empty list")
                Log.d("error", e.message.toString())
            }
            .collect {
                _propertiesState.value = PropertyListUiState.Success(it)
            }
    }

}