/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.security

interface SecurePrefsRepository {
    suspend fun saveToken(token: String)
}
