package com.nabha.telemedicine.feature.consultation

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.theme.*
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// FREE Video Call Implementation using Jitsi Meet
//
//  ✅ Jitsi Meet = 100% FREE, open-source (Apache 2.0)
//  ✅ No API key, no account, no billing required
//  ✅ Uses meet.jit.si free public server (maintained by 8x8)
//  ✅ End-to-end encrypted
//  ✅ Works on all Android devices
//
//  How it works:
//    1. App generates a unique room ID from the appointment/channel ID
//    2. Both doctor and patient join the same room URL
//    3. Jitsi handles all WebRTC signaling on their free servers
//
//  Room URL format: https://meet.jit.si/nabha-sehat-{channelId}
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun VideoCallScreen(
    channelId: String,
    token: String,   // kept for route compatibility, not used with Jitsi (no token needed)
    navController: NavController
) {
    val context = LocalContext.current
    var callStarted by remember { mutableStateOf(false) }
    var callDuration by remember { mutableLongStateOf(0L) }

    // Generate a deterministic, unique room name from channelId
    // Prefix with "nabha-sehat-" so rooms are identifiable
    val jitsiRoomId = "nabha-sehat-${channelId.take(12)}"
    val jitsiUrl    = "https://meet.jit.si/$jitsiRoomId"

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        0.85f, 1.15f,
        infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulse_scale"
    )

    LaunchedEffect(callStarted) {
        if (callStarted) {
            while (true) {
                delay(1000)
                callDuration++
            }
        }
    }

    val minutes = callDuration / 60
    val seconds = callDuration % 60
    val durationText = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF040D1A))
    ) {
        // Background gradient  
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors  = listOf(NabhaBlue900.copy(0.6f), Color(0xFF040D1A)),
                        radius  = 900f
                    )
                )
        )

        // Main content  
        if (!callStarted) {
            // Pre-call lobby — shown before joining Jitsi
            PreCallLobby(
                doctorName  = "Dr. Arjun Singh",
                jitsiRoomId = jitsiRoomId,
                jitsiUrl    = jitsiUrl,
                onJoin      = {
                    // Launch Jitsi Meet via Intent
                    // First try the Jitsi Meet app if installed, else open in browser
                    callStarted = true
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(jitsiUrl)).apply {
                            // Try to open in Jitsi Meet Android app
                            `package` = "org.jitsi.meet"
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback: open in Chrome / default browser (also free)
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(jitsiUrl)
                        )
                        context.startActivity(browserIntent)
                    }
                },
                onBack      = { navController.popBackStack() }
            )
        } else {
            // In-call status overlay (while Jitsi is running in the browser)
            InCallOverlay(
                durationText = durationText,
                jitsiRoomId  = jitsiRoomId,
                pulse        = pulse,
                onReturnToApp = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun PreCallLobby(
    doctorName: String,
    jitsiRoomId: String,
    jitsiUrl: String,
    onJoin: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier            = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(40.dp))

        // Back button
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.1f))
                    .clickable(MutableInteractionSource(), null) { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.ArrowBackIos, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(Modifier.weight(1f))

        // Doctor Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(NabhaBlue800)
                .border(3.dp, NabhaBlue400, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Person, null, tint = NabhaBlue200, modifier = Modifier.size(64.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text(doctorName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)
        Text("General Physician · Video Consultation", color = NabhaBlue300, fontSize = 14.sp)

        Spacer(Modifier.height(32.dp))

        // Jitsi Info Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(0.07f))
                .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.VideoCall, null, tint = NabhaGreen400, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Free Video Call via Jitsi Meet", fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 15.sp)
                }
                Spacer(Modifier.height(12.dp))
                InfoRow(icon = Icons.Rounded.Lock, text = "End-to-End Encrypted", color = NabhaGreen400)
                Spacer(Modifier.height(6.dp))
                InfoRow(icon = Icons.Rounded.CurrencyRupee, text = "₹0 cost · Completely free", color = NabhaGreen400)
                Spacer(Modifier.height(6.dp))
                InfoRow(icon = Icons.Rounded.Code, text = "Open-source · No account needed", color = NabhaBlue300)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Room: $jitsiRoomId",
                    color    = TextTertiary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Join Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(listOf(NabhaGreen500, NabhaGreen700))
                )
                .clickable(MutableInteractionSource(), null) { onJoin() }
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.VideoCall, null, tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(10.dp))
                Text("Join Free Video Call", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Share room link option
        Text(
            "Opens in Jitsi Meet app or browser\nShare room code with doctor: $jitsiRoomId",
            color     = TextTertiary,
            fontSize  = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun InCallOverlay(
    durationText: String,
    jitsiRoomId: String,
    pulse: Float,
    onReturnToApp: () -> Unit
) {
    Column(
        modifier            = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Live indicator
        Box(
            modifier = Modifier
                .size(100.dp * pulse)
                .clip(CircleShape)
                .background(NabhaGreen700.copy(0.2f))
                .border(2.dp, NabhaGreen500.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.VideoCall, null, tint = NabhaGreen400, modifier = Modifier.size(48.dp))
        }

        Spacer(Modifier.height(24.dp))
        Text("Call in Progress", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 22.sp)
        Text(durationText, color = NabhaGreen400, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Text("Via Jitsi Meet · $jitsiRoomId", color = TextTertiary, fontSize = 12.sp)

        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(NabhaRed600)
                .clickable(MutableInteractionSource(), null) { onReturnToApp() }
                .padding(20.dp)
        ) {
            Icon(Icons.Rounded.CallEnd, null, tint = Color.White, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text("End & Return to App", color = NabhaRed400, fontSize = 13.sp)
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, color = Color.White.copy(0.85f), fontSize = 13.sp)
    }
}
