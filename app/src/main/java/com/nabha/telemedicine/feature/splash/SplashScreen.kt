package com.nabha.telemedicine.feature.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.theme.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateToLanguage: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val iconScale = remember { Animatable(0.3f) }
    val iconAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animate icon in — run scale animation in parallel with alpha
        coroutineScope {
            launch {
                iconScale.animateTo(
                    targetValue   = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness    = Spring.StiffnessMedium
                    )
                )
            }
            iconAlpha.animateTo(1f, tween(500))
        }
        delay(300)
        textAlpha.animateTo(1f, tween(600))
        delay(200)
        subtitleAlpha.animateTo(1f, tween(600))
        delay(1800)

        // Check auth state
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            onNavigateToHome()
        } else {
            onNavigateToLanguage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors  = listOf(NabhaBlue800, SurfaceDark),
                    radius  = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background decorative rings
        repeat(3) { index ->
            val ringAlpha by animateFloatAsState(
                targetValue   = 0.05f - (index * 0.015f),
                animationSpec = tween(1000),
                label         = "ring_$index"
            )
            Box(
                modifier = Modifier
                    .size((200 + index * 100).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(NabhaBlue400.copy(alpha = 0.08f), Color.Transparent)
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Government emblem styled icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(iconScale.value)
                    .alpha(iconAlpha.value)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(NabhaBlue500, NabhaBlue800)
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Rounded.HealthAndSafety,
                    contentDescription = null,
                    tint               = Color.White,
                    modifier           = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            // App name
            Text(
                text       = "Nabha Sehat",
                fontSize   = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = TextPrimary,
                modifier   = Modifier.alpha(textAlpha.value)
            )
            Text(
                text       = "ਨਾਭਾ ਸਿਹਤ",
                fontSize   = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color      = NabhaBlue300,
                modifier   = Modifier.alpha(textAlpha.value)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text      = "Punjab Government · Telemedicine Initiative",
                fontSize  = 13.sp,
                color     = TextTertiary,
                textAlign = TextAlign.Center,
                modifier  = Modifier
                    .alpha(subtitleAlpha.value)
                    .padding(horizontal = 32.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Serving 173 Villages",
                fontSize  = 12.sp,
                color     = NabhaSaffron400,
                fontWeight = FontWeight.Medium,
                modifier  = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(Modifier.height(60.dp))

            // Loading indicator
            val infiniteTransition = rememberInfiniteTransition(label = "loading")
            val loaderAlpha by infiniteTransition.animateFloat(
                initialValue  = 0.3f,
                targetValue   = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "loader"
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(subtitleAlpha.value)
            ) {
                repeat(3) { i ->
                    val dotAlpha by infiniteTransition.animateFloat(
                        initialValue  = 0.2f,
                        targetValue   = 1f,
                        animationSpec = infiniteRepeatable(
                            animation  = tween(600),
                            repeatMode = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(i * 150)
                        ),
                        label = "dot_$i"
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(8.dp)
                            .alpha(dotAlpha)
                            .background(
                                color = NabhaBlue400,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }
        }

        // Bottom tagline
        Column(
            modifier          = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(subtitleAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ਸਿਹਤ ਹੀ ਧਨ ਹੈ", fontSize = 14.sp, color = TextTertiary, fontWeight = FontWeight.Medium)
            Text("Health is Wealth", fontSize = 11.sp, color = TextTertiary.copy(alpha = 0.6f))
        }
    }
}
