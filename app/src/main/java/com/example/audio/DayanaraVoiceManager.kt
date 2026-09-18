package com.example.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class DayanaraVoiceManager(private val context: Context) : TextToSpeech.OnInitListener {

    private val TAG = "DayanaraVoiceManager"

    private var tts: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null

    // TTS States
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _pitch = MutableStateFlow(1.05f)
    val pitch: StateFlow<Float> = _pitch.asStateFlow()

    // Speech Recognition States
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _micRms = MutableStateFlow(0f)
    val micRms: StateFlow<Float> = _micRms.asStateFlow()

    var onSpeechRecognized: ((String) -> Unit)? = null
    var onSpeechError: ((String) -> Unit)? = null

    private var lastTextToSpeak: String = ""
    private var isInitialized = false

    init {
        tts = TextToSpeech(context.applicationContext, this)
        initSpeechRecognizer()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to generic Spanish or default
                val fallback = tts?.setLanguage(Locale("es"))
                if (fallback == TextToSpeech.LANG_MISSING_DATA || fallback == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(TAG, "Spanish TTS not fully supported, falling back to default locale")
                    tts?.language = Locale.getDefault()
                }
            }

            tts?.setSpeechRate(_speechRate.value)
            tts?.setPitch(_pitch.value)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                    _isPaused.value = false
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _isPaused.value = false
                }
            })

            _isTtsReady.value = true
            isInitialized = true
        } else {
            Log.e(TAG, "Failed to initialize TextToSpeech (status: $status)")
            _isTtsReady.value = false
        }
    }

    private fun initSpeechRecognizer() {
        try {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {
                            _isListening.value = true
                        }

                        override fun onBeginningOfSpeech() {}

                        override fun onRmsChanged(rmsdB: Float) {
                            _micRms.value = rmsdB.coerceAtLeast(0f)
                        }

                        override fun onBufferReceived(buffer: ByteArray?) {}

                        override fun onEndOfSpeech() {
                            _isListening.value = false
                            _micRms.value = 0f
                        }

                        override fun onError(error: Int) {
                            _isListening.value = false
                            _micRms.value = 0f
                            val errorMsg = when (error) {
                                SpeechRecognizer.ERROR_NO_MATCH -> "No se detectó audio claro. Intenta hablar nuevamente."
                                SpeechRecognizer.ERROR_NETWORK -> "Error de red en el reconocimiento de voz."
                                SpeechRecognizer.ERROR_AUDIO -> "Error al acceder al micrófono."
                                else -> "Error de entrada de voz ($error)"
                            }
                            Log.w(TAG, "SpeechRecognizer error: $errorMsg ($error)")
                            onSpeechError?.invoke(errorMsg)
                        }

                        override fun onResults(results: Bundle?) {
                            _isListening.value = false
                            _micRms.value = 0f
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            val recognized = matches?.firstOrNull()
                            if (!recognized.isNullOrBlank()) {
                                onSpeechRecognized?.invoke(recognized.trim())
                            }
                        }

                        override fun onPartialResults(partialResults: Bundle?) {}
                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                }
            } else {
                Log.w(TAG, "Speech recognition is not available on this device.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating SpeechRecognizer: ${e.message}", e)
        }
    }

    fun speak(text: String) {
        if (_isMuted.value) {
            Log.d(TAG, "Muted. Skipping speech.")
            return
        }

        lastTextToSpeak = text
        _isPaused.value = false

        // Clean markdown and non-spoken characters for clean speech
        val cleanedText = cleanForSpeech(text)

        tts?.setSpeechRate(_speechRate.value)
        tts?.setPitch(_pitch.value)

        val utteranceId = "Dayanara_${System.currentTimeMillis()}"
        tts?.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun pause() {
        if (_isSpeaking.value) {
            tts?.stop()
            _isSpeaking.value = false
            _isPaused.value = true
        }
    }

    fun resume() {
        if (_isPaused.value && lastTextToSpeak.isNotBlank()) {
            speak(lastTextToSpeak)
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _isPaused.value = false
    }

    fun toggleMute() {
        val newMute = !_isMuted.value
        _isMuted.value = newMute
        if (newMute) {
            stop()
        }
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate.coerceIn(0.5f, 2.0f)
        tts?.setSpeechRate(_speechRate.value)
    }

    fun setPitch(pitchVal: Float) {
        _pitch.value = pitchVal.coerceIn(0.5f, 1.8f)
        tts?.setPitch(_pitch.value)
    }

    fun startListening() {
        // Pause any ongoing TTS speech so microphone doesn't pick up Dayanara's voice
        stop()

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES")
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }
            speechRecognizer?.startListening(intent)
            _isListening.value = true
        } catch (e: Exception) {
            Log.e(TAG, "Error starting voice recognition: ${e.message}", e)
            _isListening.value = false
            onSpeechError?.invoke("No se pudo iniciar el micrófono: ${e.message}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping speech recognition: ${e.message}", e)
        }
        _isListening.value = false
        _micRms.value = 0f
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
        speechRecognizer?.destroy()
    }

    private fun cleanForSpeech(input: String): String {
        return input
            .replace(Regex("[#*`_~>\\[\\]()]"), "")
            .replace(Regex("[✓•📌⚖️🛡️🔍⏳📁💡🎙️💬🔐📑🔒]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
