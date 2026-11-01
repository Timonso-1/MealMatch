package de.timonso.mealmatch.feature.auth.ui

import de.timonso.mealmatch.feature.auth.data.UserDto

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    val error: AuthError? = null,
    val user: UserDto? = null,
)

enum class AuthError {
    InvalidCredentials,
    EmailTaken,
    InvalidInput,
    Network,
    Unknown;

    companion object {
        fun fromStatus(status: Int?): AuthError = when (status) {
            401 -> InvalidCredentials
            409 -> EmailTaken
            400 -> InvalidInput
            null -> Network
            else -> Unknown
        }
    }
}

const val MIN_PASSWORD_LENGTH = 10
const val MAX_DISPLAY_NAME_LENGTH = 16
