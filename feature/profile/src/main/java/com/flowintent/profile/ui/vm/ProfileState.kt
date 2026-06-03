/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.ui.vm

import android.graphics.Bitmap
import android.net.Uri
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.util.Resource

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val profileBitmap: Bitmap? = null,
    val uploadState: Resource<String>? = null,
    val changePasswordState: Resource<Unit>? = null,
    val userUid: String? = null,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val selectedImageUri: Uri? = null,
    val isProfileLoading: Boolean = true
)
