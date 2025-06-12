package com.reyaz.feature.portal.domain.model

import org.eclipse.jetty.util.security.Password

data class ConnectRequest(
    val username: String,
    val password: String,
    val autoConnect: Boolean
)