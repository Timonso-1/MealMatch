package de.timonso.mealmatch.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank
    @field:Email
    @field:Size(max = 254)
    val email: String,

    @field:NotBlank
    @field:Size(min = 10, max = 128)
    val password: String,

    @field:NotBlank
    @field:Size(max = 16)
    val displayName: String,
)
