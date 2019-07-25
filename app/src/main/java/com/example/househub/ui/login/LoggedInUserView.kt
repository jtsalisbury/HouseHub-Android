package com.example.househub.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val email: String,
    val admin: String,
    val createdDate: String,
    val userId: Int
)
