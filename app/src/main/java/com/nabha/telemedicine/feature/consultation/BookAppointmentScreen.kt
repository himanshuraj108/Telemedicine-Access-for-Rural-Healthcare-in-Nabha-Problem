package com.nabha.telemedicine.feature.consultation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

@Composable
fun BookAppointmentScreen(doctorId: String, navController: NavController) {
    val doctor = sampleDoctors.find { it.id == doctorId } ?: sampleDoctors[0]
    var selectedType by remember { mutableStateOf("Video Call") }
    var selectedDate by remember { mutableStateOf("Today") }
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var symptoms by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val dates = listOf("Today","Tomorrow","Apr 8","Apr 9","Apr 10","Apr 11","Apr 12")
    val slots = listOf("09:00 AM","09:30 AM","10:00 AM","11:30 AM","02:00 PM","02:30 PM","03:30 PM","04:00 PM")

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark2)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NabhaIconButton(Icons.Rounded.ArrowBackIos, { navController.popBackStack() })
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Book Appointment", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 17.sp)
                    Text("ਮੁਲਾਕਾਤ ਬੁੱਕ ਕਰੋ", color = TextTertiary, fontSize = 12.sp)
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark2)
                    .navigationBarsPadding()
                    .padding(20.dp)
            ) {
                NabhaButton(
                    text      = if (selectedSlot != null) "Confirm Booking — Rs.${doctor.fee}" else "Select Slot to Continue",
                    onClick   = {
                        isLoading = true
                        navController.navigate(Screen.Appointments.route) { popUpTo(Screen.Home.route) }
                    },
                    enabled   = selectedSlot != null,
                    isLoading = isLoading,
                    icon      = Icons.Rounded.CheckCircle
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .verticalScroll(rememberScrollState())
                .padding(
                    top    = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 80.dp
                )
        ) {
            // Doctor summary card
            NabhaCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier          = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(doctor.color.copy(0.15f))
                            .border(2.dp, doctor.color.copy(0.4f), androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Person, null, tint = doctor.color, modifier = Modifier.size(26.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(doctor.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                        Text(doctor.specialty, color = doctor.color, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Rs. ${doctor.fee}", fontWeight = FontWeight.Bold, color = NabhaGreen400, fontSize = 15.sp)
                        Text("per session", color = TextTertiary, fontSize = 10.sp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Consultation type
            SectionLabel("Consultation Type", "ਸਲਾਹ ਦੀ ਕਿਸਮ")
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    Triple("Video Call",  Icons.Rounded.VideoCall, NabhaBlue400),
                    Triple("Audio Call",  Icons.Rounded.Phone,     NabhaGreen400)
                ).forEach { (type, icon, color) ->
                    ConsultTypeButton(
                        type       = type,
                        icon       = icon,
                        color      = color,
                        isSelected = selectedType == type,
                        onClick    = { selectedType = type },
                        modifier   = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Date selection
            SectionLabel("Select Date", "ਤਾਰੀਖ਼ ਚੁਣੋ")
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(dates) { date ->
                    DateChip(date = date, isSelected = selectedDate == date, onClick = {
                        selectedDate   = date
                        selectedSlot   = null
                    })
                }
            }

            Spacer(Modifier.height(20.dp))

            // Time slots
            SectionLabel("Available Time Slots", "ਸਮਾਂ ਚੁਣੋ")
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(slots) { slot ->
                    TimeSlotChip(slot = slot, isSelected = selectedSlot == slot, onClick = { selectedSlot = slot })
                }
            }

            Spacer(Modifier.height(20.dp))

            // Symptoms
            SectionLabel("Describe Your Symptoms (Optional)", "ਲੱਛਣ ਦੱਸੋ")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardDark2)
                    .border(1.dp, DividerDark, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                androidx.compose.foundation.text.BasicTextField(
                    value         = symptoms,
                    onValueChange = { symptoms = it },
                    textStyle     = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                    modifier      = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 80.dp),
                    cursorBrush   = androidx.compose.ui.graphics.SolidColor(NabhaBlue400),
                    decorationBox = { inner ->
                        if (symptoms.isEmpty()) {
                            Text("e.g. fever since 2 days, headache, weakness...", color = TextTertiary, fontSize = 14.sp)
                        }
                        inner()
                    }
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, textPunjabi: String) {
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)) {
        Text(text, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
        Text(textPunjabi, color = TextTertiary, fontSize = 11.sp)
    }
}

@Composable
private fun ConsultTypeButton(type: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) color.copy(0.15f) else CardDark)
            .border(1.5.dp, if (isSelected) color else DividerDark, RoundedCornerShape(14.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = if (isSelected) color else TextTertiary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(type, color = if (isSelected) color else TextSecondary, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, fontSize = 14.sp)
    }
}

@Composable
private fun DateChip(date: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NabhaBlue500.copy(0.2f) else CardDark)
            .border(1.dp, if (isSelected) NabhaBlue400 else DividerDark, RoundedCornerShape(12.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(date, color = if (isSelected) NabhaBlue300 else TextSecondary, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, fontSize = 13.sp)
    }
}

@Composable
private fun TimeSlotChip(slot: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NabhaGreen500.copy(0.15f) else CardDark)
            .border(1.dp, if (isSelected) NabhaGreen400 else DividerDark, RoundedCornerShape(12.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(slot, color = if (isSelected) NabhaGreen300 else TextSecondary, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, fontSize = 13.sp)
    }
}
