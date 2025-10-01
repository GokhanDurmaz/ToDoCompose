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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun SettingsScreen() {
    val isDark = isSystemInDarkTheme()
    val iconColor = if (isDark) Color(0xFF90CAF9) else Color(0xFF1976D2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, top = 30.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings Icon",
                tint = iconColor,
                modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
                fontSize = 40.sp
            )
        }
        Row(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Account Icon",
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Account",
                fontSize = 26.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CircleNotifications,
                contentDescription = "Notification Icon",
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Notifications",
                fontSize = 26.sp,
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(36.dp)
                    .weight(0.2f)
            )

        }
        Row(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = "Theme Icon",
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Theme",
                fontSize = 26.sp,
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(36.dp)
                    .weight(0.2f)
            )
        }
        Row(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "About Icon",
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "About",
                fontSize = 26.sp,
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(36.dp)
                    .weight(0.2f)
            )
        }
    }
}
