package de.timonso.mealmatch.auth

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
