package com.nabha.telemedicine.feature.appointments

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

data class AppointmentUi(val id: String, val doctorName: String, val specialty: String, val time: String, val date: String, val type: String, val status: String, val statusColor: Color, val fee: Int, val color: Color)

val mockAppointments = listOf(
    AppointmentUi("a1", "Dr. Priya Sharma",   "Pediatrician",    "11:00 AM", "Today · Apr 5",      "Video Call",  "Confirmed",  NabhaGreen400,    150, SpecialtyPediatrics),
    AppointmentUi("a2", "Dr. Arjun Singh",    "General Physician","02:30 PM", "Tomorrow · Apr 6",  "Video Call",  "Pending",    NabhaSaffron400,  200, NabhaGreen500),
    AppointmentUi("a3", "Dr. Rajesh Kumar",   "Cardiologist",    "10:00 AM", "Apr 10",             "Audio Call",  "Confirmed",  NabhaGreen400,    500, SpecialtyCardiology),
    AppointmentUi("a4", "Dr. Neha Gupta",     "Gynecologist",    "09:30 AM", "Mar 28",             "Video Call",  "Completed",  NabhaBlue400,     300, SpecialtyGynecology),
    AppointmentUi("a5", "Dr. Arjun Singh",    "General Physician","04:00 PM", "Mar 20",             "Audio Call",  "Completed",  NabhaBlue400,     200, NabhaGreen500),
    AppointmentUi("a6", "Dr. Priya Sharma",   "Pediatrician",    "11:30 AM", "Mar 10",             "Video Call",  "Cancelled",  NabhaRed500,      150, SpecialtyPediatrics)
)

@Composable
fun AppointmentsScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val upcoming  = mockAppointments.filter { it.status in listOf("Confirmed", "Pending") }
    val past      = mockAppointments.filter { it.status in listOf("Completed", "Cancelled") }

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Column(
                modifier = Modifier
                    .background(Brush.verticalGradient(listOf(NabhaBlue900.copy(0.4f), SurfaceDark)))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(12.dp))
                Text("My Appointments", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 22.sp)
                Text("ਮੇਰੀਆਂ ਮੁਲਾਕਾਤਾਂ", color = TextTertiary, fontSize = 12.sp)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardDark2)
                ) {
                    listOf("Upcoming (${upcoming.size})","Past (${past.size})").forEachIndexed { index, tab ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selectedTab == index) NabhaBlue500.copy(0.15f) else Color.Transparent)
                                .clickable(MutableInteractionSource(), null) { selectedTab = index }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                tab,
                                color      = if (selectedTab == index) NabhaBlue300 else TextTertiary,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize   = 13.sp
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(NabhaBlue500, NabhaBlue700)))
                    .clickable(MutableInteractionSource(), null) { navController.navigate(Screen.FindDoctor.route) }
                    .padding(16.dp)
            ) {
                Icon(Icons.Rounded.Add, null, tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val list = if (selectedTab == 0) upcoming else past
            if (list.isEmpty()) {
                item {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Rounded.CalendarToday, null, tint = TextTertiary, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(16.dp))
                        Text("No appointments found", color = TextTertiary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Text("Book a consultation with a doctor", color = TextTertiary, fontSize = 13.sp)
                    }
                }
            } else {
                items(list, key = { it.id }) { appt ->
                    AppointmentCard(
                        appointment = appt,
                        onJoin      = { navController.navigate(Screen.VideoCall.withArgs(appt.id, "demo_token")) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppointmentCard(appointment: AppointmentUi, onJoin: () -> Unit) {
    NabhaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(appointment.color.copy(0.15f))
                        .border(2.dp, appointment.color.copy(0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Person, null, tint = appointment.color, modifier = Modifier.size(28.dp))
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(appointment.doctorName, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 15.sp)
                    Text(appointment.specialty, color = appointment.color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(appointment.statusColor.copy(0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(appointment.status, color = appointment.statusColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = DividerDark, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.AccessTime, null, tint = NabhaBlue400, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(appointment.time, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(appointment.date, color = TextTertiary, fontSize = 11.sp)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (appointment.type == "Video Call") Icons.Rounded.VideoCall else Icons.Rounded.Phone,
                        null,
                        tint     = NabhaBlue400,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(appointment.type, color = TextTertiary, fontSize = 12.sp)
                }

                Text("Rs.${appointment.fee}", fontWeight = FontWeight.Bold, color = NabhaGreen400, fontSize = 13.sp)
            }

            if (appointment.status == "Confirmed") {
                Spacer(Modifier.height(12.dp))
                NabhaButton(
                    text   = "Join Video Call",
                    onClick = onJoin,
                    icon   = Icons.Rounded.VideoCall,
                    height = 46.dp
                )
            }
        }
    }
}
