package com.flowintent.core.db.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flowintent.core.db.AppTheme

@Entity
data class SettingsPreferences(
    @PrimaryKey val uid: String,
    @ColumnInfo("theme") val theme: AppTheme
)