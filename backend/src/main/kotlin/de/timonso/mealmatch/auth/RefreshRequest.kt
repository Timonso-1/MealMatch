package de.timonso.mealmatch.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RefreshRequest(
    @field:NotBlank @field:Size(max = 128) val refreshToken: String,
)
