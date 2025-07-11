package com.reyaz.feature.portal.domain.model

data class ConnectRequest(
    val username: String,
    val password: String,
    val autoConnect: Boolean
)