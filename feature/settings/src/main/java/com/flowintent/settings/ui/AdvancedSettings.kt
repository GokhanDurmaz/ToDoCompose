package com.flowintent.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.settings.R
import com.flowintent.settings.ui.vm.SettingsViewModel
import com.flowintent.uikit.util.VAL_0_5
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_40
import com.flowintent.uikit.util.VAL_60
import com.flowintent.uikit.util.VAL_8

@Composable
fun AdvancedSettingsScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val username by authViewModel.userName.collectAsStateWithLifecycle()
    val email by authViewModel.userEmail.collectAsStateWithLifecycle()
    var theme by remember { mutableStateOf("Dark") }
    var doNotDisturb by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(VAL_0_5) }
    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()

    LaunchedEffect(Unit) {
        authViewModel.fetchAndSaveUserProfileIfEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(VAL_16.dp)
    ) {
        ProfileHeader(
            username ?: "",
            email ?: "",
            onProfileClick = { settingsViewModel.onProfileClicked() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LanguageSelection(currentLanguage = currentLocale) { selectedLocale ->
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(selectedLocale)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        ThemeSelection(currentTheme = theme, onThemeChange = { theme = it })

        DndSection(
            doNotDisturb = doNotDisturb,
            onDndChange = { doNotDisturb = it },
            sliderValue = sliderValue,
            onSliderChange = { sliderValue = it }
        )

        AboutSection()

        Spacer(Modifier.height(VAL_40.dp))

        LogoutButton(onLogout = {
            authViewModel.onLogoutClicked()
        })
    }
}

@Composable
private fun ProfileHeader(username: String, email: String, onProfileClick: () -> Unit) {
    Card(
        onClick = onProfileClick,
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(VAL_32.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LanguageSelection(currentLanguage: String, onLanguageChange: (String) -> Unit) {
    val names = stringArrayResource(R.array.language_names)
    val codes = stringArrayResource(R.array.language_codes)

    val languages = remember(names, codes) {
        codes.zip(names)
    }

    SettingsSection(title = stringResource(R.string.language_label)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            languages.forEach { (code, name) ->
                val isSelected = if (currentLanguage.isEmpty()) code == "en" else currentLanguage.startsWith(code)
                FilterChip(
                    selected = isSelected,
                    onClick = { onLanguageChange(code) },
                    label = {
                        Text(
                            text = name,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }
    }
}

@Composable
private fun ThemeSelection(currentTheme: String, onThemeChange: (String) -> Unit) {
    SettingsSection(title = stringResource(R.string.theme_label)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Light", "Dark", "Amoled").forEach { option ->
                val isSelected = currentTheme == option
                FilterChip(
                    selected = isSelected,
                    onClick = { onThemeChange(option) },
                    label = {
                        Text(
                            text = option,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
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
    SettingsSection(title = stringResource(R.string.dnd_label)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.enable_dnd), color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.weight(1f))
            Switch(checked = doNotDisturb, onCheckedChange = onDndChange)
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.intensity), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Slider(value = sliderValue, onValueChange = onSliderChange, valueRange = 0f..1f)
    }
}

@Composable
private fun AboutSection() {
    SettingsSection(title = stringResource(R.string.about_label)) {
        Text(stringResource(R.string.version_label), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        val aboutItems = listOf(
            stringResource(R.string.privacy_policy),
            stringResource(R.string.terms_service),
            stringResource(R.string.check_updates),
            "Github"
        )
        aboutItems.forEach { item ->
            Text(
                item,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        shape = RoundedCornerShape(VAL_12.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.onError)
        Spacer(Modifier.width(VAL_8.dp))
        Text(stringResource(R.string.logout), color = MaterialTheme.colorScheme.onError)
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(VAL_16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VAL_8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(VAL_12.dp))
            content()
        }
    }
}
