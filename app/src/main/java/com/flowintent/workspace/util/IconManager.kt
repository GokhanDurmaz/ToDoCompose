package com.flowintent.workspace.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconManager {
    val iconMap: Map<String, ImageVector> = mapOf(
        "SportsGymnastics" to Icons.Default.SportsGymnastics,
        "Home" to Icons.Default.Home,
        "Add" to Icons.Default.Add,
        "Delete" to Icons.Default.Delete,
        "Search" to Icons.Default.Search,
        "ArtTrack" to Icons.Default.ArtTrack,
        "Send" to Icons.AutoMirrored.Filled.Send,
        "HealthAndSafety" to Icons.Default.HealthAndSafety
    )

    fun getIcon(iconName: String): ImageVector {
        return iconMap[iconName] ?: Icons.Default.DeviceUnknown
    }
}
