package de.timonso.mealmatch.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.timonso.mealmatch.core.network.ApiResult
import de.timonso.mealmatch.core.storage.TokenStore
import de.timonso.mealmatch.core.storage.Tokens
import de.timonso.mealmatch.feature.auth.data.AuthApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val api: AuthApi,
    private val tokens: TokenStore,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) = _state.update { it.copy(email = value, error = null) }

    fun onPasswordChange(value: String) = _state.update { it.copy(password = value, error = null) }

    fun onDisplayNameChange(value: String) =
        _state.update { it.copy(displayName = value.take(MAX_DISPLAY_NAME_LENGTH), error = null) }

    fun clearError() = _state.update { it.copy(error = null) }

    fun login() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val current = _state.value
            signIn(current.email.trim(), current.password)
        }
    }

    fun register() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val current = _state.value
            val email = current.email.trim()
            when (val result = api.register(email, current.password, current.displayName.trim())) {
                is ApiResult.Success -> signIn(email, current.password)
                is ApiResult.Failure -> _state.update {
                    it.copy(isLoading = false, error = AuthError.fromStatus(result.status))
                }
            }
        }
    }

    private suspend fun signIn(email: String, password: String) {
        when (val result = api.login(email, password)) {
            is ApiResult.Success -> {
                tokens.save(Tokens(result.value.accessToken, result.value.refreshToken))
                _state.update { it.copy(isLoading = false, password = "", user = result.value.user) }
            }
            is ApiResult.Failure -> _state.update {
                it.copy(isLoading = false, error = AuthError.fromStatus(result.status))
            }
        }
    }
}
