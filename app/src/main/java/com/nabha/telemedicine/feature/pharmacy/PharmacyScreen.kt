package com.nabha.telemedicine.feature.pharmacy

import androidx.compose.animation.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen

data class PharmacyUi(val id: String, val name: String, val address: String, val distance: String, val isOpen: Boolean, val phone: String, val medicinesCount: Int, val inStockCount: Int)
data class MedicineUi(val id: String, val name: String, val type: String, val price: Double, val isAvailable: Boolean, val pharmacyName: String)

val mockPharmacies = listOf(
    PharmacyUi("p1", "Sharma Medical Store",   "Near Bus Stand, Nabha",      "0.8 km",  true,  "+91-9876543210", 85, 78),
    PharmacyUi("p2", "Punjab Dawakhana",        "Civil Hospital Road, Nabha", "1.2 km",  true,  "+91-9876543211", 120,102),
    PharmacyUi("p3", "Guru Nanak Medical",      "Gandhi Chowk, Nabha",       "2.1 km",  false, "+91-9876543212", 65, 58),
    PharmacyUi("p4", "Saini Pharmacy",          "Village Sangrur Road",       "3.4 km",  true,  "+91-9876543213", 45, 40),
    PharmacyUi("p5", "City Medical Centre",     "Model Town, Nabha",          "1.8 km",  true,  "+91-9876543214", 95, 87)
)

val popularMedicines = listOf(
    MedicineUi("m1", "Paracetamol 500mg",  "Tablet",   12.0,  true,  "Sharma Medical"),
    MedicineUi("m2", "Amoxicillin 250mg",  "Capsule",  85.0,  true,  "Punjab Dawakhana"),
    MedicineUi("m3", "ORS Powder",         "Sachet",   8.0,   true,  "All Pharmacies"),
    MedicineUi("m4", "Omeprazole 20mg",    "Capsule",  35.0,  false, "Out of Stock"),
    MedicineUi("m5", "Metformin 500mg",    "Tablet",   45.0,  true,  "Sharma Medical"),
    MedicineUi("m6", "Amlodipine 5mg",     "Tablet",   28.0,  true,  "City Medical")
)

@Composable
fun PharmacyScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val filtered = mockPharmacies.filter { p ->
        searchQuery.isEmpty() || p.name.contains(searchQuery, true) || p.address.contains(searchQuery, true)
    }

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Column(
                modifier = Modifier
                    .background(Brush.verticalGradient(listOf(NabhaSaffron700.copy(0.15f), SurfaceDark)))
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
                        Text("Pharmacy Finder", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 20.sp)
                        Text("ਦਵਾਖਾਨਾ ਖੋਜੋ · Real-time stock", color = NabhaSaffron400, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    NabhaIconButton(
                        icon    = Icons.Rounded.Search,
                        onClick = { navController.navigate(Screen.MedicineSearch.route) },
                        tint    = NabhaSaffron400
                    )
                }

                Spacer(Modifier.height(14.dp))

                NabhaTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it },
                    hint          = "Search pharmacies or medicines...",
                    leadingIcon   = Icons.Rounded.Search
                )

                Spacer(Modifier.height(12.dp))

                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardDark2),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    listOf("Nearby Pharmacies", "Popular Medicines").forEachIndexed { index, tab ->
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Update notice
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NabhaBlue900.copy(0.5f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Update, null, tint = NabhaBlue400, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Stocks updated 5 minutes ago", color = NabhaBlue300, fontSize = 12.sp)
                }
            }

            if (selectedTab == 0) {
                // Pharmacy list
                items(filtered, key = { it.id }) { pharmacy ->
                    PharmacyCard(pharmacy = pharmacy)
                }
            } else {
                // Medicine list
                item {
                    Text("Popular Medicines", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                }
                items(popularMedicines) { med ->
                    MedicineCard(medicine = med)
                }
            }
        }
    }
}

@Composable
private fun PharmacyCard(pharmacy: PharmacyUi) {
    NabhaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(NabhaSaffron500.copy(0.12f))
                        .border(1.dp, NabhaSaffron500.copy(0.3f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.LocalPharmacy, null, tint = NabhaSaffron400, modifier = Modifier.size(26.dp))
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(pharmacy.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                    Text(pharmacy.address, color = TextTertiary, fontSize = 12.sp, maxLines = 1)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.LocationOn, null, tint = NabhaBlue400, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(2.dp))
                        Text(pharmacy.distance, color = NabhaBlue300, fontSize = 12.sp)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (pharmacy.isOpen) NabhaGreen800.copy(0.5f) else NabhaRed700.copy(0.3f)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            if (pharmacy.isOpen) "Open" else "Closed",
                            color      = if (pharmacy.isOpen) NabhaGreen400 else NabhaRed400,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
                Column {
                    Text("${pharmacy.inStockCount}/${pharmacy.medicinesCount}", fontWeight = FontWeight.Bold, color = NabhaGreen400, fontSize = 14.sp)
                    Text("in stock", color = TextTertiary, fontSize = 11.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NabhaBlue800.copy(0.5f))
                            .border(1.dp, NabhaBlue700, RoundedCornerShape(10.dp))
                            .clickable(MutableInteractionSource(), null) { /* call */ }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Phone, null, tint = NabhaBlue400, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Call", color = NabhaBlue300, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NabhaGreen800.copy(0.5f))
                            .border(1.dp, NabhaGreen700, RoundedCornerShape(10.dp))
                            .clickable(MutableInteractionSource(), null) { /* view stock */ }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("View Stock", color = NabhaGreen400, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicineCard(medicine: MedicineUi) {
    NabhaCard {
        Row(
            modifier          = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (medicine.isAvailable) NabhaGreen800.copy(0.4f) else NabhaRed700.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (medicine.isAvailable) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                    null,
                    tint     = if (medicine.isAvailable) NabhaGreen400 else NabhaRed500,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(medicine.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                Text("${medicine.type} · ${medicine.pharmacyName}", color = TextTertiary, fontSize = 12.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Rs. ${medicine.price.toInt()}", fontWeight = FontWeight.Bold, color = NabhaGreen400, fontSize = 14.sp)
                Text(
                    if (medicine.isAvailable) "In Stock" else "Out of Stock",
                    color    = if (medicine.isAvailable) NabhaGreen400 else NabhaRed500,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun MedicineSearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val results = popularMedicines.filter {
        searchQuery.isEmpty() || it.name.contains(searchQuery, true)
    }

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
                NabhaTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it },
                    hint          = "Search medicine name...",
                    leadingIcon   = Icons.Rounded.Search,
                    modifier      = Modifier.weight(1f)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(results) { med ->
                MedicineCard(medicine = med)
            }
        }
    }
}
