package com.nabha.telemedicine.feature.consultation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

data class Specialty(val name: String, val namePunjabi: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val color: Color)
data class DoctorCard(val id: String, val name: String, val specialty: String, val qualification: String, val experience: Int, val rating: Float, val reviews: Int, val fee: Int, val isAvailable: Boolean, val color: Color, val languages: List<String>)

val specialties = listOf(
    Specialty("All",            "ਸਾਰੇ",            Icons.Rounded.GridView,       NabhaBlue400),
    Specialty("General",        "ਜਨਰਲ",            Icons.Rounded.LocalHospital,   NabhaGreen500),
    Specialty("Pediatrics",     "ਬੱਚਿਆਂ ਦੇ",       Icons.Rounded.ChildCare,       SpecialtyPediatrics),
    Specialty("Cardiology",     "ਦਿਲ",             Icons.Rounded.Favorite,        SpecialtyCardiology),
    Specialty("Orthopedics",    "ਹੱਡੀਆਂ",           Icons.Rounded.Accessible,      SpecialtyOrthopedics),
    Specialty("Gynecology",     "ਗਾਇਨੀ",           Icons.Rounded.PregnantWoman,   SpecialtyGynecology),
    Specialty("Dermatology",    "ਚਮੜੀ",            Icons.Rounded.Face,            SpecialtyDermatology),
    Specialty("Neurology",      "ਦਿਮਾਗ",           Icons.Rounded.Psychology,       SpecialtyNeurology)
)

val sampleDoctors = listOf(
    DoctorCard("d1", "Dr. Arjun Singh",    "General Physician", "MBBS, MD",    12, 4.8f, 342, 200, true,  NabhaGreen500,    listOf("Punjabi", "Hindi", "English")),
    DoctorCard("d2", "Dr. Priya Sharma",   "Pediatrician",      "MBBS, DCH",   8,  4.9f, 521, 150, true,  SpecialtyPediatrics, listOf("Punjabi", "Hindi")),
    DoctorCard("d3", "Dr. Rajesh Kumar",   "Cardiologist",      "MBBS, DM",    18, 4.7f, 198, 500, false, SpecialtyCardiology, listOf("Hindi", "English")),
    DoctorCard("d4", "Dr. Neha Gupta",     "Gynecologist",      "MBBS, MS",    10, 4.8f, 287, 300, true,  SpecialtyGynecology, listOf("Punjabi", "Hindi")),
    DoctorCard("d5", "Dr. Sukhvir Dhaliwal","Orthopedician",    "MBBS, MS",    15, 4.6f, 156, 400, true,  SpecialtyOrthopedics, listOf("Punjabi", "Hindi")),
    DoctorCard("d6", "Dr. Manpreet Kaur",  "Dermatologist",     "MBBS, DVD",   7,  4.7f, 203, 250, false, SpecialtyDermatology, listOf("Punjabi", "Hindi", "English"))
)

@Composable
fun FindDoctorScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf("All") }
    var showOnlineOnly by remember { mutableStateOf(false) }

    val filtered = sampleDoctors.filter { doc ->
        (selectedSpecialty == "All" || doc.specialty.contains(selectedSpecialty, true)) &&
        (searchQuery.isEmpty() || doc.name.contains(searchQuery, true) || doc.specialty.contains(searchQuery, true)) &&
        (!showOnlineOnly || doc.isAvailable)
    }

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(listOf(NabhaBlue900.copy(0.7f), SurfaceDark))
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    NabhaIconButton(Icons.Rounded.ArrowBackIos, { navController.popBackStack() })
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Find Doctor", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                        Text("ਡਾਕਟਰ ਲੱਭੋ", color = TextTertiary, fontSize = 12.sp)
                    }
                    // Online filter toggle
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (showOnlineOnly) NabhaGreen800.copy(0.5f) else CardDark)
                            .border(1.dp, if (showOnlineOnly) NabhaGreen500 else DividerDark, RoundedCornerShape(20.dp))
                            .clickable(MutableInteractionSource(), null) { showOnlineOnly = !showOnlineOnly }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (showOnlineOnly) StatusOnline else TextTertiary)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Online",
                            color     = if (showOnlineOnly) NabhaGreen400 else TextTertiary,
                            fontSize  = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                NabhaTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it },
                    hint          = "Search by name or specialty...",
                    leadingIcon   = Icons.Rounded.Search
                )

                Spacer(Modifier.height(14.dp))

                // Specialty chips
                LazyRow(
                    contentPadding        = PaddingValues(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(specialties) { spec ->
                        SpecialtyChip(
                            specialty   = spec,
                            isSelected  = selectedSpecialty == spec.name,
                            onClick     = { selectedSpecialty = spec.name }
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark),
            contentPadding = PaddingValues(
                top    = padding.calculateTopPadding() + 8.dp,
                bottom = 24.dp,
                start  = 20.dp,
                end    = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "${filtered.size} doctor${if (filtered.size != 1) "s" else ""} found",
                    color    = TextTertiary,
                    fontSize = 13.sp
                )
            }
            items(filtered, key = { it.id }) { doc ->
                DoctorListCard(
                    doctor  = doc,
                    onClick = { navController.navigate(Screen.DoctorProfile.withId(doc.id)) }
                )
            }
            if (filtered.isEmpty()) {
                item {
                    EmptyState(message = "No doctors found. Try another specialty or search term.")
                }
            }
        }
    }
}

@Composable
private fun SpecialtyChip(specialty: Specialty, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        if (isSelected) specialty.color.copy(0.2f) else CardDark, tween(200), label = "chip_bg"
    )
    val borderColor by animateColorAsState(
        if (isSelected) specialty.color else DividerDark, tween(200), label = "chip_border"
    )
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(specialty.icon, null, tint = if (isSelected) specialty.color else TextTertiary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(
            specialty.name,
            color      = if (isSelected) specialty.color else TextSecondary,
            fontSize   = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun DoctorListCard(doctor: DoctorCard, onClick: () -> Unit) {
    NabhaCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(MutableInteractionSource(), null) { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(doctor.color.copy(0.15f))
                            .border(2.dp, doctor.color.copy(0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Person, null, tint = doctor.color, modifier = Modifier.size(32.dp))
                    }
                    if (doctor.isAvailable) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(StatusOnline)
                                .border(2.dp, CardDark, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(doctor.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                    Text(doctor.specialty, color = doctor.color, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text("${doctor.qualification} · ${doctor.experience} yrs exp", color = TextTertiary, fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Star, null, tint = NabhaSaffron400, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text(doctor.rating.toString(), color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text("${doctor.reviews} reviews", color = TextTertiary, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = DividerDark, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Translate, null, tint = TextTertiary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(doctor.languages.joinToString(", "), color = TextTertiary, fontSize = 11.sp, maxLines = 1)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        "Rs. ${doctor.fee}",
                        color      = NabhaGreen400,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (doctor.isAvailable) NabhaBlue500 else OutlineDark)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            if (doctor.isAvailable) "Book Now" else "Unavailable",
                            color      = Color.White,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.PersonSearch, null, tint = TextTertiary, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(16.dp))
        Text(message, color = TextTertiary, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp))
    }
}
