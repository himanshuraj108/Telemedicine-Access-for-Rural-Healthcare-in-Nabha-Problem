package com.nabha.telemedicine.feature.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.components.NabhaButton
import com.nabha.telemedicine.core.design.components.NabhaOutlinedButton
import com.nabha.telemedicine.core.design.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val icon: ImageVector,
    val iconColor: Color,
    val title: String,
    val titleHindi: String,
    val description: String,
    val gradient: List<Color>
)

val onboardingPages = listOf(
    OnboardingPage(
        icon        = Icons.Rounded.VideoCall,
        iconColor   = NabhaBlue400,
        title       = "Video Consultations",
        titleHindi  = "ਵੀਡੀਓ ਸਲਾਹ",
        description = "Connect with qualified doctors from Nabha Civil Hospital via high-quality video calls — no travel required.",
        gradient    = listOf(NabhaBlue900, SurfaceDark)
    ),
    OnboardingPage(
        icon        = Icons.Rounded.FolderOpen,
        iconColor   = NabhaGreen400,
        title       = "Digital Health Records",
        titleHindi  = "ਡਿਜੀਟਲ ਸਿਹਤ ਰਿਕਾਰਡ",
        description = "All your prescriptions, lab reports, and medical history stored securely and accessible even offline.",
        gradient    = listOf(NabhaGreen900, SurfaceDark)
    ),
    OnboardingPage(
        icon        = Icons.Rounded.LocalPharmacy,
        iconColor   = NabhaSaffron400,
        title       = "Real-Time Medicine Stock",
        titleHindi  = "ਦਵਾਈਆਂ ਦੀ ਜਾਣਕਾਰੀ",
        description = "Check medicine availability across 50+ local pharmacies before making the trip. Save time, save money.",
        gradient    = listOf(Color(0xFF3B1F00), SurfaceDark)
    ),
    OnboardingPage(
        icon        = Icons.Rounded.Psychology,
        iconColor   = Color(0xFF8B5CF6),
        title       = "AI Symptom Checker",
        titleHindi  = "ਲੱਛਣ ਜਾਂਚ",
        description = "Describe your symptoms and get instant AI-powered guidance — works completely offline for rural areas.",
        gradient    = listOf(Color(0xFF1A0D3E), SurfaceDark)
    )
)

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit) {
    val pagerState = rememberPagerState { onboardingPages.size }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        HorizontalPager(
            state    = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPageContent(page = onboardingPages[page])
        }

        // Bottom controls
        Column(
            modifier            = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dot indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier              = Modifier.padding(bottom = 36.dp)
            ) {
                repeat(onboardingPages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    val width by animateDpAsState(
                        targetValue   = if (isSelected) 28.dp else 8.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label         = "dot_width_$index"
                    )
                    val color by animateColorAsState(
                        targetValue   = if (isSelected) NabhaBlue400 else OutlineDark,
                        animationSpec = tween(300),
                        label         = "dot_color_$index"
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            if (pagerState.currentPage == onboardingPages.size - 1) {
                NabhaButton(text = "Get Started — Free", onClick = onGetStarted)
            } else {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onGetStarted) {
                        Text("Skip", color = TextTertiary, fontSize = 15.sp)
                    }
                    NabhaButton(
                        text     = "Next",
                        onClick  = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        fullWidth = false,
                        modifier = Modifier.width(130.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    val iconScale = remember { Animatable(0.5f) }
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(page) {
        iconScale.snapTo(0.5f)
        contentAlpha.snapTo(0f)
        iconScale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
        )
        contentAlpha.animateTo(1f, tween(500))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = page.gradient,
                    startY = 0f,
                    endY   = 1200f
                )
            )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(0.5f))

            // Animated icon container
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(iconScale.value)
                    .clip(RoundedCornerShape(40.dp))
                    .background(page.iconColor.copy(alpha = 0.12f))
                    .then(
                        Modifier.padding(1.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(page.iconColor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = page.icon,
                        contentDescription = null,
                        tint               = page.iconColor,
                        modifier           = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(Modifier.height(48.dp))

            Text(
                text       = page.title,
                fontSize   = 30.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
                textAlign  = TextAlign.Center,
                modifier   = Modifier.alpha(contentAlpha.value)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text       = page.titleHindi,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Medium,
                color      = page.iconColor,
                textAlign  = TextAlign.Center,
                modifier   = Modifier.alpha(contentAlpha.value)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text       = page.description,
                fontSize   = 15.sp,
                color      = TextSecondary,
                textAlign  = TextAlign.Center,
                lineHeight = 24.sp,
                modifier   = Modifier
                    .alpha(contentAlpha.value)
                    .padding(horizontal = 8.dp)
            )

            Spacer(Modifier.weight(1f))
        }
    }
}
