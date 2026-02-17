package com.flowintent.workspace.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

const val VAL_0_0 = 0f
const val VAL_0_1 = 0.1f
const val VAL_0_2 = 0.2f
const val VAL_0_3 = 0.3f
const val VAL_0_4 = 0.4f
const val VAL_0_5 = 0.5f
const val VAL_0_7 = 0.7f
const val VAL_0_8 = 0.8f
const val VAL_1_0 = 1f
const val VAL_1_0_2 = 1.02f
const val VAL_2_0 = 2f

const val VAL_0 = 0
const val VAL_2 = 2
const val VAL_4: Int = 4
const val VAL_5: Int = 5
const val VAL_7: Int = 7
const val VAL_8 = 8
const val VAL_10: Int = 10
const val VAL_12: Int = 12
const val VAL_16: Int = 16
const val VAL_20: Int = 20
const val VAL_22: Int = 22
const val VAL_23: Int = 23
const val VAL_24: Int = 24
const val VAL_25: Int = 25
const val VAL_26: Int = 26
const val VAL_28: Int = 28
const val VAL_30: Int = 30
const val VAL_32: Int = 32
const val VAL_35: Int = 35
const val VAL_36: Int = 36
const val VAL_40: Int = 40
const val VAL_45: Int = 45
const val VAL_50: Int = 50
const val VAL_52: Int = 52
const val VAL_54: Int = 54
const val VAL_55: Int = 55
const val VAL_60: Int = 60
const val VAL_72: Int = 72
const val VAL_75: Int = 75
const val VAL_80: Int = 80
const val VAL_200: Int = 200
const val VAL_255: Int = 255
const val VAL_300 = 300

const val COLOR_0XFF90CAF9 = 0xFF90CAF9
const val COLOR_0XFF1976D2 = 0xFF1976D2
const val COLOR_0XFFEF5350 = 0xFFEF5350
const val COLOR_0XFF0F0F1C = 0xFF0F0F1C
const val COLOR_0XFF42A5F5 = 0xFF42A5F5
const val COLOR_0XFF1A1A2E = 0xFF1A1A2E
const val COLOR_0XFF7B2FF7 = 0xFF7B2FF7
const val COLOR_0XFF9D4EDD = 0xFF9D4EDD
const val COLOR_0XFF2A2A3D = 0xFF2A2A3D
const val COLOR_0XFFE63946 = 0xFFe63946
const val COLOR_0XFF1E1E2F = 0xFF1E1E2F
const val COLOR_0XFF003366 = 0xFF003366
const val COLOR_0XFF74C3F3 = 0xFF74C3F3

fun String.isValidEmail(): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun changeLanguage(languageCode: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
