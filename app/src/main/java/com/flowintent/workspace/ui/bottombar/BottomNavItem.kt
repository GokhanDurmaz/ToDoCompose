package com.flowintent.workspace.ui.bottombar

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.flowintent.navigation.nav.MainNavigation

data class BottomNavItem(
    val navigation: MainNavigation,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    @StringRes val contentDescriptionRes: Int
)
