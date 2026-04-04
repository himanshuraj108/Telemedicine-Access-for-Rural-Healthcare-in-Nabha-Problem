@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.nabha.telemedicine.feature.symptom

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen
import kotlinx.coroutines.delay

data class SymptomCategory(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val color: Color, val symptoms: List<String>)

val symptomCategories = listOf(
    SymptomCategory("Head & Neck",   Icons.Rounded.Psychology,    NabhaBlue400,   listOf("Headache","Dizziness","Neck pain","Sore throat","Earache","Toothache")),
    SymptomCategory("Chest & Heart", Icons.Rounded.Favorite,      NabhaRed500,    listOf("Chest pain","Palpitations","Shortness of breath","Chest tightness")),
    SymptomCategory("Stomach",       Icons.Rounded.Restaurant,    NabhaSaffron400,listOf("Stomach pain","Nausea","Vomiting","Diarrhea","Constipation","Bloating")),
    SymptomCategory("Joints & Body", Icons.Rounded.Accessible,    SpecialtyOrthopedics, listOf("Joint pain","Back pain","Muscle pain","Swelling","Stiffness")),
    SymptomCategory("Skin",          Icons.Rounded.Face,          SpecialtyDermatology, listOf("Rash","Itching","Redness","Swelling","Blisters","Dry skin")),
    SymptomCategory("General",       Icons.Rounded.LocalHospital, NabhaGreen400,  listOf("Fever","Fatigue","Weight loss","Night sweats","Loss of appetite","Weakness"))
)

@Composable
fun SymptomCheckerScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf(symptomCategories[0]) }
    val selectedSymptoms = remember { mutableStateListOf<String>() }
    var isAnalyzing by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf("") }
    var severity by remember { mutableIntStateOf(3) }

    val scale by animateFloatAsState(if (isAnalyzing) 1.1f else 1f, spring(), label = "ai_scale")

    Scaffold(
        containerColor = SurfaceDark,
        topBar = {
            Column(
                modifier = Modifier
                    .background(SurfaceDark2)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NabhaIconButton(Icons.Rounded.ArrowBackIos, { navController.popBackStack() })
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("AI Symptom Checker", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 17.sp)
                        Text("ਲੱਛਣ ਜਾਂਚ · Works Offline", color = NabhaGreen400, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NabhaGreen800.copy(0.5f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.OfflineBolt, null, tint = NabhaGreen400, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Offline", color = NabhaGreen400, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
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
                    text      = "Analyze ${if (selectedSymptoms.isNotEmpty()) "(${selectedSymptoms.size} symptoms)" else "Symptoms"}",
                    onClick   = {
                        isAnalyzing = true
                        navController.navigate(Screen.DiagnosisResult.route)
                    },
                    enabled   = selectedSymptoms.isNotEmpty(),
                    isLoading = isAnalyzing,
                    icon      = Icons.Rounded.Psychology
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding() + 80.dp)
        ) {
            // AI Banner
            item {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1A0D3E), Color(0xFF0D1B2A))
                            )
                        )
                        .border(1.dp, Color(0xFF8B5CF6).copy(0.4f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .scale(scale)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF8B5CF6).copy(0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, null, tint = Color(0xFFA78BFA), modifier = Modifier.size(26.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text("AI-Powered Analysis", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                            Text(
                                "Select your symptoms below for instant analysis. No internet needed.",
                                color     = TextSecondary,
                                fontSize  = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Category tabs
            item {
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(symptomCategories) { cat ->
                        CategoryChip(
                            category   = cat,
                            isSelected = selectedCategory == cat,
                            onClick    = { selectedCategory = cat }
                        )
                    }
                }
            }

            // Selected category symptoms
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Text(
                    "${selectedCategory.name} Symptoms",
                    fontWeight = FontWeight.SemiBold,
                    color      = TextPrimary,
                    fontSize   = 15.sp,
                    modifier   = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            item {
                androidx.compose.foundation.layout.FlowRow(
                    modifier              = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement   = Arrangement.spacedBy(10.dp)
                ) {
                    selectedCategory.symptoms.forEach { symptom ->
                        val isSelected = symptom in selectedSymptoms
                        SymptomChip(
                            symptom    = symptom,
                            isSelected = isSelected,
                            color      = selectedCategory.color,
                            onClick    = {
                                if (isSelected) selectedSymptoms.remove(symptom)
                                else selectedSymptoms.add(symptom)
                            }
                        )
                    }
                }
            }

            // Selected symptoms summary
            if (selectedSymptoms.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                        Text("Selected Symptoms (${selectedSymptoms.size})", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp, modifier = Modifier.padding(bottom = 10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(CardDark2)
                                .border(1.dp, DividerDark, RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                selectedSymptoms.joinToString(", "),
                                color     = TextSecondary,
                                fontSize  = 14.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                // Duration
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text("How long? (Optional)", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp, modifier = Modifier.padding(bottom = 10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf("Today","1-2 days","3-5 days","1 week","2+ weeks")) { dur ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (duration == dur) NabhaSaffron500.copy(0.15f) else CardDark)
                                        .border(1.dp, if (duration == dur) NabhaSaffron400 else DividerDark, RoundedCornerShape(20.dp))
                                        .clickable(MutableInteractionSource(), null) { duration = dur }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(dur, color = if (duration == dur) NabhaSaffron300 else TextSecondary, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                // Severity slider
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Severity", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                            Text(
                                when (severity) {
                                    in 1..3  -> "Mild"
                                    in 4..6  -> "Moderate"
                                    in 7..8  -> "Severe"
                                    else     -> "Very Severe"
                                },
                                color      = when (severity) {
                                    in 1..3  -> NabhaGreen400
                                    in 4..6  -> NabhaSaffron400
                                    in 7..8  -> NabhaRed500
                                    else     -> NabhaRed700
                                },
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 14.sp
                            )
                        }
                        Slider(
                            value          = severity.toFloat(),
                            onValueChange  = { severity = it.toInt() },
                            valueRange     = 1f..10f,
                            steps          = 8,
                            colors         = SliderDefaults.colors(
                                thumbColor              = NabhaRed500,
                                activeTrackColor        = NabhaRed500,
                                inactiveTrackColor      = DividerDark
                            )
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("1 - Mild", color = NabhaGreen400, fontSize = 11.sp)
                            Text("10 - Emergency", color = NabhaRed500, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(category: SymptomCategory, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) category.color.copy(0.15f) else CardDark)
            .border(1.dp, if (isSelected) category.color else DividerDark, RoundedCornerShape(20.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(category.icon, null, tint = if (isSelected) category.color else TextTertiary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(category.name, color = if (isSelected) category.color else TextSecondary, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
private fun SymptomChip(symptom: String, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) color.copy(0.15f) else CardDark2)
            .border(1.5.dp, if (isSelected) color else DividerDark, RoundedCornerShape(20.dp))
            .clickable(MutableInteractionSource(), null) { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Icon(Icons.Rounded.Check, null, tint = color, modifier = Modifier.size(14.dp).padding(end = 4.dp))
            }
            Text(symptom, color = if (isSelected) color else TextSecondary, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
        }
    }
}
