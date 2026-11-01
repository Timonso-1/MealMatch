package de.timonso.mealmatch

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.timonso.mealmatch.core.network.HttpClientFactory
import de.timonso.mealmatch.core.storage.InMemoryTokenStore
import de.timonso.mealmatch.core.ui.theme.MealMatchTheme
import de.timonso.mealmatch.feature.auth.data.AuthApi
import de.timonso.mealmatch.feature.auth.ui.AuthViewModel
import de.timonso.mealmatch.feature.auth.ui.LoginScreen
import de.timonso.mealmatch.feature.auth.ui.RegisterScreen

@Composable
fun App() {
    MealMatchTheme {
        val viewModel = viewModel {
            AuthViewModel(AuthApi(HttpClientFactory.create()), InMemoryTokenStore())
        }
        val state by viewModel.state.collectAsStateWithLifecycle()
        var showRegister by remember { mutableStateOf(false) }
        val user = state.user

        when {
            user != null -> Text("Hallo ${user.displayName}")
            showRegister -> RegisterScreen(
                viewModel = viewModel,
                onBackClick = {
                    viewModel.clearError()
                    showRegister = false
                },
            )
            else -> LoginScreen(
                viewModel = viewModel,
                onRegisterClick = {
                    viewModel.clearError()
                    showRegister = true
                },
            )
        }
    }
}
