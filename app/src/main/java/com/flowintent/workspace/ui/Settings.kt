package com.flowintent.workspace.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CircleNotifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flowintent.workspace.util.COLOR_0XFF1976D2
import com.flowintent.workspace.util.COLOR_0XFF90CAF9
import com.flowintent.workspace.util.VAL_0_2
import com.flowintent.workspace.util.VAL_0_8
import com.flowintent.workspace.util.VAL_10
import com.flowintent.workspace.util.VAL_20
import com.flowintent.workspace.util.VAL_24
import com.flowintent.workspace.util.VAL_26
import com.flowintent.workspace.util.VAL_30
import com.flowintent.workspace.util.VAL_36
import com.flowintent.workspace.util.VAL_40
import com.flowintent.workspace.util.VAL_54
import com.flowintent.workspace.util.VAL_8

@Preview(showBackground = true)
@Composable
fun SettingsScreen() {
    val isDark = isSystemInDarkTheme()
    val iconColor = if (isDark) Color(COLOR_0XFF90CAF9) else Color(COLOR_0XFF1976D2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = VAL_30.dp, top = VAL_30.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        SettingsHeader(iconColor)
        SettingsItem(Icons.Default.AccountCircle, "Account", iconColor, showChevron = false)
        SettingsItem(Icons.Default.CircleNotifications, "Notifications", iconColor)
        SettingsItem(Icons.Default.LightMode, "Theme", iconColor)
        SettingsItem(Icons.Default.Info, "About", iconColor)
    }
}

@Composable
fun SettingsHeader(iconColor: Color) {
    Row(
        modifier = Modifier.padding(bottom = VAL_30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings Icon",
            tint = iconColor,
            modifier = Modifier.size(VAL_54.dp)
        )
        Spacer(modifier = Modifier.width(VAL_8.dp))
        Text(text = "Settings", fontSize = VAL_40.sp)
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    iconColor: Color,
    showChevron: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VAL_20.dp, horizontal = VAL_10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(VAL_36.dp)
        )
        Spacer(modifier = Modifier.width(VAL_8.dp))
        Text(
            text = title,
            fontSize = VAL_26.sp,
            modifier = Modifier.weight(VAL_0_8)
        )
        if (showChevron) {
            Spacer(modifier = Modifier.width(VAL_24.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier
                    .size(VAL_36.dp)
                    .weight(VAL_0_2)
            )
        } else {
            // Maintains alignment with items that have chevrons
            Spacer(modifier = Modifier.weight(VAL_0_2))
        }
    }
}
