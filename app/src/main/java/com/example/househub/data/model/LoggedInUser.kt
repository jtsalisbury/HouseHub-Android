package com.example.househub.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val email: String,
    val admin: String,
    val createdDate: String,
    val userId: Int
)


