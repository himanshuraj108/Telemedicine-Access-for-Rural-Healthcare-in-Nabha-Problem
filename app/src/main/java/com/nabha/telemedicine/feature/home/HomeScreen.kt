package com.nabha.telemedicine.feature.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

data class QuickAction(
    val icon: ImageVector,
    val label: String,
    val labelPunjabi: String,
    val color: Color,
    val route: String
)

data class HealthTip(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val color: Color
)

val quickActions = listOf(
    QuickAction(Icons.Rounded.VideoCall,       "Consult Doctor",    "ਡਾਕਟਰ ਨਾਲ ਗੱਲ",    NabhaBlue500,     Screen.FindDoctor.route),
    QuickAction(Icons.Rounded.Psychology,      "Symptom Check",     "ਲੱਛਣ ਜਾਂਚ",         Color(0xFF8B5CF6), Screen.SymptomChecker.route),
    QuickAction(Icons.Rounded.FolderOpen,      "My Records",        "ਮੇਰੇ ਰਿਕਾਰਡ",       NabhaGreen500,    Screen.Records.route),
    QuickAction(Icons.Rounded.LocalPharmacy,   "Medicine Stock",    "ਦਵਾਈ ਸਟਾਕ",          NabhaSaffron500,  Screen.Pharmacy.route),
    QuickAction(Icons.Rounded.CalendarMonth,   "Appointments",      "ਮੁਲਾਕਾਤਾਂ",         NabhaBlue400,     Screen.Appointments.route),
    QuickAction(Icons.Rounded.Emergency,       "Emergency SOS",     "ਐਮਰਜੈਂਸੀ",           NabhaRed500,      Screen.Emergency.route)
)

val healthTips = listOf(
    HealthTip(Icons.Rounded.WaterDrop,   "Stay Hydrated",       "Drink 8 glasses of water daily, especially in summer.",   NabhaBlue400),
    HealthTip(Icons.Rounded.DirectionsWalk, "Daily Walk",       "30 minutes of walking reduces heart disease risk by 30%.", NabhaGreen400),
    HealthTip(Icons.Rounded.NoMeals,     "Balanced Diet",       "Include seasonal vegetables and dal in every meal.",        NabhaSaffron400),
    HealthTip(Icons.Rounded.Nightlight,  "Quality Sleep",       "7-8 hours of sleep boosts immunity and mental health.",    Color(0xFF8B5CF6))
)

@Composable
fun HomeScreen(navController: NavController) {
    val contentAlpha = remember { Animatable(0f) }
    var currentTipIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, tween(600))
        // Rotate health tips
        while (true) {
            kotlinx.coroutines.delay(5000)
            currentTipIndex = (currentTipIndex + 1) % healthTips.size
        }
    }

    Scaffold(
        containerColor = SurfaceDark
    ) { padding ->
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .alpha(contentAlpha.value),
            contentPadding      = PaddingValues(bottom = padding.calculateBottomPadding() + 16.dp)
        ) {
            // ── Header ───────────────────────────────────────────────────
            item {
                HomeHeader(navController = navController)
            }

            // ── Offline Banner ───────────────────────────────────────────
            item {
                OfflineBanner(isOffline = false) // Replace with actual network state
            }

            // ── Quick Actions ─────────────────────────────────────────────
            item {
                QuickActionsGrid(
                    actions        = quickActions,
                    onActionClick  = { action -> navController.navigate(action.route) }
                )
            }

            // ── Health Tip Card ───────────────────────────────────────────
            item {
                AnimatedHealthTipCard(
                    tip      = healthTips[currentTipIndex],
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // ── Upcoming Appointment ──────────────────────────────────────
            item {
                SectionTitle(title = "Upcoming Appointment", titlePunjabi = "ਆਉਣ ਵਾਲੀ ਮੁਲਾਕਾਤ")
                UpcomingAppointmentCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onClick  = { navController.navigate(Screen.Appointments.route) }
                )
            }

            // ── Available Doctors ─────────────────────────────────────────
            item {
                SectionTitle(
                    title         = "Available Doctors",
                    titlePunjabi  = "ਉਪਲਬਧ ਡਾਕਟਰ",
                    actionText    = "See All",
                    onAction      = { navController.navigate(Screen.FindDoctor.route) }
                )
            }
            item {
                LazyRow(
                    contentPadding      = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(mockDoctors) { doctor ->
                        DoctorMiniCard(doctor = doctor, onClick = {
                            navController.navigate(Screen.DoctorProfile.withId(doctor.id))
                        })
                    }
                }
            }

            // ── Stats ─────────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                NabhaStatsRow(modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}

@Composable
private fun HomeHeader(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(NabhaBlue900, SurfaceDark),
                    endY   = 400f
                )
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Column {
            Spacer(Modifier.height(40.dp))
            Row(
                modifier          = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text       = "ਸਤ ਸ੍ਰੀ ਅਕਾਲ!",
                        fontSize   = 14.sp,
                        color      = NabhaBlue300,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text       = "Good Morning",
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Text(
                        text  = "How are you feeling today?",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    NabhaIconButton(
                        icon    = Icons.Rounded.Notifications,
                        onClick = { navController.navigate(Screen.Notifications.route) }
                    )
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(NabhaBlue700)
                            .border(2.dp, NabhaBlue400, CircleShape)
                            .clickable(MutableInteractionSource(), null) {
                                navController.navigate(Screen.Settings.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Person, null, tint = NabhaBlue200, modifier = Modifier.size(24.dp))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Search bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardDark2)
                    .border(1.dp, DividerDark, RoundedCornerShape(14.dp))
                    .clickable(MutableInteractionSource(), null) { navController.navigate(Screen.FindDoctor.route) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Search, null, tint = TextTertiary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Search doctors, symptoms...", color = TextTertiary, fontSize = 14.sp)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun QuickActionsGrid(
    actions: List<QuickAction>,
    onActionClick: (QuickAction) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text(
            text       = "Quick Actions",
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary,
            modifier   = Modifier.padding(bottom = 14.dp)
        )
        val rows = actions.chunked(3)
        rows.forEach { rowActions ->
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowActions.forEach { action ->
                    QuickActionTile(
                        action    = action,
                        onClick   = { onActionClick(action) },
                        modifier  = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                // fill remaining space if less than 3
                repeat(3 - rowActions.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun QuickActionTile(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed) 0.93f else 1f,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "tile_scale"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(action.color.copy(alpha = 0.1f))
            .border(1.dp, action.color.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(action.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(action.icon, null, tint = action.color, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text       = action.label,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary,
            textAlign  = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines   = 2
        )
        Text(
            text    = action.labelPunjabi,
            fontSize = 10.sp,
            color   = TextTertiary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun AnimatedHealthTipCard(tip: HealthTip, modifier: Modifier) {
    AnimatedContent(
        targetState   = tip,
        transitionSpec = {
            fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 2 } togetherWith
            fadeOut(tween(300))
        },
        label         = "tip_transition",
        modifier      = modifier
    ) { currentTip ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(currentTip.color.copy(0.15f), currentTip.color.copy(0.05f))
                    )
                )
                .border(1.dp, currentTip.color.copy(0.3f), RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(currentTip.color.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(currentTip.icon, null, tint = currentTip.color, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Health Tip · ${currentTip.title}",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = currentTip.color
                )
                Text(
                    text      = currentTip.description,
                    fontSize  = 13.sp,
                    color     = TextSecondary,
                    maxLines  = 2,
                    overflow  = TextOverflow.Ellipsis,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun UpcomingAppointmentCard(modifier: Modifier, onClick: () -> Unit) {
    NabhaCard(modifier = modifier.clickable(MutableInteractionSource(), null) { onClick() }) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(NabhaGreen800.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.VideoCall, null, tint = NabhaGreen400, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Dr. Priya Sharma", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                Text("General Physician · Video Call", color = TextTertiary, fontSize = 12.sp)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.AccessTime, null, tint = NabhaBlue400, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Today, 11:00 AM", color = NabhaBlue300, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(NabhaGreen500.copy(0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("Join", color = NabhaGreen400, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    titlePunjabi: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 17.sp)
            Text(titlePunjabi, color = TextTertiary, fontSize = 12.sp)
        }
        if (actionText != null && onAction != null) {
            Text(
                text     = actionText,
                color    = NabhaBlue400,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(MutableInteractionSource(), null) { onAction() }
            )
        }
    }
}

data class MockDoctor(val id: String, val name: String, val specialty: String, val rating: Float, val color: Color, val isOnline: Boolean)
val mockDoctors = listOf(
    MockDoctor("d1", "Dr. Arjun Singh",   "General Physician",  4.8f, NabhaGreen500,    true),
    MockDoctor("d2", "Dr. Priya Sharma",  "Pediatrician",       4.9f, SpecialtyPediatrics, true),
    MockDoctor("d3", "Dr. Rajesh Kumar",  "Cardiologist",       4.7f, SpecialtyCardiology, false),
    MockDoctor("d4", "Dr. Neha Gupta",    "Gynecologist",       4.8f, SpecialtyGynecology, true)
)

@Composable
private fun DoctorMiniCard(doctor: MockDoctor, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .height(170.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(CardDark)
            .border(1.dp, DividerDark, RoundedCornerShape(18.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(doctor.color.copy(0.2f))
                    .border(2.dp, doctor.color.copy(0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, null, tint = doctor.color, modifier = Modifier.size(28.dp))
            }
            if (doctor.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(StatusOnline)
                        .border(2.dp, CardDark, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            doctor.name,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary,
            fontSize   = 12.sp,
            maxLines   = 2,
            textAlign  = androidx.compose.ui.text.style.TextAlign.Center,
            overflow   = TextOverflow.Ellipsis
        )
        Text(
            doctor.specialty,
            color    = TextTertiary,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Star, null, tint = NabhaSaffron400, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(3.dp))
            Text(doctor.rating.toString(), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun NabhaStatsRow(modifier: Modifier) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(Modifier.weight(1f), "173", "Villages Served", NabhaBlue400)
        StatCard(Modifier.weight(1f), "11",  "Doctors Online",  NabhaGreen400)
        StatCard(Modifier.weight(1f), "50+", "Pharmacies",      NabhaSaffron400)
    }
}

@Composable
private fun StatCard(modifier: Modifier, value: String, label: String, color: Color) {
    Column(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(0.08f))
            .border(1.dp, color.copy(0.2f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text       = value,
            fontWeight = FontWeight.ExtraBold,
            color      = color,
            fontSize   = 22.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = label,
            color     = TextTertiary,
            fontSize  = 11.sp,
            maxLines  = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}
