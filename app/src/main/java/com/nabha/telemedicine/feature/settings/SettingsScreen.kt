package com.nabha.telemedicine.feature.settings

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.theme.*

@Composable
fun SettingsScreen(navController: NavController) {
    var selectedLanguage by remember { mutableStateOf("Punjabi") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(true) }
    var offlineSyncEnabled by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(NabhaBlue900.copy(0.3f), SurfaceDark)))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    // User profile / avatar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(NabhaBlue800)
                                .border(2.dp, NabhaBlue400, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Person, null, tint = NabhaBlue200, modifier = Modifier.size(36.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text("Sukhvir Singh", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                            Text("Village Bahman, Nabha", color = TextTertiary, fontSize = 13.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(NabhaGreen400))
                                Spacer(Modifier.width(4.dp))
                                Text("Active · Nabha District", color = NabhaGreen400, fontSize = 12.sp)
                            }
                        }
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
            contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 60.dp)
        ) {
            // Health Stats Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(NabhaBlue800.copy(0.6f), NabhaGreen900.copy(0.4f)))
                        )
                        .border(1.dp, DividerDark, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStat("6", "Appointments", NabhaBlue400)
                        Divider(modifier = Modifier.height(40.dp).width(1.dp), color = DividerDark)
                        ProfileStat("3", "Health Records", NabhaGreen400)
                        Divider(modifier = Modifier.height(40.dp).width(1.dp), color = DividerDark)
                        ProfileStat("A+", "Blood Group", NabhaSaffron400)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // Language Section
            item { SettingsSectionTitle("Language & Region") }
            item {
                SettingsItem(
                    icon     = Icons.Rounded.Language,
                    title    = "App Language",
                    subtitle = selectedLanguage,
                    color    = NabhaBlue400,
                    onClick  = { /* cycle languages */ selectedLanguage = when (selectedLanguage) { "Punjabi" -> "Hindi"; "Hindi" -> "English"; else -> "Punjabi" } }
                )
            }

            // Account Section
            item { SettingsSectionTitle("Account") }
            item {
                SettingsItem(Icons.Rounded.Person, "Edit Profile", "Update your personal info", NabhaGreen400) {}
                SettingsItem(Icons.Rounded.Security, "Privacy & Security", "Manage data and permissions", NabhaBlue400) {}
                SettingsItem(Icons.Rounded.VerifiedUser, "Aadhaar Verification", "Not linked", NabhaSaffron400) {}
            }

            // Preferences
            item { SettingsSectionTitle("Preferences") }
            item {
                SettingsToggleItem(
                    icon     = Icons.Rounded.Notifications,
                    title    = "Notifications",
                    subtitle = "Appointment reminders & updates",
                    color    = NabhaSaffron400,
                    checked  = notificationsEnabled,
                    onToggle = { notificationsEnabled = it }
                )
            }
            item {
                SettingsToggleItem(
                    icon     = Icons.Rounded.DarkMode,
                    title    = "Dark Mode",
                    subtitle = "Currently enabled",
                    color    = Color(0xFF8B5CF6),
                    checked  = darkMode,
                    onToggle = { darkMode = it }
                )
            }
            item {
                SettingsToggleItem(
                    icon     = Icons.Rounded.CloudSync,
                    title    = "Offline Sync",
                    subtitle = "Sync records for offline access",
                    color    = NabhaGreen400,
                    checked  = offlineSyncEnabled,
                    onToggle = { offlineSyncEnabled = it }
                )
            }

            // Support
            item { SettingsSectionTitle("Help & Support") }
            item {
                SettingsItem(Icons.Rounded.Help, "Help & FAQ", "Frequently asked questions", NabhaBlue400) {}
                SettingsItem(Icons.Rounded.ContactSupport, "Contact Support", "Chat or call helpdesk", NabhaGreen400) {}
                SettingsItem(Icons.Rounded.Info, "About App", "Version 1.0.0 · Punjab Govt", TextTertiary) {}
            }

            // Logout
            item {
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NabhaRed700.copy(0.1f))
                        .border(1.dp, NabhaRed700.copy(0.3f), RoundedCornerShape(16.dp))
                        .clickable(MutableInteractionSource(), null) { /* logout */ }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier          = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Logout, null, tint = NabhaRed500, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(14.dp))
                        Text("Sign Out", color = NabhaRed400, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Nabha Sehat · Punjab Government Telemedicine Initiative\nVersion 1.0.0",
                    color     = TextTertiary,
                    fontSize  = 11.sp,
                    modifier  = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        title,
        fontWeight = FontWeight.SemiBold,
        color      = TextTertiary,
        fontSize   = 12.sp,
        modifier   = Modifier.padding(top = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsItem(icon: ImageVector, title: String, subtitle: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, color = TextPrimary, fontSize = 14.sp)
            Text(subtitle, color = TextTertiary, fontSize = 12.sp)
        }
        Icon(Icons.Rounded.ChevronRight, null, tint = TextTertiary, modifier = Modifier.size(18.dp))
    }
    Divider(color = DividerDark, thickness = 0.5.dp)
}

@Composable
private fun SettingsToggleItem(icon: ImageVector, title: String, subtitle: String, color: Color, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, color = TextPrimary, fontSize = 14.sp)
            Text(subtitle, color = TextTertiary, fontSize = 12.sp)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onToggle,
            colors          = SwitchDefaults.colors(
                checkedThumbColor  = Color.White,
                checkedTrackColor  = color,
                uncheckedTrackColor = DividerDark
            )
        )
    }
    Divider(color = DividerDark, thickness = 0.5.dp)
}

@Composable
private fun ProfileStat(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.ExtraBold, color = color, fontSize = 20.sp)
        Text(label, color = TextTertiary, fontSize = 11.sp)
    }
}
