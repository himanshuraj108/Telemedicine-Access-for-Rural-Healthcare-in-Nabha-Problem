package com.nabha.telemedicine.feature.symptom

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*
import com.nabha.telemedicine.core.navigation.Screen
import kotlinx.coroutines.delay

data class DiagnosisResult(val condition: String, val confidence: Int, val description: String, val specialty: String, val color: Color)

val mockDiagnosis = listOf(
    DiagnosisResult("Common Cold / Viral Fever",    82, "Viral upper respiratory infection. Usually resolves in 5-7 days with rest and fluids.", "General Physician", NabhaBlue400),
    DiagnosisResult("Sinusitis",                    61, "Inflammation of sinus cavities. May require nasal decongestants or antibiotics.", "ENT Specialist",    NabhaSaffron400),
    DiagnosisResult("Allergic Rhinitis",             44, "Allergic reaction causing nasal congestion and headache.", "General Physician", NabhaGreen400)
)

@Composable
fun DiagnosisResultScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }
    var analysisComplete by remember { mutableStateOf(false) }

    val urgencyColor   = NabhaSaffron400  // Medium urgency for demo
    val urgencyLabel   = "Moderate — See a Doctor"
    val urgencyPunjabi = "ਡਾਕਟਰ ਨੂੰ ਮਿਲੋ"

    LaunchedEffect(Unit) {
        delay(1200)
        analysisComplete = true
        delay(300)
        isVisible = true
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
                Column {
                    Text("AI Analysis Result", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 17.sp)
                    Text("ਵਿਸ਼ਲੇਸ਼ਣ ਨਤੀਜਾ", color = TextTertiary, fontSize = 12.sp)
                }
            }
        },
        bottomBar = {
            if (analysisComplete) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceDark2)
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    NabhaButton(
                        text    = "Consult a Doctor Now",
                        onClick = { navController.navigate(Screen.FindDoctor.route) },
                        icon    = Icons.Rounded.VideoCall
                    )
                    Spacer(Modifier.height(10.dp))
                    NabhaOutlinedButton(
                        text    = "Check Nearby Pharmacies",
                        onClick = { navController.navigate(Screen.Pharmacy.route) },
                        icon    = Icons.Rounded.LocalPharmacy
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(SurfaceDark)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(
                start  = 20.dp,
                top    = 16.dp,
                end    = 20.dp,
                bottom = padding.calculateBottomPadding() + 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Loading / complete animation
            if (!analysisComplete) {
                item { AnalyzingLoader() }
            } else {
                // Urgency banner
                item {
                    AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(500)) + slideInVertically(tween(500))) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(urgencyColor.copy(0.2f), urgencyColor.copy(0.05f))
                                    )
                                )
                                .border(1.dp, urgencyColor.copy(0.4f), RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(urgencyColor.copy(0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Rounded.Warning, null, tint = urgencyColor, modifier = Modifier.size(26.dp))
                                }
                                Spacer(Modifier.width(14.dp))
                                Column {
                                    Text("Urgency Level", color = urgencyColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text(urgencyLabel, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                                    Text(urgencyPunjabi, color = TextTertiary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Possible Conditions",
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        fontSize   = 16.sp
                    )
                }

                // Diagnosis cards
                itemsIndexed(mockDiagnosis) { index, result ->
                    val alpha = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        delay(index * 150L)
                        alpha.animateTo(1f, tween(400))
                    }
                    Box(modifier = Modifier.alpha(alpha.value)) {
                        DiagnosisCard(result = result, rank = index + 1)
                    }
                }

                item {
                    // Recommendation
                    NabhaCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Recommend, null, tint = NabhaBlue400, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Recommended Action", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Schedule a video consultation with a General Physician within 24-48 hours. Take rest, drink plenty of fluids, and monitor your temperature. If fever exceeds 103°F or breathing difficulty develops, seek emergency care immediately.",
                                color     = TextSecondary,
                                fontSize  = 13.sp,
                                lineHeight = 21.sp
                            )
                        }
                    }
                }

                item {
                    // Nearest facility
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(NabhaGreen800.copy(0.3f))
                            .border(1.dp, NabhaGreen700, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.LocalHospital, null, tint = NabhaGreen400, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nearest Facility", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                                Text("Nabha Civil Hospital — 2.4 km", color = NabhaGreen400, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text("Mon–Sat: 8 AM–5 PM · Emergency: 24/7", color = TextTertiary, fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Disclaimer
                item {
                    Text(
                        "This AI analysis is for informational purposes only and does not replace professional medical advice. Always consult a qualified doctor for diagnosis and treatment.",
                        color     = TextTertiary,
                        fontSize  = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyzingLoader() {
    val infiniteTransition = rememberInfiniteTransition(label = "loader")
    val scale by infiniteTransition.animateFloat(0.8f, 1.2f,
        infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "loader_scale"
    )
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp * scale)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF8B5CF6).copy(0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.AutoAwesome, null, tint = Color(0xFFA78BFA), modifier = Modifier.size(52.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Analyzing your symptoms...", fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 17.sp)
        Text("ਲੱਛਣਾਂ ਦਾ ਵਿਸ਼ਲੇਸ਼ਣ ਕੀਤਾ ਜਾ ਰਿਹਾ ਹੈ", color = TextTertiary, fontSize = 13.sp)
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            color            = Color(0xFF8B5CF6),
            trackColor       = DividerDark,
            modifier         = Modifier
                .width(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun DiagnosisCard(result: DiagnosisResult, rank: Int) {
    NabhaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(result.color.copy(0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "#$rank",
                        color      = result.color,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 14.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(result.condition, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                    Text("See: ${result.specialty}", color = result.color, fontSize = 12.sp)
                }
                // Confidence bar
                Column(horizontalAlignment = Alignment.End) {
                    Text("${result.confidence}%", fontWeight = FontWeight.Bold, color = result.color, fontSize = 16.sp)
                    Text("match", color = TextTertiary, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            // Confidence progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(DividerDark)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(result.confidence / 100f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(result.color)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(result.description, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
        }
    }
}
