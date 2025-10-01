package com.flowintent.core.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.SettingsPreferences

@Dao
interface SettingsDao {
    @Insert
    suspend fun saveUserPreferences(settingsPreferences: SettingsPreferences)

    @Query("UPDATE SettingsPreferences SET theme = :appTheme WHERE uid = :uid")
    suspend fun updateUserPreferences(uid: Int, appTheme: AppTheme)
}
