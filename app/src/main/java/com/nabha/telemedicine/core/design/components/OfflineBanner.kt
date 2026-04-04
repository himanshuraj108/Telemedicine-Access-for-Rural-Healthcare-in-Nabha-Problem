package com.nabha.telemedicine.core.design.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.theme.NabhaRed600
import com.nabha.telemedicine.core.design.theme.NabhaRed700

@Composable
fun OfflineBanner(
    isOffline: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible           = isOffline,
        enter             = slideInVertically(tween(400)) { -it } + fadeIn(tween(400)),
        exit              = slideOutVertically(tween(400)) { -it } + fadeOut(tween(400)),
        modifier          = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NabhaRed700)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector        = Icons.Rounded.WifiOff,
                contentDescription = null,
                tint               = Color.White,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text       = "You are offline — showing cached data",
                color      = Color.White,
                fontSize   = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
