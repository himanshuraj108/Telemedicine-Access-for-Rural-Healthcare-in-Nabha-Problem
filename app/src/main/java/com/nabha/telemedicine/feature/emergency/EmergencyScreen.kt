package com.nabha.telemedicine.feature.emergency

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*

data class EmergencyContact(val name: String, val number: String, val description: String, val icon: ImageVector, val color: Color)

val emergencyContacts = listOf(
    EmergencyContact("Ambulance",              "108",           "Free emergency ambulance service",         Icons.Rounded.LocalHospital, NabhaRed500),
    EmergencyContact("Police",                 "100",           "Law enforcement and distress",              Icons.Rounded.LocalPolice,   NabhaBlue400),
    EmergencyContact("Fire Brigade",           "101",           "Fire emergency and rescue",                 Icons.Rounded.Fireplace,     NabhaSaffron500),
    EmergencyContact("National Emergency",     "112",           "All emergency services combined",           Icons.Rounded.Emergency,     NabhaRed600),
    EmergencyContact("Nabha Civil Hospital",   "01765-220022",  "Nabha District Hospital",                  Icons.Rounded.LocalHospital, NabhaGreen400),
    EmergencyContact("Women Helpline",         "1091",          "Women emergency helpline",                  Icons.Rounded.Woman,         SpecialtyGynecology),
    EmergencyContact("Child Helpline",         "1098",          "Child emergency services",                  Icons.Rounded.ChildCare,     SpecialtyPediatrics)
)

@Composable
fun EmergencyScreen(navController: NavController) {
    val infiniteTransition = rememberInfiniteTransition(label = "sos")
    val pulseScale by infiniteTransition.animateFloat(
        0.9f, 1.1f,
        infiniteRepeatable(tween(800, easing = EaseInOut), RepeatMode.Reverse),
        label = "sos_pulse"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        0.6f, 1f,
        infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "sos_alpha"
    )

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NabhaRed700.copy(0.2f))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NabhaIconButton(Icons.Rounded.ArrowBackIos, { navController.popBackStack() })
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Emergency SOS", fontWeight = FontWeight.Bold, color = NabhaRed400, fontSize = 18.sp)
                        Text("ਐਮਰਜੈਂਸੀ · Call for Help", color = TextTertiary, fontSize = 12.sp)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Big SOS Button
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier            = Modifier.fillMaxWidth()
                ) {
                    // Pulsing rings
                    Box(contentAlignment = Alignment.Center) {
                        // Outer ring
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(NabhaRed600.copy(pulseAlpha * 0.15f))
                                .border(1.dp, NabhaRed600.copy(pulseAlpha * 0.3f), CircleShape)
                        )
                        // Inner ring
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .background(NabhaRed600.copy(0.2f))
                                .border(2.dp, NabhaRed500.copy(0.5f), CircleShape)
                        )
                        // SOS button
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(listOf(NabhaRed600, NabhaRed700))
                                )
                                .clickable(MutableInteractionSource(), null) { /* call 108 */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("SOS", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 28.sp)
                                Text("108", color = Color.White.copy(0.85f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Tap SOS to call Ambulance", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 16.sp)
                    Text("ਐਂਬੂਲੈਂਸ ਲਈ SOS ਦਬਾਓ", color = TextTertiary, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Your location will be shared automatically with emergency services",
                        color     = TextTertiary,
                        fontSize  = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier  = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }

            // Nearest Hospital
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(NabhaGreen800.copy(0.25f))
                        .border(1.dp, NabhaGreen700, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.LocalHospital, null, tint = NabhaGreen400, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nearest Hospital", color = NabhaGreen400, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text("Nabha Civil Hospital", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                            Text("2.4 km away · Emergency open 24/7", color = TextSecondary, fontSize = 12.sp)
                        }
                        Icon(Icons.Rounded.Navigation, null, tint = NabhaBlue400, modifier = Modifier.size(24.dp))
                    }
                }
            }

            item {
                Text(
                    "Emergency Contacts",
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    fontSize   = 17.sp
                )
            }

            items(emergencyContacts) { contact ->
                EmergencyContactCard(contact = contact)
            }
        }
    }
}

@Composable
private fun EmergencyContactCard(contact: EmergencyContact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(contact.color.copy(0.06f))
            .border(1.dp, contact.color.copy(0.2f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(contact.color.copy(0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(contact.icon, null, tint = contact.color, modifier = Modifier.size(24.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(contact.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
            Text(contact.description, color = TextTertiary, fontSize = 11.sp)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(contact.color)
                .clickable(MutableInteractionSource(), null) { /* dial */ }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(contact.number, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
