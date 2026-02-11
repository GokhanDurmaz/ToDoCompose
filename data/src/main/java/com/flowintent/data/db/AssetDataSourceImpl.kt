package com.flowintent.data.db

import android.content.Context
import android.util.Log
import com.flowintent.core.db.source.AssetDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

internal class AssetDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): AssetDataSource {

    override suspend fun readJsonString(fileName: String): String {
        return withContext(Dispatchers.IO) {
            try {
                context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                Log.e(TAG, ioException.message ?: "ioException while reading the asset file.")
                ""
            }
        }
    }

    companion object {
        private val TAG = AssetDataSourceImpl::class.simpleName
    }
}
