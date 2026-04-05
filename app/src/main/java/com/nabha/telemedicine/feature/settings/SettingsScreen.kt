package com.nabha.telemedicine.feature.settings

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.NabhaTextField
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

// ── User profile state (in real app, from ViewModel/Firebase) ─────────────────
data class UserProfile(
    val name: String = "Sukhvir Singh",
    val village: String = "Village Bahman, Nabha",
    val phone: String = "+91-98765-43210",
    val bloodGroup: String = "A+",
    val aadhaarLinked: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    // ── State ──────────────────────────────────────────────────────────────────
    var profile by remember { mutableStateOf(UserProfile()) }
    var selectedLanguage by remember { mutableStateOf("Punjabi") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(true) }
    var offlineSyncEnabled by remember { mutableStateOf(true) }

    // Dialog visibility
    var showEditProfile by remember { mutableStateOf(false) }
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showAadhaar by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showHelp by remember { mutableStateOf(false) }
    var showContact by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showSignOutConfirm by remember { mutableStateOf(false) }

    // ── Dialogs ────────────────────────────────────────────────────────────────
    if (showEditProfile) {
        EditProfileDialog(
            profile  = profile,
            onSave   = { updated -> profile = updated; showEditProfile = false },
            onDismiss = { showEditProfile = false }
        )
    }
    if (showLanguagePicker) {
        LanguagePickerDialog(
            current   = selectedLanguage,
            onSelect  = { selectedLanguage = it; showLanguagePicker = false },
            onDismiss = { showLanguagePicker = false }
        )
    }
    if (showAadhaar) {
        AadhaarDialog(
            isLinked  = profile.aadhaarLinked,
            onLink    = { profile = profile.copy(aadhaarLinked = true); showAadhaar = false },
            onDismiss = { showAadhaar = false }
        )
    }
    if (showPrivacy) {
        PrivacyDialog(onDismiss = { showPrivacy = false })
    }
    if (showHelp) {
        HelpDialog(onDismiss = { showHelp = false })
    }
    if (showContact) {
        ContactDialog(onDismiss = { showContact = false })
    }
    if (showAbout) {
        AboutDialog(onDismiss = { showAbout = false })
    }
    if (showSignOutConfirm) {
        SignOutDialog(
            onConfirm = {
                showSignOutConfirm = false
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onDismiss = { showSignOutConfirm = false }
        )
    }

    // ── Main Layout ────────────────────────────────────────────────────────────
    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(NabhaBlue900.copy(0.4f), SurfaceDark)))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar — tap to edit profile
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(NabhaBlue800)
                            .border(2.dp, NabhaBlue400, CircleShape)
                            .clickable(MutableInteractionSource(), null) { showEditProfile = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Person, null, tint = NabhaBlue200, modifier = Modifier.size(38.dp))
                        // Camera overlay badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(NabhaBlue500)
                                .border(2.dp, SurfaceDark, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.CameraAlt, null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(profile.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 19.sp)
                        Text(profile.village, color = TextTertiary, fontSize = 13.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(NabhaGreen400))
                            Spacer(Modifier.width(4.dp))
                            Text("Active · Nabha District", color = NabhaGreen400, fontSize = 12.sp)
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // Edit button in header
                    NabhaIconButton(Icons.Rounded.Edit, onClick = { showEditProfile = true }, tint = NabhaBlue400)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 24.dp)
        ) {
            // ── Stats Card ──────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(NabhaBlue800.copy(0.6f), NabhaGreen900.copy(0.4f))))
                        .border(1.dp, DividerDark, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        ProfileStat(
                            value  = "6",
                            label  = "Appointments",
                            color  = NabhaBlue400,
                            onClick = { navController.navigate(Screen.Appointments.route) }
                        )
                        Divider(modifier = Modifier.height(40.dp).width(1.dp), color = DividerDark)
                        ProfileStat(
                            value  = "3",
                            label  = "Health Records",
                            color  = NabhaGreen400,
                            onClick = { navController.navigate(Screen.Records.route) }
                        )
                        Divider(modifier = Modifier.height(40.dp).width(1.dp), color = DividerDark)
                        ProfileStat(
                            value  = profile.bloodGroup,
                            label  = "Blood Group",
                            color  = NabhaSaffron400,
                            onClick = { showEditProfile = true }
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ── Language & Region ───────────────────────────────────────────────
            item { SettingsSectionTitle("Language & Region") }
            item {
                SettingsItem(
                    icon     = Icons.Rounded.Language,
                    title    = "App Language",
                    subtitle = selectedLanguage,
                    color    = NabhaBlue400,
                    onClick  = { showLanguagePicker = true }
                )
            }

            // ── Account ─────────────────────────────────────────────────────────
            item { SettingsSectionTitle("Account") }
            item {
                SettingsItem(Icons.Rounded.Person,       "Edit Profile",        "Update your personal info",       NabhaGreen400)  { showEditProfile = true }
                SettingsItem(Icons.Rounded.Security,     "Privacy & Security",  "Manage data and permissions",      NabhaBlue400)   { showPrivacy = true }
                SettingsItem(
                    icon     = Icons.Rounded.VerifiedUser,
                    title    = "Aadhaar Verification",
                    subtitle = if (profile.aadhaarLinked) "✓ Linked" else "Not linked",
                    color    = if (profile.aadhaarLinked) NabhaGreen400 else NabhaSaffron400,
                    onClick  = { showAadhaar = true }
                )
            }

            // ── Preferences ─────────────────────────────────────────────────────
            item { SettingsSectionTitle("Preferences") }
            item {
                SettingsToggleItem(
                    icon     = Icons.Rounded.Notifications,
                    title    = "Notifications",
                    subtitle = if (notificationsEnabled) "Reminders & updates enabled" else "Notifications disabled",
                    color    = NabhaSaffron400,
                    checked  = notificationsEnabled,
                    onToggle = { notificationsEnabled = it }
                )
                SettingsToggleItem(
                    icon     = Icons.Rounded.DarkMode,
                    title    = "Dark Mode",
                    subtitle = if (darkMode) "Currently enabled" else "Currently disabled",
                    color    = Color(0xFF8B5CF6),
                    checked  = darkMode,
                    onToggle = { darkMode = it }
                )
                SettingsToggleItem(
                    icon     = Icons.Rounded.CloudSync,
                    title    = "Offline Sync",
                    subtitle = if (offlineSyncEnabled) "Records synced for offline access" else "Sync disabled",
                    color    = NabhaGreen400,
                    checked  = offlineSyncEnabled,
                    onToggle = { offlineSyncEnabled = it }
                )
            }

            // ── Help & Support ──────────────────────────────────────────────────
            item { SettingsSectionTitle("Help & Support") }
            item {
                SettingsItem(Icons.Rounded.Help,           "Help & FAQ",      "Frequently asked questions", NabhaBlue400)   { showHelp = true }
                SettingsItem(Icons.Rounded.ContactSupport, "Contact Support", "Chat or call helpdesk",      NabhaGreen400)  { showContact = true }
                SettingsItem(Icons.Rounded.Info,           "About App",       "Version 1.0.0 · Punjab Govt",TextTertiary)   { showAbout = true }
            }

            // ── Sign Out ────────────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NabhaRed700.copy(0.1f))
                        .border(1.dp, NabhaRed700.copy(0.35f), RoundedCornerShape(16.dp))
                        .clickable(MutableInteractionSource(), null) { showSignOutConfirm = true }
                        .padding(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Logout, null, tint = NabhaRed500, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(14.dp))
                        Text("Sign Out", color = NabhaRed400, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Nabha Sehat · Punjab Government Telemedicine Initiative\nVersion 1.0.0",
                    color      = TextTertiary,
                    fontSize   = 11.sp,
                    modifier   = Modifier.fillMaxWidth(),
                    textAlign  = TextAlign.Center,
                    lineHeight = 17.sp
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Edit Profile Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun EditProfileDialog(profile: UserProfile, onSave: (UserProfile) -> Unit, onDismiss: () -> Unit) {
    var name       by remember { mutableStateOf(profile.name) }
    var village    by remember { mutableStateOf(profile.village) }
    var phone      by remember { mutableStateOf(profile.phone) }
    var bloodGroup by remember { mutableStateOf(profile.bloodGroup) }
    val bloodGroups = listOf("A+","A-","B+","B-","AB+","AB-","O+","O-")

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Edit Profile", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                Text("Update your personal information", color = TextTertiary, fontSize = 13.sp)
                Spacer(Modifier.height(20.dp))

                NabhaTextField(value = name, onValueChange = { name = it }, hint = "Full Name", leadingIcon = Icons.Rounded.Person)
                Spacer(Modifier.height(12.dp))
                NabhaTextField(value = village, onValueChange = { village = it }, hint = "Village / Address", leadingIcon = Icons.Rounded.LocationOn)
                Spacer(Modifier.height(12.dp))
                NabhaTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    hint = "Phone Number",
                    leadingIcon = Icons.Rounded.Phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(Modifier.height(16.dp))

                // Blood group selector
                Text("Blood Group", color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    bloodGroups.take(4).forEach { bg ->
                        BloodGroupChip(bg, bg == bloodGroup) { bloodGroup = bg }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    bloodGroups.drop(4).forEach { bg ->
                        BloodGroupChip(bg, bg == bloodGroup) { bloodGroup = bg }
                    }
                }

                Spacer(Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick  = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border   = androidx.compose.foundation.BorderStroke(1.dp, DividerDark)
                    ) { Text("Cancel") }
                    Button(
                        onClick  = { onSave(UserProfile(name.trim(), village.trim(), phone.trim(), bloodGroup, profile.aadhaarLinked)) },
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.buttonColors(containerColor = NabhaBlue500)
                    ) { Text("Save Changes", color = Color.White) }
                }
            }
        }
    }
}

@Composable
private fun BloodGroupChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) NabhaSaffron500.copy(0.2f) else CardDark)
            .border(1.dp, if (selected) NabhaSaffron400 else DividerDark, RoundedCornerShape(10.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = if (selected) NabhaSaffron300 else TextSecondary, fontSize = 13.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Language Picker Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun LanguagePickerDialog(current: String, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    val languages = listOf(
        Triple("Punjabi", "ਪੰਜਾਬੀ", "ਮੁੱਖ ਭਾਸ਼ਾ"),
        Triple("Hindi",   "हिंदी",   "National Language"),
        Triple("English", "English", "International Language")
    )
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Text("App Language", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                Text("ਭਾਸ਼ਾ ਚੁਣੋ · Select Language", color = TextTertiary, fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))

                languages.forEach { (key, nativeName, desc) ->
                    val selected = key == current
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (selected) NabhaBlue500.copy(0.12f) else Color.Transparent)
                            .border(1.dp, if (selected) NabhaBlue400.copy(0.5f) else Color.Transparent, RoundedCornerShape(14.dp))
                            .clickable(MutableInteractionSource(), null) { onSelect(key) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(nativeName, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 16.sp)
                            Text(desc, color = TextTertiary, fontSize = 12.sp)
                        }
                        if (selected) {
                            Icon(Icons.Rounded.CheckCircle, null, tint = NabhaBlue400, modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Aadhaar Verification Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun AadhaarDialog(isLinked: Boolean, onLink: () -> Unit, onDismiss: () -> Unit) {
    var aadhaarNumber by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otp by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                            .background(if (isLinked) NabhaGreen500.copy(0.15f) else NabhaSaffron500.copy(0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isLinked) Icons.Rounded.VerifiedUser else Icons.Rounded.Badge,
                            null,
                            tint     = if (isLinked) NabhaGreen400 else NabhaSaffron400,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("Aadhaar Verification", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                        Text(if (isLinked) "✓ Successfully linked" else "Link your Aadhaar card", color = TextTertiary, fontSize = 12.sp)
                    }
                }

                if (isLinked) {
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                            .background(NabhaGreen800.copy(0.3f)).padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.CheckCircle, null, tint = NabhaGreen400, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("Your Aadhaar is linked and verified", color = NabhaGreen300, fontSize = 13.sp)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NabhaBlue500)) {
                        Text("Close", color = Color.White)
                    }
                } else {
                    Spacer(Modifier.height(20.dp))
                    if (!otpSent) {
                        NabhaTextField(
                            value = aadhaarNumber,
                            onValueChange = { if (it.length <= 12) aadhaarNumber = it },
                            hint = "12-digit Aadhaar Number",
                            leadingIcon = Icons.Rounded.Badge,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), border = androidx.compose.foundation.BorderStroke(1.dp, DividerDark)) {
                                Text("Cancel", color = TextSecondary)
                            }
                            Button(
                                onClick  = { if (aadhaarNumber.length == 12) otpSent = true },
                                enabled  = aadhaarNumber.length == 12,
                                modifier = Modifier.weight(1f),
                                colors   = ButtonDefaults.buttonColors(containerColor = NabhaSaffron500)
                            ) { Text("Send OTP", color = Color.White) }
                        }
                    } else {
                        Text("OTP sent to registered mobile", color = NabhaGreen400, fontSize = 13.sp)
                        Spacer(Modifier.height(12.dp))
                        NabhaTextField(
                            value = otp,
                            onValueChange = { if (it.length <= 6) otp = it },
                            hint = "Enter 6-digit OTP",
                            leadingIcon = Icons.Rounded.Lock,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = { otpSent = false }, modifier = Modifier.weight(1f), border = androidx.compose.foundation.BorderStroke(1.dp, DividerDark)) {
                                Text("Back", color = TextSecondary)
                            }
                            Button(
                                onClick  = { if (otp.length == 6) onLink() },
                                enabled  = otp.length == 6,
                                modifier = Modifier.weight(1f),
                                colors   = ButtonDefaults.buttonColors(containerColor = NabhaSaffron500)
                            ) { Text("Verify", color = Color.White) }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Privacy & Security Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun PrivacyDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Text("Privacy & Security", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                Text("Your data is safe with us", color = TextTertiary, fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))

                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 320.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrivacyItem(Icons.Rounded.Lock,        "End-to-End Encryption", "All consultations and health data are fully encrypted", NabhaBlue400)
                    PrivacyItem(Icons.Rounded.Storage,     "Local Data Storage",    "Records are stored securely on your device and cloud", NabhaGreen400)
                    PrivacyItem(Icons.Rounded.VisibilityOff,"No Third-Party Sharing","Your health data is never shared with advertisers",   Color(0xFF8B5CF6))
                    PrivacyItem(Icons.Rounded.DeleteForever,"Delete My Data",        "Request full deletion of your account and data",      NabhaRed400)
                }

                Spacer(Modifier.height(20.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NabhaBlue500)) {
                    Text("I Understand", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun PrivacyItem(icon: ImageVector, title: String, desc: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(0.12f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 13.sp)
            Text(desc, color = TextTertiary, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Help & FAQ Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HelpDialog(onDismiss: () -> Unit) {
    val faqs = listOf(
        "How do I book a doctor appointment?" to "Go to 'Consult Doctor' → Find a doctor → Select a time slot → Confirm.",
        "Is my health data secure?" to "Yes. All data is encrypted end-to-end. Only you and your doctor can access it.",
        "Can I use the app offline?" to "Yes! Enable Offline Sync in Preferences. Your records will be available without internet.",
        "How do I get an emergency ambulance?" to "Tap 'Emergency SOS' on the home screen to call 108 immediately.",
        "What languages does the app support?" to "Punjabi (default), Hindi, and English. Change in Settings → Language.",
        "Is this app free?" to "Yes, Nabha Sehat is a free Punjab Government service for all citizens."
    )
    var expanded by remember { mutableStateOf<Int?>(null) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Text("Help & FAQ", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                Text("ਆਮ ਪੁੱਛੇ ਜਾਣ ਵਾਲੇ ਸਵਾਲ", color = TextTertiary, fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))

                Column(modifier = Modifier.verticalScroll(rememberScrollState()).heightIn(max = 380.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    faqs.forEachIndexed { index, (q, a) ->
                        val isOpen = expanded == index
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isOpen) NabhaBlue900.copy(0.5f) else CardDark)
                                .border(1.dp, if (isOpen) NabhaBlue700 else DividerDark, RoundedCornerShape(12.dp))
                                .clickable(MutableInteractionSource(), null) { expanded = if (isOpen) null else index }
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(q, fontWeight = FontWeight.Medium, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                Icon(if (isOpen) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, null, tint = TextTertiary, modifier = Modifier.size(18.dp))
                            }
                            AnimatedVisibility(visible = isOpen) {
                                Column {
                                    Spacer(Modifier.height(8.dp))
                                    Divider(color = DividerDark, thickness = 0.5.dp)
                                    Spacer(Modifier.height(8.dp))
                                    Text(a, color = TextSecondary, fontSize = 12.sp, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NabhaBlue500)) {
                    Text("Got it!", color = Color.White)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Contact Support Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun ContactDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Text("Contact Support", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                Text("We're here to help · ਅਸੀਂ ਮਦਦ ਕਰਨ ਲਈ ਤਿਆਰ ਹਾਂ", color = TextTertiary, fontSize = 13.sp)
                Spacer(Modifier.height(20.dp))

                ContactOption(Icons.Rounded.Phone,    "Call Helpdesk",   "+91-1800-XXX-XXXX",      "Mon–Sat, 9AM–6PM",  NabhaBlue400)
                Spacer(Modifier.height(12.dp))
                ContactOption(Icons.Rounded.Chat,     "WhatsApp Support", "+91-98765-43210",         "Available 24/7",    NabhaGreen400)
                Spacer(Modifier.height(12.dp))
                ContactOption(Icons.Rounded.Email,    "Email Us",         "help@nabhasehat.punjab.gov.in", "Response in 24hrs", NabhaSaffron400)
                Spacer(Modifier.height(12.dp))
                ContactOption(Icons.Rounded.LocationOn,"Visit Office",    "Civil Hospital, Nabha",   "Patiala District, Punjab", Color(0xFF8B5CF6))

                Spacer(Modifier.height(20.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NabhaBlue500)) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ContactOption(icon: ImageVector, title: String, value: String, note: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(0.07f))
            .border(1.dp, color.copy(0.2f), RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(0.15f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
            Text(value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(note, color = TextTertiary, fontSize = 11.sp)
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// About App Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, DividerDark, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(NabhaBlue700, NabhaGreen800)))
                        .border(2.dp, NabhaBlue400.copy(0.5f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.HealthAndSafety, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Nabha Sehat", fontWeight = FontWeight.ExtraBold, color = TextPrimary, fontSize = 22.sp)
                Text("ਨਾਭਾ ਸਿਹਤ", color = NabhaBlue300, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("Version 1.0.0", color = TextTertiary, fontSize = 12.sp)
                Spacer(Modifier.height(20.dp))

                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(CardDark).padding(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AboutRow("Initiative", "Punjab Government Telemedicine")
                        AboutRow("District",   "Nabha, Patiala, Punjab")
                        AboutRow("Coverage",   "173+ Villages")
                        AboutRow("Doctors",    "11+ Specialists Online")
                        AboutRow("Launched",   "2026")
                        AboutRow("License",    "Open Source · MIT")
                    }
                }

                Spacer(Modifier.height(20.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NabhaBlue500)) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun AboutRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextTertiary, fontSize = 12.sp)
        Text(value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Sign Out Confirmation Dialog
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun SignOutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CardDark2)
                .border(1.dp, NabhaRed700.copy(0.3f), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(NabhaRed700.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Logout, null, tint = NabhaRed500, modifier = Modifier.size(30.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Sign Out?", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "You will be logged out of Nabha Sehat. Your offline data will remain on this device.",
                    color = TextTertiary, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 19.sp
                )
                Spacer(Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick  = onDismiss,
                        modifier = Modifier.weight(1f),
                        border   = androidx.compose.foundation.BorderStroke(1.dp, DividerDark)
                    ) { Text("Cancel", color = TextSecondary) }
                    Button(
                        onClick  = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.buttonColors(containerColor = NabhaRed600)
                    ) { Text("Sign Out", color = Color.White, fontWeight = FontWeight.SemiBold) }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Shared Components
// ══════════════════════════════════════════════════════════════════════════════
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
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(0.12f)),
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(0.12f)),
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
                checkedThumbColor   = Color.White,
                checkedTrackColor   = color,
                uncheckedTrackColor = DividerDark
            )
        )
    }
    Divider(color = DividerDark, thickness = 0.5.dp)
}

@Composable
private fun ProfileStat(value: String, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(MutableInteractionSource(), null) { onClick() }
    ) {
        Text(value, fontWeight = FontWeight.ExtraBold, color = color, fontSize = 20.sp)
        Text(label, color = TextTertiary, fontSize = 11.sp)
    }
}

@Composable
private fun NabhaIconButton(icon: ImageVector, onClick: () -> Unit, tint: Color = TextTertiary) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(CardDark)
            .border(1.dp, DividerDark, CircleShape)
            .clickable(MutableInteractionSource(), null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
    }
}
