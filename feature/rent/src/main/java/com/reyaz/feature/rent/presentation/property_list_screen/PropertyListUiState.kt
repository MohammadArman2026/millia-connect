package com.reyaz.feature.rent.presentation.property_list_screen

sealed class PropertyListUiState<out T> {
    object Loading : PropertyListUiState<Nothing>()
    data class Success<T>(val data: T) : PropertyListUiState<T>()
    data class Error(val message: String) : PropertyListUiState<Nothing>()
}

//It lets you represent a restricted set of possible types (subclasses).
//All subclasses of a sealed class must be defined in the same file →
// this makes the compiler know all possible cases.
//This is super useful for representing states, results, or events in Android apps.