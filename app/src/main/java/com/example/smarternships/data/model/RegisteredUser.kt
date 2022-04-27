package com.example.smarternships.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class RegisteredUser(
    val userId: String,
    val displayName: String
)