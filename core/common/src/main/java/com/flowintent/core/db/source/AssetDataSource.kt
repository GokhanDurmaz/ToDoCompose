/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.source

interface AssetDataSource {
    suspend fun readJsonString(fileName: String): String
}
