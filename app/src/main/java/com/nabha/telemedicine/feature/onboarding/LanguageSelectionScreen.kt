package com.nabha.telemedicine.feature.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Language
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
import com.nabha.telemedicine.core.design.components.NabhaButton
import com.nabha.telemedicine.core.design.theme.*
import kotlinx.coroutines.launch

data class LanguageOption(
    val code: String,
    val name: String,
    val nativeName: String,
    val tagline: String
)

val languages = listOf(
    LanguageOption("en", "English", "English", "Clear & Simple"),
    LanguageOption("hi", "Hindi", "हिन्दी", "हमारी भाषा"),
    LanguageOption("pa", "Punjabi", "ਪੰਜਾਬੀ", "ਸਾਡੀ ਭਾਸ਼ਾ")
)

@Composable
fun LanguageSelectionScreen(
    onLanguageSelected: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf("pa") }
    val scope = rememberCoroutineScope()

    val titleAlpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        titleAlpha.animateTo(1f, tween(600))
        contentAlpha.animateTo(1f, tween(600, delayMillis = 200))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SurfaceDark, SurfaceDark2, SurfaceDark)
                )
            )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))

            // Header
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(NabhaBlue600, NabhaBlue800))
                    )
                    .alpha(titleAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Language, null, tint = Color.White, modifier = Modifier.size(38.dp))
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text       = "Choose Your Language",
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
                textAlign  = TextAlign.Center,
                modifier   = Modifier.alpha(titleAlpha.value)
            )
            Text(
                text       = "ਆਪਣੀ ਭਾਸ਼ਾ ਚੁਣੋ",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Medium,
                color      = NabhaBlue300,
                textAlign  = TextAlign.Center,
                modifier   = Modifier
                    .alpha(titleAlpha.value)
                    .padding(top = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "You can change this later in settings",
                fontSize  = 13.sp,
                color     = TextTertiary,
                textAlign = TextAlign.Center,
                modifier  = Modifier.alpha(titleAlpha.value)
            )

            Spacer(Modifier.height(48.dp))

            // Language cards
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .alpha(contentAlpha.value),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                languages.forEach { lang ->
                    LanguageCard(
                        language   = lang,
                        isSelected = selectedLanguage == lang.code,
                        onClick    = { selectedLanguage = lang.code }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            NabhaButton(
                text     = "Continue",
                onClick  = onLanguageSelected,
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .alpha(contentAlpha.value)
            )
        }
    }
}

@Composable
private fun LanguageCard(
    language: LanguageOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue   = if (isSelected) NabhaBlue400 else DividerDark,
        animationSpec = tween(300),
        label         = "border"
    )
    val bgColor by animateColorAsState(
        targetValue   = if (isSelected) NabhaBlue500.copy(alpha = 0.12f) else CardDark,
        animationSpec = tween(300),
        label         = "bg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = language.nativeName,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = if (isSelected) TextPrimary else TextSecondary
            )
            Text(
                text    = "${language.name} · ${language.tagline}",
                fontSize = 13.sp,
                color   = if (isSelected) NabhaBlue300 else TextTertiary
            )
        }

        AnimatedVisibility(visible = isSelected) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NabhaBlue500),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Check,
                    null,
                    tint     = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
