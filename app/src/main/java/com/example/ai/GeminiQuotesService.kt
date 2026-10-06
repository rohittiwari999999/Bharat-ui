package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.TemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiQuotesService {
  private const val TAG = "GeminiQuotesService"
  private const val MODEL_NAME = "gemini-3.5-flash"
  private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

  /**
   * Generates creative festival wishes, business promo text, or wedding captions.
   * Uses Gemini 3.5 Flash if API key is present; otherwise falls back to prebuilt repository.
   */
  suspend fun generateQuotes(
    category: String,
    businessType: String,
    language: String = "Hindi"
  ): List<String> = withContext(Dispatchers.IO) {
    val apiKey = try {
      BuildConfig.GEMINI_API_KEY
    } catch (e: Throwable) {
      ""
    }

    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      Log.d(TAG, "No valid Gemini API key configured, using rich fallback library")
      return@withContext getFallbackQuotes(category)
    }

    val prompt = """
      You are an expert copywriter for Indian festival posters, business banners, and wedding invitations.
      Create 4 short, poetic, high-impact quotes/wishes for a poster.
      Category: $category
      Business/Theme: $businessType
      Language: $language (Include beautiful Devanagari script for Hindi or elegant English)
      Requirements:
      - 1 to 2 lines maximum per quote.
      - Highly appealing and auspicious for Indian festivals/events.
      - Return ONLY a JSON array of 4 string quotes, like: ["Quote 1", "Quote 2", "Quote 3", "Quote 4"]
    """.trimIndent()

    try {
      val jsonBody = JSONObject().apply {
        val contents = JSONArray().apply {
          val contentObj = JSONObject().apply {
            val parts = JSONArray().apply {
              val partObj = JSONObject().apply {
                put("text", prompt)
              }
              put(partObj)
            }
            put("parts", parts)
          }
          put(contentObj)
        }
        put("contents", contents)

        val genConfig = JSONObject().apply {
          put("responseMimeType", "application/json")
          put("temperature", 0.7)
        }
        put("generationConfig", genConfig)
      }

      val request = Request.Builder()
        .url("$BASE_URL$MODEL_NAME:generateContent?key=$apiKey")
        .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
        .build()

      val response = okHttpClient.newCall(request).execute()
      val responseString = response.body?.string()

      if (!response.isSuccessful || responseString == null) {
        Log.w(TAG, "Gemini API request failed: ${response.code}, falling back to repository")
        return@withContext getFallbackQuotes(category)
      }

      val rootJson = JSONObject(responseString)
      val candidates = rootJson.optJSONArray("candidates")
      val firstCandidate = candidates?.optJSONObject(0)
      val content = firstCandidate?.optJSONObject("content")
      val parts = content?.optJSONArray("parts")
      val textResponse = parts?.optJSONObject(0)?.optString("text")

      if (!textResponse.isNullOrBlank()) {
        try {
          val cleanText = textResponse.trim().removePrefix("```json").removeSuffix("```").trim()
          val array = JSONArray(cleanText)
          val resultList = mutableListOf<String>()
          for (i in 0 until array.length()) {
            val q = array.getString(i)
            if (q.isNotBlank()) resultList.add(q)
          }
          if (resultList.isNotEmpty()) {
            return@withContext resultList
          }
        } catch (e: Exception) {
          Log.w(TAG, "Error parsing JSON array: ${e.message}")
        }
      }
      getFallbackQuotes(category)
    } catch (e: Exception) {
      Log.e(TAG, "Exception during Gemini API call: ${e.message}")
      getFallbackQuotes(category)
    }
  }

  private fun getFallbackQuotes(category: String): List<String> {
    return when {
      category.contains("Diwali", ignoreCase = true) -> TemplateRepository.prebuiltHindiQuotes["Diwali"] ?: emptyList()
      category.contains("Holi", ignoreCase = true) -> TemplateRepository.prebuiltHindiQuotes["Holi"] ?: emptyList()
      category.contains("Wedding", ignoreCase = true) -> TemplateRepository.prebuiltHindiQuotes["Wedding"] ?: emptyList()
      category.contains("Business", ignoreCase = true) || category.contains("Sale", ignoreCase = true) ->
        TemplateRepository.prebuiltHindiQuotes["Business"] ?: emptyList()
      else -> TemplateRepository.prebuiltHindiQuotes["Good Morning / Daily"] ?: emptyList()
    }
  }
}
