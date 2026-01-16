package com.flowintent.data.db.parser

internal interface JsonParser {
    fun <T> fromJson(json: String, classOfT: Class<T>): T
    fun <T> fromJsonList(json: String, classOfT: Class<T>): List<T>
}
