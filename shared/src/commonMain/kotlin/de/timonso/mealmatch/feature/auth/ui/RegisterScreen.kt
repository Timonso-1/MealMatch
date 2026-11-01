package de.timonso.mealmatch.feature.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.timonso.mealmatch.core.ui.theme.MealMatchColors
import mealmatch.shared.generated.resources.Res
import mealmatch.shared.generated.resources.ic_lock
import mealmatch.shared.generated.resources.ic_mail
import mealmatch.shared.generated.resources.ic_person
import mealmatch.shared.generated.resources.ic_person_add
import org.jetbrains.compose.resources.painterResource

@Composable
fun RegisterScreen(viewModel: AuthViewModel, onBackClick: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RegisterContent(
        state = state,
        onDisplayNameChange = viewModel::onDisplayNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRegister = viewModel::register,
        onBackClick = onBackClick,
    )
}

@Composable
private fun RegisterContent(
    state: AuthUiState,
    onDisplayNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onBackClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val canRegister = state.displayName.isNotBlank() &&
        '@' in state.email &&
        state.password.length >= MIN_PASSWORD_LENGTH

    AuthScaffold {
        AuthHeader()
        AuthCard {
            Text(
                "Konto erstellen",
                color = MealMatchColors.Ink,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Lege dein Konto an und plane gemeinsam.",
                color = MealMatchColors.Muted,
                fontSize = 15.sp,
            )
            Spacer(Modifier.height(20.dp))

            AuthTextField(
                value = state.displayName,
                onValueChange = onDisplayNameChange,
                label = "Anzeigename",
                icon = Res.drawable.ic_person,
            )
            Spacer(Modifier.height(12.dp))
            AuthTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = "E-Mail-Adresse",
                icon = Res.drawable.ic_mail,
                keyboardType = KeyboardType.Email,
            )
            Spacer(Modifier.height(12.dp))
            AuthTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = "Passwort",
                icon = Res.drawable.ic_lock,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                supportingText = "Mindestens $MIN_PASSWORD_LENGTH Zeichen",
            )

            ErrorText(state.error)

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onRegister,
                enabled = canRegister || state.isLoading,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MealMatchColors.Green),
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                if (!state.isLoading) {
                    Icon(painterResource(Res.drawable.ic_person_add), contentDescription = null, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.size(10.dp))
                }
                LoadingLabel(state.isLoading, "Konto erstellen")
            }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onBackClick,
                enabled = !state.isLoading,
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.5.dp, MealMatchColors.Green),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text(
                    "Zurück zur Anmeldung",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        AuthTagline()
    }
}
