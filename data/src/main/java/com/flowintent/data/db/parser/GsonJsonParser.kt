package com.flowintent.data.db.parser

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

class GsonJsonParser @Inject constructor(
    private val gson: Gson
): JsonParser {
    override fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    override fun <T> fromJsonList(
        json: String,
        classOfT: Class<T>
    ): List<T> {
        val type: Type = TypeToken.getParameterized(List::class.java, classOfT).type
        return gson.fromJson(json, type)
    }
}
