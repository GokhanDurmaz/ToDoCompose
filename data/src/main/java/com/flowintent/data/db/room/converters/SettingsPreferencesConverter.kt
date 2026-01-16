package com.flowintent.data.db.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.flowintent.core.db.AppTheme
import com.google.gson.Gson
import javax.inject.Inject

@ProvidedTypeConverter
class SettingsPreferencesConverter @Inject constructor(
    private val gson: Gson
) {

    @TypeConverter
    fun fromAppTheme(appTheme: AppTheme): String {
        return gson.toJson(appTheme)
    }

    @TypeConverter
    fun toAppTheme(
        settingsPreferencesJson: String
    ): AppTheme {
        return gson.fromJson(settingsPreferencesJson, AppTheme::class.java)
    }
}
