package com.nabha.telemedicine.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.components.*
import com.nabha.telemedicine.core.design.theme.*

@Composable
fun ProfileSetupScreen(onProfileSaved: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var bloodGroup by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, tween(600))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SurfaceDark, SurfaceDark2)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp)
                .alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            // Profile picture placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(NabhaBlue800)
                    .border(2.dp, NabhaBlue400, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.PersonAdd, null, tint = NabhaBlue300, modifier = Modifier.size(48.dp))
            }

            Spacer(Modifier.height(8.dp))
            Text("Add Photo (Optional)", color = NabhaBlue400, fontSize = 13.sp, fontWeight = FontWeight.Medium)

            Spacer(Modifier.height(28.dp))

            Text(
                "Complete Your Profile",
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
                textAlign  = TextAlign.Center
            )
            Text(
                "ਆਪਣੀ ਪ੍ਰੋਫਾਈਲ ਪੂਰੀ ਕਰੋ",
                fontSize  = 16.sp,
                color     = NabhaBlue300,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(36.dp))

            // Form fields
            NabhaTextField(
                value         = name,
                onValueChange = { name = it },
                hint          = "Full Name",
                label         = "Full Name *",
                leadingIcon   = Icons.Rounded.Person,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            Spacer(Modifier.height(16.dp))

            NabhaTextField(
                value         = village,
                onValueChange = { village = it },
                hint          = "Your Village / Town",
                label         = "Village / Town *",
                leadingIcon   = Icons.Rounded.LocationOn,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NabhaTextField(
                    value         = age,
                    onValueChange = { if (it.length <= 3) age = it.filter { c -> c.isDigit() } },
                    hint          = "Age",
                    label         = "Age *",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier      = Modifier.weight(1f)
                )
                NabhaTextField(
                    value         = bloodGroup,
                    onValueChange = { bloodGroup = it.uppercase().take(3) },
                    hint          = "A+, B-, O+...",
                    label         = "Blood Group",
                    modifier      = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))

            // Gender selector
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Gender *",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextSecondary,
                    modifier   = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Male", "Female", "Other").forEach { gender ->
                        GenderChip(
                            label      = gender,
                            isSelected = selectedGender == gender,
                            onClick    = { selectedGender = gender },
                            modifier   = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Info note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NabhaSaffron700.copy(alpha = 0.1f))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Info, null, tint = NabhaSaffron400, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    "This info helps doctors provide better care. District is set to Nabha by default.",
                    color     = TextSecondary,
                    fontSize  = 12.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.height(32.dp))

            NabhaButton(
                text      = "Save Profile",
                onClick   = {
                    isLoading = true
                    onProfileSaved()
                },
                enabled   = name.isNotBlank() && village.isNotBlank() && age.isNotBlank(),
                isLoading = isLoading,
                icon      = Icons.Rounded.Check
            )

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun GenderChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderCol by animateColorAsState(
        if (isSelected) NabhaBlue400 else DividerDark, tween(200), label = "gender_border"
    )
    val bgCol by animateColorAsState(
        if (isSelected) NabhaBlue500.copy(0.15f) else CardDark, tween(200), label = "gender_bg"
    )
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgCol)
            .border(1.5.dp, borderCol, RoundedCornerShape(12.dp))
            .clickable(MutableInteractionSource(), null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color      = if (isSelected) NabhaBlue300 else TextTertiary,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize   = 14.sp
        )
    }
}
