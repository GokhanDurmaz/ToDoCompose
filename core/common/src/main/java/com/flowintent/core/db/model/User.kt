package com.flowintent.core.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    val name: String,
    val mail: String,
    val profile: String
)
