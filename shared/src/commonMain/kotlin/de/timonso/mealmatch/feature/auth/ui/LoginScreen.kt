package de.timonso.mealmatch.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import mealmatch.shared.generated.resources.ic_login
import mealmatch.shared.generated.resources.ic_mail
import mealmatch.shared.generated.resources.ic_person_add
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(viewModel: AuthViewModel, onRegisterClick: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login,
        onRegisterClick = onRegisterClick,
    )
}

@Composable
private fun LoginContent(
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val canLogin = !state.isLoading && state.email.isNotBlank() && state.password.isNotEmpty()

    AuthScaffold {
        AuthHeader()
        AuthCard {
            Text(
                "Willkommen zurück!",
                color = MealMatchColors.Ink,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Melde dich an und plane deine nächsten Mahlzeiten.",
                color = MealMatchColors.Muted,
                fontSize = 15.sp,
            )
            Spacer(Modifier.height(20.dp))

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
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {}) {
                    Text("Passwort vergessen?", color = MealMatchColors.Green, fontWeight = FontWeight.SemiBold)
                }
            }

            ErrorText(state.error)

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onLogin,
                enabled = canLogin || state.isLoading,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MealMatchColors.Green),
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                if (!state.isLoading) {
                    Icon(painterResource(Res.drawable.ic_login), contentDescription = null, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.size(10.dp))
                }
                LoadingLabel(state.isLoading, "Anmelden")
            }

            Row(
                Modifier.fillMaxWidth().padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(Modifier.weight(1f), color = MealMatchColors.Outline)
                Text("  oder  ", color = MealMatchColors.Muted)
                HorizontalDivider(Modifier.weight(1f), color = MealMatchColors.Outline)
            }

            OutlinedButton(
                onClick = onRegisterClick,
                enabled = !state.isLoading,
                shape = RoundedCornerShape(28.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MealMatchColors.Green),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Icon(
                    painterResource(Res.drawable.ic_person_add),
                    contentDescription = null,
                    tint = MealMatchColors.Green,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.size(10.dp))
                Text("Registrieren", color = MaterialTheme.colorScheme.onSurface, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(20.dp))
            FeatureRow()
        }
        AuthTagline()
    }
}
