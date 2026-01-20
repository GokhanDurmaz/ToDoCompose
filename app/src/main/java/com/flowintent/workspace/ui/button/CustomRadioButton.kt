package com.flowintent.workspace.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.flowintent.workspace.util.COLOR_0XFF42A5F5
import com.flowintent.workspace.util.VAL_0_4
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_2
import com.flowintent.workspace.util.VAL_20

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(COLOR_0XFF42A5F5)

    Box(
        modifier = modifier
            .size(VAL_20.dp)
            .clip(CircleShape)
            .background(
                if (selected) selectedColor else Color.Transparent
            )
            .border(
                width = VAL_2.dp,
                color = if (selected) selectedColor
                else MaterialTheme.colorScheme.onSurface.copy(alpha = VAL_0_4),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(VAL_12.dp)
            )
        }
    }
}
