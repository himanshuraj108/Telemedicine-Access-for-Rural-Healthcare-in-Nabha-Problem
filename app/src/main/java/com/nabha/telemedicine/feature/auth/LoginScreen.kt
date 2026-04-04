package com.nabha.telemedicine.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

// ─────────────────────────────────────────────────────────────────────────────
// FREE Authentication Strategy:
//   Firebase Email/Password auth → 100% FREE on Spark plan
//   No SMS costs, no API keys, no billing account needed
//   Works fully offline (cached auth state)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isNewUser by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    val auth = remember { FirebaseAuth.getInstance() }

    val contentAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) { contentAlpha.animateTo(1f, tween(600)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(NabhaBlue900, Color(0xFF040D1A), SurfaceDark)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(NabhaBlue700, NabhaGreen700))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.HealthAndSafety,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "ਨਾਭਾ ਸਿਹਤ",
                fontSize   = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = TextPrimary
            )
            Text(
                "Nabha Sehat",
                fontSize = 14.sp,
                color    = NabhaBlue300
            )
            Text(
                "Punjab Government Telemedicine",
                fontSize = 12.sp,
                color    = TextTertiary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(Modifier.height(40.dp))

            // Tab Toggle — Sign In / Register (single Row, each half is directly clickable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardDark2)
                    .border(1.dp, DividerDark, RoundedCornerShape(14.dp))
            ) {
                listOf("Sign In" to false, "Register" to true).forEach { (label, isRegister) ->
                    val selected = isNewUser == isRegister
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) NabhaBlue500.copy(0.2f) else Color.Transparent)
                            .clickable {
                                isNewUser    = isRegister
                                errorMessage = null
                            }
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            color      = if (selected) NabhaBlue300 else TextTertiary,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize   = 15.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Email Field
            FreeCostTextField(
                value         = email,
                onValueChange = { email = it.trim() },
                label         = "Email Address",
                hint          = "yourname@gmail.com",
                icon          = Icons.Rounded.Email,
                keyboardType  = KeyboardType.Email,
                imeAction     = ImeAction.Next
            )

            Spacer(Modifier.height(14.dp))

            // Password Field
            FreeCostTextField(
                value               = password,
                onValueChange       = { password = it },
                label               = "Password",
                hint                = "Min. 6 characters",
                icon                = Icons.Rounded.Lock,
                keyboardType        = KeyboardType.Password,
                imeAction           = ImeAction.Done,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon        = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            null,
                            tint = TextTertiary
                        )
                    }
                }
            )

            // Error Message
            AnimatedVisibility(visible = errorMessage != null) {
                Text(
                    errorMessage ?: "",
                    color     = NabhaRed400,
                    fontSize  = 13.sp,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(28.dp))

            // CTA Button
            NabhaButton(
                text      = if (isNewUser) "Create Account" else "Sign In",
                onClick   = {
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = "Please enter your email and password"
                        return@NabhaButton
                    }
                    if (password.length < 6) {
                        errorMessage = "Password must be at least 6 characters"
                        return@NabhaButton
                    }
                    isLoading    = true
                    errorMessage = null

                    val task = if (isNewUser) {
                        auth.createUserWithEmailAndPassword(email, password)
                    } else {
                        auth.signInWithEmailAndPassword(email, password)
                    }

                    task.addOnSuccessListener {
                        isLoading = false
                        if (isNewUser) {
                            navController.navigate(Screen.ProfileSetup.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }.addOnFailureListener { e ->
                        isLoading    = false
                        errorMessage = when {
                            e.message?.contains("email") == true -> "Invalid email format"
                            e.message?.contains("password") == true -> "Wrong password"
                            e.message?.contains("no user") == true -> "No account found. Register first."
                            e.message?.contains("already in use") == true -> "Email already registered. Sign In instead."
                            e.message?.contains("network") == true -> "Network error. Check internet connection."
                            else -> e.message ?: "Authentication failed"
                        }
                    }
                },
                enabled   = !isLoading,
                isLoading = isLoading,
                icon      = if (isNewUser) Icons.Rounded.PersonAdd else Icons.Rounded.Login
            )

            Spacer(Modifier.height(16.dp))

            // Forgot password
            if (!isNewUser) {
                TextButton(
                    onClick = {
                        if (email.isNotEmpty()) {
                            auth.sendPasswordResetEmail(email)
                            errorMessage = "Password reset email sent to $email"
                        } else {
                            errorMessage = "Enter your email above first"
                        }
                    }
                ) {
                    Text("Forgot Password?", color = NabhaBlue400, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// Simple text field composable reused for login form
@Composable
private fun FreeCostTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Column {
        Text(label, color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value             = value,
            onValueChange     = onValueChange,
            placeholder       = { Text(hint, color = TextTertiary, fontSize = 14.sp) },
            leadingIcon       = { Icon(icon, null, tint = NabhaBlue400, modifier = Modifier.size(20.dp)) },
            trailingIcon      = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions   = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            singleLine        = true,
            modifier          = Modifier.fillMaxWidth(),
            shape             = RoundedCornerShape(14.dp),
            colors            = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = NabhaBlue400,
                unfocusedBorderColor = DividerDark,
                focusedContainerColor   = CardDark2,
                unfocusedContainerColor = CardDark2,
                focusedTextColor  = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor       = NabhaBlue400
            )
        )
    }
}
