package de.timonso.mealmatch.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class CreateUserRequest(
    @field:NotBlank
    @field:Email
    @field:Size(max = 254)
    val email: String,

    @field:NotBlank
    @field:Size(min = 12, max = 100)
    val password: String,

    @field:NotBlank
    @field:Size(max = 16)
    val displayName: String,
)

data class UserResponse(
    val id: UUID,
    val email: String,
    val displayName: String,
    val emailVerified: Boolean,
    val createdAt: Instant,
)

fun AppUser.toResponse() = UserResponse(id, email, displayName, emailVerified, createdAt)


