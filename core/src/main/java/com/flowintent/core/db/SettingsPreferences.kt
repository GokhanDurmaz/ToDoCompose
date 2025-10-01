package com.flowintent.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsPreferences(
    @PrimaryKey val uid: String,
    @ColumnInfo("theme") val theme: AppTheme
)
