package com.flowintent.workspace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.workspace.ui.vm.AuthViewModel
import com.flowintent.workspace.util.COLOR_0XFF0F0F1C
import com.flowintent.workspace.util.COLOR_0XFF1A1A2E
import com.flowintent.workspace.util.COLOR_0XFF1E1E2F
import com.flowintent.workspace.util.COLOR_0XFF2A2A3D
import com.flowintent.workspace.util.COLOR_0XFF7B2FF7
import com.flowintent.workspace.util.COLOR_0XFF9D4EDD
import com.flowintent.workspace.util.COLOR_0XFFE63946
import com.flowintent.workspace.util.VAL_0_5
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_16
import com.flowintent.workspace.util.VAL_20
import com.flowintent.workspace.util.VAL_32
import com.flowintent.workspace.util.VAL_40
import com.flowintent.workspace.util.VAL_60
import com.flowintent.workspace.util.VAL_8

@Preview(showBackground = true)
@Composable
fun SettingsScreen2() {
    AdvancedSettingsScreen()
}

@Composable
fun AdvancedSettingsScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val username by viewModel.userName.collectAsStateWithLifecycle()
    val email by viewModel.userEmail.collectAsStateWithLifecycle()
    var theme by remember { mutableStateOf("Dark") }
    var doNotDisturb by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(VAL_0_5) }

    LaunchedEffect(Unit) {
        viewModel.fetchAndSaveUserProfileIfEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(COLOR_0XFF0F0F1C))
            .verticalScroll(rememberScrollState())
            .padding(VAL_16.dp)
    ) {
        ProfileHeader(username ?: "", email ?: "")

        Spacer(modifier = Modifier.height(20.dp))

        ThemeSelection(currentTheme = theme, onThemeChange = { theme = it })

        DndSection(
            doNotDisturb = doNotDisturb,
            onDndChange = { doNotDisturb = it },
            sliderValue = sliderValue,
            onSliderChange = { sliderValue = it }
        )

        AboutSection()

        Spacer(Modifier.height(VAL_40.dp))

        LogoutButton(onLogout = { viewModel.clearAll() })
    }
}

@Composable
private fun ProfileHeader(username: String, email: String) {
    Card(
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(COLOR_0XFF1A1A2E))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(VAL_16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(VAL_60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(COLOR_0XFF7B2FF7), Color(COLOR_0XFF9D4EDD))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(VAL_32.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(username, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = Color.White)
                Text(email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
private fun ThemeSelection(currentTheme: String, onThemeChange: (String) -> Unit) {
    SettingsSection(title = "Theme") {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Light", "Dark", "Amoled").forEach { option ->
                val isSelected = currentTheme == option
                FilterChip(
                    selected = isSelected,
                    onClick = { onThemeChange(option) },
                    label = { Text(text = option, color = if (isSelected) Color.White else Color.Gray) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(COLOR_0XFF7B2FF7),
                        containerColor = Color(COLOR_0XFF2A2A3D)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) Color(COLOR_0XFF9D4EDD) else Color.Gray,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = true
                    )
                )
            }
        }
    }
}

@Composable
private fun DndSection(
    doNotDisturb: Boolean,
    onDndChange: (Boolean) -> Unit,
    sliderValue: Float,
    onSliderChange: (Float) -> Unit
) {
    SettingsSection(title = "Do Not Disturb") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable DND", color = Color.White)
            Spacer(Modifier.weight(1f))
            Switch(checked = doNotDisturb, onCheckedChange = onDndChange)
        }
        Spacer(Modifier.height(12.dp))
        Text("Intensity", color = Color.Gray)
        Slider(value = sliderValue, onValueChange = onSliderChange, valueRange = 0f..1f)
    }
}

@Composable
private fun AboutSection() {
    SettingsSection(title = "About") {
        Text("Version 1.0.0", color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        listOf("Privacy Policy", "Terms of Service", "Check for Updates", "Github").forEach {
            Text(it, color = Color.White)
        }
    }
}

@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(COLOR_0XFFE63946)),
        shape = RoundedCornerShape(VAL_12.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(VAL_8.dp))
        Text("Logout", color = Color.White)
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(VAL_16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VAL_8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(COLOR_0XFF1E1E2F))
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(VAL_12.dp))
            content()
        }
    }
}
