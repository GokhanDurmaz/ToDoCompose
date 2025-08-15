package com.flowintent.workspace.util

import kotlinx.serialization.json.Json

internal val TaskTypeConverterJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    isLenient = true
}