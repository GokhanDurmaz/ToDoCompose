/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.Home
import com.flowintent.uikit.util.IconManager
import org.junit.Assert.assertEquals
import org.junit.Test

class IconManagerTest {

    @Test
    fun `getIcon returns correct icon for valid name`() {
        val icon = IconManager.getIcon("Home")
        assertEquals(Icons.Default.Home, icon)
    }

    @Test
    fun `getIcon returns DeviceUnknown for invalid name`() {
        val icon = IconManager.getIcon("InvalidName")
        assertEquals(Icons.Default.DeviceUnknown, icon)
    }
}
