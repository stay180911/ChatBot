package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.legal.LegalKnowledgeBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiLegalService {
    private const val TAG = "GeminiLegalService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_PROMPT = """
        Eres Dayanara, una brillante abogada penalista y asesora técnica especializada en delitos de corrupción en la función pública y preservación probatoria.
        Tus áreas de especialidad son:
        1. Tipificación penal precisa: Sobornos y Cohecho (activo/pasivo), Peculado (doloso/culposo/uso), Malversación presupuestaria, Colusión en licitaciones y contrataciones públicas, Nepotismo e incompatibilidades funcionales, Tráfico de influencias y Enriquecimiento ilícito.
        2. Protocolos de preservación de evidencia: Cadena de custodia digital, hashes criptográficos SHA-256, legalidad de grabaciones de audio/video realizadas por el propio interlocutor, exportación de chats crudos (WhatsApp/Signal) y cabeceras de correos electrónicos.
        3. Blindaje del anonimato del denunciante y advertencias de seguridad física y laboral.
        
        Instrucciones de respuesta:
        - Responde siempre en español formal, seguro, didáctico y empático.
        - Usa viñetas estructuradas y emojis sobrios para facilitar la lectura.
        - Indica siempre el posible tipo penal, elementos objetivos/subjetivos del delito, y qué evidencias concretas se deben recaudar con seguridad.
        - Mantén tus respuestas claras y directas, óptimas tanto para lectura visual como para ser locutadas por síntesis de voz (TTS).
    """

    suspend fun getLegalCounsel(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "No valid Gemini API key found. Using specialized Legal Knowledge Base.")
            return@withContext LegalKnowledgeBase.getDirectOrientation(prompt)
        }

        try {
            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val systemInstructionObj = JSONObject().apply {
                    val sysParts = JSONArray().apply {
                        put(JSONObject().put("text", SYSTEM_PROMPT.trimIndent()))
                    }
                    put("parts", sysParts)
                }
                put("systemInstruction", systemInstructionObj)

                val generationConfig = JSONObject().apply {
                    put("temperature", 0.3)
                    put("topP", 0.95)
                    put("maxOutputTokens", 1200)
                }
                put("generationConfig", generationConfig)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (!text.isNullOrBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }

            Log.w(TAG, "Gemini API call failed with code: ${response.code}. Falling back to knowledge base.")
            LegalKnowledgeBase.getDirectOrientation(prompt)
        } catch (e: Exception) {
            Log.e(TAG, "Error in getLegalCounsel: ${e.message}", e)
            LegalKnowledgeBase.getDirectOrientation(prompt)
        }
    }
}
