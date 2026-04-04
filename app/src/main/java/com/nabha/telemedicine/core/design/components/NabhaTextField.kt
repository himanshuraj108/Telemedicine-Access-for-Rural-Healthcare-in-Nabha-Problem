package com.nabha.telemedicine.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabha.telemedicine.core.design.theme.*

@Composable
fun NabhaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    label: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = 1,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text       = label,
                color      = TextSecondary,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier   = Modifier.padding(bottom = 8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(CardDark2)
        ) {
            Row(
                modifier          = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector        = leadingIcon,
                        contentDescription = null,
                        tint               = TextTertiary,
                        modifier           = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text       = hint,
                            color      = TextTertiary,
                            fontSize   = 15.sp
                        )
                    }
                    BasicTextField(
                        value                = value,
                        onValueChange        = onValueChange,
                        textStyle            = TextStyle(
                            color      = TextPrimary,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        keyboardOptions      = keyboardOptions,
                        keyboardActions      = keyboardActions,
                        visualTransformation = visualTransformation,
                        maxLines             = maxLines,
                        enabled              = enabled,
                        cursorBrush          = SolidColor(NabhaBlue400),
                        modifier             = Modifier.fillMaxWidth()
                    )
                }

                if (trailingIcon != null) {
                    Spacer(Modifier.width(12.dp))
                    trailingIcon()
                }
            }
        }
    }
}
