package com.nabha.telemedicine.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    phone: String,
    onOtpVerified: (isNewUser: Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var resendTimer by remember { mutableIntStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    val titleAlpha = remember { Animatable(0f) }
    val formAlpha  = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        titleAlpha.animateTo(1f, tween(500))
        formAlpha.animateTo(1f, tween(500, delayMillis = 200))
        focusRequesters[0].requestFocus()

        // Countdown timer
        while (resendTimer > 0) {
            delay(1000)
            resendTimer--
        }
        canResend = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SurfaceDark, SurfaceDark2, SurfaceDark)))
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            // Back button
            Row(modifier = Modifier.fillMaxWidth()) {
                NabhaIconButton(
                    icon    = Icons.Rounded.ArrowBackIos,
                    onClick = { /* navController.popBackStack() */ }
                )
            }

            Spacer(Modifier.height(32.dp))

            // OTP icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(NabhaGreen800.copy(alpha = 0.5f))
                    .alpha(titleAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Sms, null, tint = NabhaGreen400, modifier = Modifier.size(40.dp))
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Verify Phone",
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
                modifier   = Modifier.alpha(titleAlpha.value)
            )
            Text(
                "ਫੋਨ ਤਸਦੀਕ ਕਰੋ",
                fontSize  = 17.sp,
                color     = NabhaGreen300,
                fontWeight = FontWeight.Medium,
                modifier  = Modifier.alpha(titleAlpha.value)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text      = "We sent a 6-digit OTP to\n+91 $phone",
                fontSize  = 14.sp,
                color     = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier  = Modifier.alpha(titleAlpha.value)
            )

            Spacer(Modifier.height(48.dp))

            // OTP Input boxes
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .alpha(formAlpha.value),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                otpValues.forEachIndexed { index, value ->
                    OtpBox(
                        value          = value,
                        isFocused      = false,
                        focusRequester = focusRequesters[index],
                        onValueChange  = { newVal ->
                            if (newVal.length <= 1 && newVal.all { it.isDigit() }) {
                                otpValues[index] = newVal
                                if (newVal.isNotEmpty() && index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                                if (index == 5 && newVal.isNotEmpty()) {
                                    focusManager.clearFocus()
                                }
                            }
                        },
                        onKeyEvent     = { keyEvent ->
                            if (keyEvent.key == Key.Backspace &&
                                keyEvent.type == KeyEventType.KeyDown &&
                                value.isEmpty() && index > 0) {
                                otpValues[index - 1] = ""
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else false
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                Text(
                    text      = errorMessage,
                    color     = NabhaRed500,
                    fontSize  = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(36.dp))

            NabhaButton(
                text      = "Verify OTP",
                onClick   = {
                    isLoading = true
                    scope.launch {
                        delay(1500) // Simulate verification
                        isLoading = false
                        onOtpVerified(true) // isNewUser = true for demo
                    }
                },
                enabled   = otpValues.all { it.isNotEmpty() },
                isLoading = isLoading,
                modifier  = Modifier.alpha(formAlpha.value)
            )

            Spacer(Modifier.height(32.dp))

            // Resend OTP
            Row(
                modifier  = Modifier.alpha(formAlpha.value),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Didn't receive? ", color = TextTertiary, fontSize = 14.sp)
                if (canResend) {
                    Text(
                        text     = "Resend OTP",
                        color    = NabhaBlue400,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) {
                            resendTimer = 60
                            canResend   = false
                            scope.launch {
                                while (resendTimer > 0) {
                                    delay(1000)
                                    resendTimer--
                                }
                                canResend = true
                            }
                        }
                    )
                } else {
                    Text(
                        text  = "Resend in ${resendTimer}s",
                        color = TextTertiary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun OtpBox(
    value: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onKeyEvent: (KeyEvent) -> Boolean
) {
    val borderColor by animateColorAsState(
        targetValue   = if (value.isNotEmpty()) NabhaGreen400 else if (isFocused) NabhaBlue400 else OutlineDark,
        animationSpec = tween(200),
        label         = "otp_border"
    )

    Box(
        modifier         = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardDark2)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .focusRequester(focusRequester),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.text.BasicTextField(
            value         = value,
            onValueChange = onValueChange,
            textStyle     = TextStyle(
                color      = TextPrimary,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine      = true,
            modifier        = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .onKeyEvent { onKeyEvent(it) },
            cursorBrush     = androidx.compose.ui.graphics.SolidColor(NabhaBlue400)
        )
    }
}
