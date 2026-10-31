package de.timonso.mealmatch.user

import java.time.Instant
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val displayName: String,
    val emailVerified: Boolean,
    val createdAt: Instant,
)

fun AppUser.toResponse() = UserResponse(id, email, displayName, emailVerified, createdAt)
