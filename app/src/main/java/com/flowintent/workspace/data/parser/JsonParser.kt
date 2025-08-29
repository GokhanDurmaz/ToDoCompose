package com.flowintent.workspace.data.parser

interface JsonParser {
    fun <T> fromJson(json: String, classOfT: Class<T>): T
    fun <T> fromJsonList(json: String, classOfT: Class<T>): List<T>
}
