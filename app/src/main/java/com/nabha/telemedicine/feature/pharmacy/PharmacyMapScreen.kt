package com.nabha.telemedicine.feature.pharmacy

import android.preference.PreferenceManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.theme.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

// ─────────────────────────────────────────────────────────────────────────────
// FREE Map Implementation using OpenStreetMap + OSMDroid
//
//  ✅ OpenStreetMap data = free & open (ODbL license)
//  ✅ OSMDroid library = free & open-source (Apache 2.0)
//  ✅ No Google Maps API key required
//  ✅ No billing account required
//  ✅ No usage limits
//  ✅ Works offline with cached tiles
//
// ─────────────────────────────────────────────────────────────────────────────

data class PharmacyCoordinate(
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    val isOpen: Boolean,
    val phone: String
)

// Nabha area coordinates (approx)
val pharmacyCoordinates = listOf(
    PharmacyCoordinate("p1", "Sharma Medical Store",  "Near Bus Stand",        30.3760, 76.1490, true,  "+91-9876543210"),
    PharmacyCoordinate("p2", "Punjab Dawakhana",       "Civil Hospital Road",   30.3745, 76.1510, true,  "+91-9876543211"),
    PharmacyCoordinate("p3", "Guru Nanak Medical",     "Gandhi Chowk",          30.3730, 76.1530, false, "+91-9876543212"),
    PharmacyCoordinate("p4", "Saini Pharmacy",         "Sangrur Road",          30.3720, 76.1455, true,  "+91-9876543213"),
    PharmacyCoordinate("p5", "City Medical Centre",    "Model Town",            30.3780, 76.1470, true,  "+91-9876543214")
)

@Composable
fun PharmacyMapScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedPharmacy by remember { mutableStateOf<PharmacyCoordinate?>(null) }

    // OSMDroid requires configuring user agent
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        Configuration.getInstance().userAgentValue = "com.nabha.telemedicine/1.0"
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
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardDark2)
                        .clickable(MutableInteractionSource(), null) { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.ArrowBackIos, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Nearby Pharmacies", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 17.sp)
                    Text("ਨਜ਼ਦੀਕੀ ਦਵਾਖਾਨੇ · OpenStreetMap (Free)", color = NabhaGreen400, fontSize = 11.sp)
                }
                Spacer(Modifier.weight(1f))
                // Free badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NabhaGreen800.copy(0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("FREE MAP", color = NabhaGreen400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {

            // OSMDroid MapView — 100% free, no API key
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory  = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK) // OpenStreetMap tiles (free)
                        setMultiTouchControls(true)
                        controller.setZoom(15.5)
                        // Center on Nabha, Punjab
                        controller.setCenter(GeoPoint(30.374, 76.150))

                        // Add pharmacy markers
                        pharmacyCoordinates.forEach { pharmacy ->
                            val marker = Marker(this).apply {
                                position        = GeoPoint(pharmacy.lat, pharmacy.lon)
                                title           = pharmacy.name
                                snippet         = pharmacy.address
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            marker.setOnMarkerClickListener { _, _ ->
                                selectedPharmacy = pharmacy
                                true
                            }
                            overlays.add(marker)
                        }
                        invalidate()
                    }
                }
            )

            // Bottom sheet — pharmacy details
            selectedPharmacy?.let { pharmacy ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(SurfaceDark2)
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(listOf(NabhaBlue700, NabhaGreen700)),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(pharmacy.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                                Text(pharmacy.address, color = TextTertiary, fontSize = 12.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (pharmacy.isOpen) NabhaGreen800.copy(0.5f) else NabhaRed700.copy(0.3f)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    if (pharmacy.isOpen) "Open" else "Closed",
                                    color      = if (pharmacy.isOpen) NabhaGreen400 else NabhaRed400,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 12.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Call button
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NabhaBlue800.copy(0.5f))
                                    .border(1.dp, NabhaBlue700, RoundedCornerShape(12.dp))
                                    .clickable(MutableInteractionSource(), null) { /* dial */ }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Phone, null, tint = NabhaBlue400, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Call", color = NabhaBlue300, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                            // Get Directions button
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NabhaGreen800.copy(0.5f))
                                    .border(1.dp, NabhaGreen700, RoundedCornerShape(12.dp))
                                    .clickable(MutableInteractionSource(), null) { /* directions */ }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Navigation, null, tint = NabhaGreen400, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Directions", color = NabhaGreen300, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                            // Dismiss
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CardDark2)
                                    .clickable(MutableInteractionSource(), null) { selectedPharmacy = null },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Rounded.Close, null, tint = TextTertiary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Legend
            if (selectedPharmacy == null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(Color.Transparent, SurfaceDark.copy(0.9f)))
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        "Tap a marker to see pharmacy details\nPowered by OpenStreetMap — 100% Free",
                        color     = TextTertiary,
                        fontSize  = 12.sp,
                        modifier  = Modifier.align(Alignment.Center),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
