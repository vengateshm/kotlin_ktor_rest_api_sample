package com.facts.model

import io.ktor.auth.*
import java.io.Serializable

data class User(
    val userId: Int,
    val email: String,
    val displayName: String,
    val password: String
) : Serializable, Principal
