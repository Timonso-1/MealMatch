package de.timonso.mealmatch.auth

import de.timonso.mealmatch.user.UserResponse

data class LoginResponse(
    val user: UserResponse,
    val accessToken: String,
    val refreshToken: String,
)
