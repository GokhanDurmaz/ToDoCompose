/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.model

data class AuthCallbacks(
    val onLoading: (Boolean) -> Unit,
    val onError: (String?) -> Unit,
    val onSuccess: () -> Unit
)
