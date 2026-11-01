package de.timonso.mealmatch.feature.auth.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.timonso.mealmatch.core.ui.theme.MealMatchColors
import mealmatch.shared.generated.resources.Res
import mealmatch.shared.generated.resources.ic_calendar
import mealmatch.shared.generated.resources.ic_cart
import mealmatch.shared.generated.resources.ic_dice
import mealmatch.shared.generated.resources.ic_restaurant
import mealmatch.shared.generated.resources.ic_visibility
import mealmatch.shared.generated.resources.ic_visibility_off
import mealmatch.shared.generated.resources.logo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthScaffold(content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(MealMatchColors.Background)) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(MealMatchColors.GreenSoft, size.width * 0.55f, Offset(-size.width * 0.1f, size.height * 0.30f))
            drawCircle(MealMatchColors.GreenSoft, size.width * 0.50f, Offset(size.width * 1.05f, size.height * 0.20f))
            drawCircle(MealMatchColors.GreenSoft, size.width * 0.60f, Offset(size.width * 0.15f, size.height * 1.0f))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content,
        )
    }
}

@Composable
fun AuthHeader() {
    Image(painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(128.dp))
    Text(
        "MealMatch",
        color = MealMatchColors.GreenDark,
        fontSize = 40.sp,
        fontWeight = FontWeight.ExtraBold,
    )
    Text(
        "Gemeinsam planen. Einfach genießen.",
        color = MealMatchColors.Ink,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
    )
    Spacer(Modifier.height(20.dp))
}

@Composable
fun AuthCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: DrawableResource,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = {},
    supportingText: String? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        leadingIcon = { Icon(painterResource(icon), contentDescription = null) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        painterResource(if (passwordVisible) Res.drawable.ic_visibility_off else Res.drawable.ic_visibility),
                        contentDescription = if (passwordVisible) "Passwort verbergen" else "Passwort anzeigen",
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        supportingText = supportingText?.let { { Text(it) } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MealMatchColors.Green,
            unfocusedBorderColor = MealMatchColors.Outline,
            focusedLeadingIconColor = MealMatchColors.Green,
            unfocusedLeadingIconColor = MealMatchColors.Ink,
            unfocusedTrailingIconColor = MealMatchColors.Ink,
            unfocusedPlaceholderColor = MealMatchColors.Muted,
            focusedPlaceholderColor = MealMatchColors.Muted,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ErrorText(error: AuthError?) {
    if (error == null) return
    val message = when (error) {
        AuthError.InvalidCredentials -> "E-Mail oder Passwort ist falsch."
        AuthError.EmailTaken -> "Diese E-Mail-Adresse ist bereits registriert."
        AuthError.InvalidInput -> "Bitte prüfe deine Eingaben."
        AuthError.Network -> "Keine Verbindung zum Server."
        AuthError.Unknown -> "Ein unbekannter Fehler ist aufgetreten."
    }
    Text(
        message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        textAlign = TextAlign.Start,
    )
}

@Composable
fun LoadingLabel(isLoading: Boolean, text: String) {
    if (isLoading) {
        CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
    } else {
        Text(text, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun FeatureRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Feature(Res.drawable.ic_dice, "Zufällige\nGerichte")
        Feature(Res.drawable.ic_restaurant, "Eigene Gerichte\nvorschlagen")
        Feature(Res.drawable.ic_cart, "Einkaufslisten\nerstellen")
        Feature(Res.drawable.ic_calendar, "Essensplan\nfür die Woche")
    }
}

@Composable
private fun Feature(icon: DrawableResource, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(52.dp).clip(CircleShape).background(MealMatchColors.GreenTint),
            contentAlignment = Alignment.Center,
        ) {
            Icon(painterResource(icon), contentDescription = null, tint = MealMatchColors.Green)
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 11.sp, lineHeight = 14.sp, textAlign = TextAlign.Center, color = MealMatchColors.Ink)
    }
}

@Composable
fun AuthTagline() {
    Spacer(Modifier.height(24.dp))
    Text(
        "Weniger Planen.\nMehr Genießen.",
        color = MealMatchColors.Ink,
        fontSize = 22.sp,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(16.dp))
}
