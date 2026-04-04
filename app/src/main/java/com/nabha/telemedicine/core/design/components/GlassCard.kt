package com.nabha.telemedicine.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nabha.telemedicine.core.design.theme.*

// ── Glassmorphism Card ──────────────────────────────────────────────────────
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderAlpha: Float = 0.15f,
    backgroundAlpha: Float = 0.12f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = backgroundAlpha + 0.05f),
                        Color.White.copy(alpha = backgroundAlpha)
                    )
                )
            )
            .border(
                width  = 1.dp,
                brush  = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = borderAlpha + 0.1f),
                        Color.White.copy(alpha = borderAlpha * 0.3f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}

// ── Dark Card ──────────────────────────────────────────────────────────────
@Composable
fun NabhaCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    backgroundColor: Color = CardDark,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width  = 1.dp,
                color  = DividerDark,
                shape  = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}

// ── Gradient Card ──────────────────────────────────────────────────────────
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.linearGradient(
        colors = listOf(NabhaBlue800, NabhaBlue900)
    ),
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush = gradient)
    ) {
        content()
    }
}

// ── Status Card ────────────────────────────────────────────────────────────
@Composable
fun StatusCard(
    modifier: Modifier = Modifier,
    color: Color = NabhaBlue500,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(color.copy(alpha = 0.12f))
            .border(
                width  = 1.dp,
                color  = color.copy(alpha = 0.3f),
                shape  = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}
