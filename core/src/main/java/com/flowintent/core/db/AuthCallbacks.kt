package com.flowintent.core.db

data class AuthCallbacks(
    val onLoading: (Boolean) -> Unit,
    val onError: (String?) -> Unit,
    val onSuccess: () -> Unit
)
