package com.flowintent.core.db.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val profileImageUrl: String? = null
)
