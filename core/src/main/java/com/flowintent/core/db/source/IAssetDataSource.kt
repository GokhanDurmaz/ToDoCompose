package com.flowintent.core.db.source

interface IAssetDataSource {
    suspend fun readJsonString(fileName: String): String
}
