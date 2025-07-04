package com.reyaz.core.common

sealed class Resource<T>(
    data: T? = null,
    message: String? = null
) {
    class Loading<T>(val message: String? = null) : Resource<T>()
    class Success<T>(val data: T? = null, val message: String? = null) : Resource<T>(data = data, message = message)
    class Error<T>(val message: String? = null) : Resource<T>(message = message)
}

// TODO: https://youtu.be/MiLN2vs2Oe0