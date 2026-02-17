package com.flowintent.core.db

import androidx.annotation.Keep

@Keep
data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = ""
)
