package com.nabha.telemedicine.feature.consultation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun DoctorProfileScreen(doctorId: String, navController: NavController) {
    val doctor = sampleDoctors.find { it.id == doctorId } ?: sampleDoctors[0]
    val scrollState = rememberScrollState()

    val timeSlots = listOf("09:00 AM","09:30 AM","10:00 AM","10:30 AM","11:00 AM","02:00 PM","02:30 PM","03:00 PM","04:00 PM","04:30 PM")
    var selectedSlot by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = SurfaceDark,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark2)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                NabhaButton(
                    text    = if (selectedSlot != null) "Book — Rs. ${doctor.fee}" else "Select a Time Slot",
                    onClick = { navController.navigate(Screen.BookAppointment.withId(doctor.id)) },
                    enabled = selectedSlot != null
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .verticalScroll(scrollState)
        ) {
            // Hero section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(doctor.color.copy(0.2f), SurfaceDark),
                            endY = 500f
                        )
                    )
                    .padding(horizontal = 20.dp)
            ) {
                Column {
                    Spacer(Modifier.height(56.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NabhaIconButton(Icons.Rounded.ArrowBackIos, { navController.popBackStack() })
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier          = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(doctor.color.copy(0.2f))
                                    .border(3.dp, doctor.color.copy(0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Rounded.Person, null, tint = doctor.color, modifier = Modifier.size(50.dp))
                            }
                            if (doctor.isAvailable) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(StatusOnline)
                                        .border(3.dp, SurfaceDark, CircleShape)
                                        .align(Alignment.BottomEnd)
                                )
                            }
                        }

                        Spacer(Modifier.width(18.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(doctor.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 20.sp)
                            Text(doctor.specialty, color = doctor.color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(doctor.qualification, color = TextTertiary, fontSize = 13.sp)
                            Spacer(Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                StatBadge("${doctor.experience} Yrs", "Experience", NabhaBlue400)
                                StatBadge(doctor.rating.toString(), "Rating", NabhaSaffron400)
                                StatBadge("${doctor.reviews}", "Reviews", NabhaGreen400)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }

            // Info cards row
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoCard(Modifier.weight(1f), Icons.Rounded.VideoCall, "Video Call", "50 min", NabhaBlue400)
                InfoCard(Modifier.weight(1f), Icons.Rounded.Phone, "Audio Call", "Available", NabhaGreen400)
                InfoCard(Modifier.weight(1f), Icons.Rounded.CurrencyRupee, "Fee", "Rs.${doctor.fee}", NabhaSaffron400)
            }

            Spacer(Modifier.height(20.dp))

            // Languages
            SectionTitle2("Languages Spoken")
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(doctor.languages) { lang ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardDark2)
                            .border(1.dp, DividerDark, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(lang, color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // About
            SectionTitle2("About Doctor")
            Text(
                text      = "Dr. ${doctor.name.split(" ").last()} is a highly qualified ${doctor.specialty} with ${doctor.experience} years of experience. They specialize in primary care for rural communities and provide consultations in ${doctor.languages.joinToString(", ")} to ensure effective communication with patients from all backgrounds.",
                color     = TextSecondary,
                fontSize  = 14.sp,
                lineHeight = 22.sp,
                modifier  = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Time slots
            SectionTitle2("Available Slots — Today")
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(timeSlots) { slot ->
                    val isSelected = selectedSlot == slot
                    val booked     = slot in listOf("10:00 AM", "11:00 AM")
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    booked     -> CardDark.copy(alpha = 0.4f)
                                    isSelected -> NabhaBlue500.copy(0.2f)
                                    else       -> CardDark
                                }
                            )
                            .border(
                                1.dp,
                                when {
                                    booked     -> OutlineDark
                                    isSelected -> NabhaBlue400
                                    else       -> DividerDark
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication        = null,
                                enabled           = !booked
                            ) { selectedSlot = slot }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            slot,
                            color      = when {
                                booked     -> TextTertiary.copy(0.4f)
                                isSelected -> NabhaBlue300
                                else       -> TextSecondary
                            },
                            fontSize   = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(Modifier.height(padding.calculateBottomPadding() + 80.dp))
        }
    }
}

@Composable
private fun StatBadge(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 15.sp)
        Text(label, color = TextTertiary, fontSize = 10.sp)
    }
}

@Composable
private fun InfoCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(0.08f))
            .border(1.dp, color.copy(0.2f), RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.height(6.dp))
        Text(value, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
        Text(label, color = TextTertiary, fontSize = 10.sp)
    }
}

@Composable
private fun SectionTitle2(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        color      = TextPrimary,
        fontSize   = 16.sp,
        modifier   = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}
