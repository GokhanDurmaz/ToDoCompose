package com.flowintent.workspace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun SettingsScreen2() {
    AdvancedSettingsScreen()
}

@Composable
fun AdvancedSettingsScreen(
    username: String = "Luther Castel",
    email: String = "luther@email.com",
    onLogout: () -> Unit = {}
) {
    var theme by remember { mutableStateOf("Dark") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var doNotDisturb by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F1C)) // koyu arka plan
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 🔹 Header - Profil
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF7B2FF7), Color(0xFF9D4EDD))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(username, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Theme Selection
        SettingsSection(title = "Theme") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("Light", "Dark", "Amoled").forEach { option ->
                    val isSelected = theme == option

                    FilterChip(
                        selected = isSelected,
                        onClick = { theme = option },
                        label = {
                            Text(
                                text = option,
                                color = if (isSelected) Color.White else Color.Gray
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF7B2FF7), // seçili arka plan
                            containerColor = Color(0xFF2A2A3D)          // normal arka plan
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) Color(0xFF9D4EDD) else Color.Gray,
                            borderWidth = 1.dp,
                            enabled = true,
                            selected = true
                        )
                    )
                }
            }
        }


        // 🔹 Notifications
        SettingsSection(title = "Do Not Disturb") {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable DND", color = Color.White)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = doNotDisturb,
                    onCheckedChange = { doNotDisturb = it }
                )
            }
            Spacer(Modifier.height(12.dp))
            Text("Intensity", color = Color.Gray)
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..1f
            )
        }

        // 🔹 About
        SettingsSection(title = "About") {
            Text("Version 1.0.0", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            Text("Privacy Policy", color = Color.White)
            Text("Terms of Service", color = Color.White)
            Text("Check for Updates", color = Color.White)
            Text("Github", color = Color.White)
        }

        Spacer(Modifier.height(40.dp))

        // 🔹 Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFe63946)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Logout", color = Color.White)
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
