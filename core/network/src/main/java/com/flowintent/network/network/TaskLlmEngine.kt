package com.flowintent.network.network

import android.content.Context
import android.util.Log
import com.flowintent.core.db.TaskType
import com.flowintent.network.BuildConfig
import com.flowintent.network.data.GroqMessageRequest
import com.flowintent.network.data.GroqRequest
import com.flowintent.network.data.TaskExtraction
import com.flowintent.network.network.services.GroqApiService
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskLlmEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groqApiService: GroqApiService,
    private val gson: Gson
) {
    private val apiKey = "Bearer ${BuildConfig.GROQ_API_KEY}"
    private var cachedRules: JSONObject? = null

    init {
        loadRuleset()
    }

    private fun loadRuleset() {
        try {
            val jsonString = context.assets.open("ruleset.json").bufferedReader().use { it.readText() }
            cachedRules = JSONObject(jsonString)
        } catch (e: Exception) {
            Log.e("TaskLlmEngine", "Ruleset load error: ${e.message}")
        }
    }

    suspend fun extractTask(
        userInput: String,
        languageCode: String,
        onResult: (title: String, timeText: String?, category: TaskType) -> Unit
    ) = withContext(Dispatchers.IO) {

        val langCode = languageCode.take(2).lowercase()
        val langRule = cachedRules?.optJSONObject(langCode) ?: cachedRules?.optJSONObject("en")

        val currentDateTime = SimpleDateFormat("yyyy-MM-dd EEEE HH:mm", Locale.getDefault()).format(Date())

        val baseSystemPrompt = langRule?.optString("system_prompt") ?: "Extract task and time."
        val dynamicSystemPrompt = baseSystemPrompt.replace("{current_date}", currentDateTime)

        val request = GroqRequest(
            messages = listOf(
                GroqMessageRequest(role = "system", content = dynamicSystemPrompt),
                GroqMessageRequest(role = "user", content = userInput)
            )
        )

        try {
            val response = groqApiService.getCompletion(apiKey, request)
            val aiResponseContent = response.choices.firstOrNull()?.message?.content

            if (!aiResponseContent.isNullOrBlank()) {
                val cleanJson = aiResponseContent
                    .replace("```json", "")
                    .replace("```", "")
                    .trim()

                Log.d("TaskLlmEngine", "Raw AI Output: $cleanJson")

                val taskData = gson.fromJson(cleanJson, TaskExtraction::class.java)

                val finalTime = if (taskData.time == "null" || taskData.time.isNullOrBlank()) null else taskData.time

                val validatedCategory = try {
                    TaskType.valueOf(taskData.category?.uppercase() ?: "OTHER")
                } catch (e: Exception) {
                    Log.e("TaskLlmEngine", e.message.toString())
                    TaskType.OTHER
                }
                onResult(taskData.title, finalTime,validatedCategory)
            } else {
                onResult(userInput, null, TaskType.OTHER)
            }
        } catch (e: Exception) {
            Log.e("TaskLlmEngine", "Extraction Error: ${e.message}")
            onResult(userInput, null, TaskType.OTHER)
        }
    }
}
