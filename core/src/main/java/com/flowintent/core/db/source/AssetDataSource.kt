package com.flowintent.core.db.source

interface AssetDataSource {
    suspend fun readJsonString(fileName: String): String
}
