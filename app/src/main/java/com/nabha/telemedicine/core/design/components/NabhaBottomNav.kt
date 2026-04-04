package com.nabha.telemedicine.core.design.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.theme.*

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String
)

@Composable
fun NabhaBottomNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(CardDark2)
            .border(1.dp, DividerDark, RoundedCornerShape(28.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.route == selectedRoute
                BottomNavItemView(
                    item       = item,
                    isSelected = isSelected,
                    onClick    = { onItemSelected(item.route) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItemView(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val bgColor by animateColorAsState(
        targetValue   = if (isSelected) NabhaBlue500.copy(alpha = 0.15f) else Color.Transparent,
        animationSpec = spring(),
        label         = "nav_bg"
    )
    val iconColor by animateColorAsState(
        targetValue   = if (isSelected) NabhaBlue400 else TextTertiary,
        animationSpec = spring(),
        label         = "nav_icon"
    )
    val labelColor by animateColorAsState(
        targetValue   = if (isSelected) NabhaBlue400 else TextTertiary,
        animationSpec = spring(),
        label         = "nav_label"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector        = if (isSelected) item.selectedIcon else item.icon,
            contentDescription = item.label,
            tint               = iconColor,
            modifier           = Modifier.size(24.dp)
        )
        if (isSelected) {
            Spacer(Modifier.height(4.dp))
            Text(
                text       = item.label,
                color      = labelColor,
                fontSize   = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
