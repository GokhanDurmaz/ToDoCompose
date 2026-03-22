package com.flowintent.network.network

import android.content.Context
import android.util.Log
import com.flowintent.core.db.model.ActionType
import com.flowintent.core.db.model.TaskType
import com.flowintent.network.data.groq.GroqMessageRequest
import com.flowintent.network.data.groq.GroqRequest
import com.flowintent.network.data.TaskExtraction
import com.flowintent.network.network.services.GroqApiService
import com.flowintent.network.util.NativeConfig
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
    private val apiKey = "Bearer ${NativeConfig.getGroqApiKey()}"
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
        onResult: (title: String, timeText: String?, category: TaskType, action: ActionType) -> Unit
    ) = withContext(Dispatchers.IO) {

        val langCode = languageCode.take(2).lowercase()
        val langRule = cachedRules?.optJSONObject(langCode) ?: cachedRules?.optJSONObject("en")
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd EEEE HH:mm", Locale.getDefault()).format(Date())

        val baseSystemPrompt = langRule?.optString("system_prompt") ?: "Extract tasks and time."
        val dynamicSystemPrompt = "${baseSystemPrompt.replace("{current_date}", currentDateTime)} " +
                "Use JSON array if multiple tasks exist."

        val request = GroqRequest(
            messages = listOf(
                GroqMessageRequest(role = "system", content = dynamicSystemPrompt),
                GroqMessageRequest(role = "user", content = userInput)
            ),
            temperature = 0.0
        )

        try {
            val response = groqApiService.getCompletion(apiKey, request)
            val aiContent = response.choices.firstOrNull()?.message?.content
            Log.d("TaskLlmEngine", aiContent.toString())

            if (!aiContent.isNullOrBlank()) {
                val jsonStart = aiContent.indexOfAny(charArrayOf('[', '{'))
                val jsonEnd = aiContent.lastIndexOfAny(charArrayOf(']', '}'))

                if (jsonStart != -1 && jsonEnd != -1) {
                    val cleanJson = aiContent.substring(jsonStart, jsonEnd + 1).trim()
                    val extractedTasks = mutableListOf<TaskExtraction>()

                    if (cleanJson.startsWith("[")) {
                        val taskArray = gson.fromJson(cleanJson, Array<TaskExtraction>::class.java)
                        extractedTasks.addAll(taskArray)
                    } else {
                        val singleTask = gson.fromJson(cleanJson, TaskExtraction::class.java)
                        extractedTasks.add(singleTask)
                    }

                    extractedTasks.forEach { taskData ->
                        val finalTime = if (taskData.time == "null" || taskData.time.isNullOrBlank()) null else taskData.time
                        val validatedCategory = try {
                            TaskType.valueOf(taskData.category.uppercase())
                        } catch (e: Exception) {
                            Log.e("TaskLlmEngine", e.message.toString())
                            TaskType.OTHER
                        }

                        val validatedAction = try {
                            ActionType.valueOf(taskData.action.uppercase())
                        } catch (e: Exception) {
                            Log.e("TaskLlmEngine", e.message.toString())
                            ActionType.ADD
                        }

                        onResult(taskData.title, finalTime, validatedCategory, validatedAction)
                    }
                } else {
                    onResult(userInput, null, TaskType.OTHER, ActionType.ADD)
                }
            }
        } catch (e: Exception) {
            Log.e("TaskLlmEngine", "Extraction Error: ${e.message}")
            onResult(userInput, null, TaskType.OTHER, ActionType.ADD)
        }
    }
}
