package com.nabha.telemedicine.core.design.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.theme.*

// ── Primary Gradient Button ─────────────────────────────────────────────────
@Composable
fun NabhaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    fullWidth: Boolean = true,
    height: Dp = 56.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label         = "button_scale"
    )

    Box(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(height)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (enabled) Brush.horizontalGradient(
                    colors = listOf(NabhaBlue500, NabhaBlue600)
                ) else Brush.horizontalGradient(
                    colors = listOf(OutlineDark, OutlineDark)
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication        = null,
                enabled           = enabled && !isLoading
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier  = Modifier.size(24.dp),
                color     = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically,
                modifier              = Modifier.padding(horizontal = 24.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector         = icon,
                        contentDescription  = null,
                        tint                = Color.White,
                        modifier            = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text       = text,
                    color      = Color.White,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

// ── Outlined / Ghost Button ─────────────────────────────────────────────────
@Composable
fun NabhaOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    fullWidth: Boolean = true,
    height: Dp = 56.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label         = "outline_btn_scale"
    )

    Box(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(height)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(GlassBlue10)
            .clickable(
                interactionSource = interactionSource,
                indication        = null,
                enabled           = enabled
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically,
            modifier              = Modifier.padding(horizontal = 24.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector         = icon,
                    contentDescription  = null,
                    tint                = NabhaBlue400,
                    modifier            = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text       = text,
                color      = NabhaBlue400,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

// ── Emergency Red Button ────────────────────────────────────────────────────
@Composable
fun NabhaEmergencyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    fullWidth: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label         = "emergency_btn_scale"
    )

    Box(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(64.dp)
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(NabhaRed600, NabhaRed700)
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication        = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment     = Alignment.CenterVertically,
            modifier              = Modifier.padding(horizontal = 24.dp)
        ) {
            if (icon != null) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(10.dp))
            }
            Text(
                text       = text,
                color      = Color.White,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ── Icon Action Button ──────────────────────────────────────────────────────
@Composable
fun NabhaIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = NabhaBlue400,
    size: Dp = 48.dp,
    contentDescription: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
        label         = "icon_btn_scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(RoundedCornerShape(size / 2))
            .background(GlassWhite10)
            .clickable(
                interactionSource = interactionSource,
                indication        = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = tint,
            modifier           = Modifier.size(size * 0.45f)
        )
    }
}
