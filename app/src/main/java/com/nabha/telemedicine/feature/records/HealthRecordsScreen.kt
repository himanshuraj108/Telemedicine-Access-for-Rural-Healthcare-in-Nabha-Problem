package com.nabha.telemedicine.feature.records

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

data class HealthRecordUi(
    val id: String,
    val title: String,
    val type: String,
    val doctorName: String,
    val date: String,
    val icon: ImageVector,
    val color: Color,
    val isLocal: Boolean
)

val mockRecords = listOf(
    HealthRecordUi("r1", "Fever Treatment Prescription",    "Prescription",    "Dr. Arjun Singh",   "Apr 3, 2026",  Icons.Rounded.Description,  NabhaBlue400,     false),
    HealthRecordUi("r2", "Blood Test Report — CBC",         "Lab Result",      "Nabha Civil Lab",   "Mar 28, 2026", Icons.Rounded.Science,       NabhaGreen400,    false),
    HealthRecordUi("r3", "Chest X-Ray Report",              "Scan Report",     "Dr. Rajesh Kumar",  "Mar 15, 2026", Icons.Rounded.MonitorHeart,  SpecialtyCardiology,false),
    HealthRecordUi("r4", "COVID-19 Vaccination Certificate","Vaccination",     "PHC Nabha",         "Jan 12, 2026", Icons.Rounded.Vaccines,      NabhaGreen500,    false),
    HealthRecordUi("r5", "Discharge Summary",               "Discharge",       "Nabha Civil Hospital","Dec 5, 2025",Icons.Rounded.LocalHospital, NabhaSaffron400,  true),
    HealthRecordUi("r6", "Typhoid Test Report",             "Lab Result",      "City Diagnostic",   "Nov 20, 2025", Icons.Rounded.Biotech,       SpecialtyNeurology,false)
)

val recordFilters = listOf("All", "Prescription", "Lab Result", "Scan Report", "Vaccination")

@Composable
fun HealthRecordsScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filtered = mockRecords.filter { rec ->
        (selectedFilter == "All" || rec.type == selectedFilter) &&
        (searchQuery.isEmpty() || rec.title.contains(searchQuery, true) || rec.type.contains(searchQuery, true))
    }

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
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column {
                        Text("My Health Records", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 20.sp)
                        Text("ਮੇਰੇ ਸਿਹਤ ਰਿਕਾਰਡ", color = TextTertiary, fontSize = 12.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NabhaIconButton(Icons.Rounded.QrCode, { /* Share via QR */ })
                        NabhaIconButton(Icons.Rounded.Add, { /* Upload */ }, tint = NabhaGreen400)
                    }
                }
                Spacer(Modifier.height(14.dp))
                NabhaTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it },
                    hint          = "Search records...",
                    leadingIcon   = Icons.Rounded.Search
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding        = PaddingValues(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recordFilters) { filter ->
                        FilterChipRecord(filter, selectedFilter == filter) { selectedFilter = filter }
                    }
                }
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(listOf(NabhaBlue500, NabhaBlue700)))
                    .clickable(MutableInteractionSource(), null) { /* Upload record */ }
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Upload, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Upload Record", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
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
                bottom = padding.calculateBottomPadding() + 100.dp,
                start  = 20.dp,
                end    = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Offline sync status
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(NabhaGreen800.copy(0.3f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.CloudDone, null, tint = NabhaGreen400, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("${mockRecords.size} records synced offline · Last sync: Just now", color = NabhaGreen300, fontSize = 12.sp)
                }
            }

            item {
                Text("${filtered.size} record${if (filtered.size != 1) "s" else ""}", color = TextTertiary, fontSize = 13.sp)
            }

            items(filtered, key = { it.id }) { record ->
                HealthRecordCard(
                    record  = record,
                    onClick = { navController.navigate(Screen.RecordDetail.withId(record.id)) }
                )
            }
        }
    }
}

@Composable
private fun FilterChipRecord(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) NabhaBlue500.copy(0.2f) else CardDark)
            .border(1.dp, if (isSelected) NabhaBlue400 else DividerDark, RoundedCornerShape(20.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(label, color = if (isSelected) NabhaBlue300 else TextSecondary, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
private fun HealthRecordCard(record: HealthRecordUi, onClick: () -> Unit) {
    NabhaCard(modifier = Modifier.clickable(MutableInteractionSource(), null) { onClick() }) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(record.color.copy(0.12f))
                    .border(1.dp, record.color.copy(0.3f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(record.icon, null, tint = record.color, modifier = Modifier.size(26.dp))
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(record.title, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp, modifier = Modifier.weight(1f), maxLines = 2)
                    if (record.isLocal) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NabhaSaffron500.copy(0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Local", color = NabhaSaffron300, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Spacer(Modifier.height(3.dp))
                Text(record.type, color = record.color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Text("${record.doctorName} · ${record.date}", color = TextTertiary, fontSize = 11.sp)
            }

            Icon(Icons.Rounded.ChevronRight, null, tint = TextTertiary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun RecordDetailScreen(recordId: String, navController: NavController) {
    val record = mockRecords.find { it.id == recordId } ?: mockRecords[0]

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Row(
                modifier = Modifier
                    .background(SurfaceDark2)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NabhaIconButton(Icons.Rounded.ArrowBackIos, { navController.popBackStack() })
                Spacer(Modifier.width(12.dp))
                Text("Record Detail", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 17.sp)
                Spacer(Modifier.weight(1f))
                NabhaIconButton(Icons.Rounded.Share, { /* share */ }, tint = NabhaBlue400)
                NabhaIconButton(Icons.Rounded.Download, { /* download */ }, tint = NabhaGreen400)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(record.color.copy(0.2f), CardDark)
                            )
                        )
                        .border(1.dp, record.color.copy(0.3f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(record.icon, null, tint = record.color, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Preview not available", color = TextTertiary, fontSize = 13.sp)
                        Text("Tap download to save locally", color = TextTertiary, fontSize = 11.sp)
                    }
                }
            }
            item {
                NabhaCard {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DetailRow("Record Title", record.title, Icons.Rounded.Description)
                        DetailRow("Type",         record.type,       Icons.Rounded.Category)
                        DetailRow("Doctor",       record.doctorName, Icons.Rounded.Person)
                        DetailRow("Date",         record.date,       Icons.Rounded.CalendarMonth)
                        DetailRow("Storage",      if (record.isLocal) "Local Device" else "Cloud Synced", if (record.isLocal) Icons.Rounded.PhoneAndroid else Icons.Rounded.CloudDone)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TextTertiary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, color = TextTertiary, fontSize = 11.sp)
            Text(value, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}
