package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.DayanaraVoiceManager
import com.example.data.local.ChatMessageEntity
import com.example.data.local.DayanaraDatabase
import com.example.data.local.ReportEntity
import com.example.data.remote.GeminiLegalService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DayanaraViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DayanaraDatabase.getDatabase(application)
    private val reportDao = db.reportDao()
    private val chatDao = db.chatDao()

    val voiceManager = DayanaraVoiceManager(application)

    // Active screen tab: 0 -> Chat, 1 -> Nueva Denuncia, 2 -> Historial Denuncias
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _userFeedback = MutableSharedFlow<String>()
    val userFeedback: SharedFlow<String> = _userFeedback.asSharedFlow()

    val messages: StateFlow<List<ChatMessageEntity>> = chatDao.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<ReportEntity>> = reportDao.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isSpeaking = voiceManager.isSpeaking
    val isPaused = voiceManager.isPaused
    val isListening = voiceManager.isListening
    val isMuted = voiceManager.isMuted
    val speechRate = voiceManager.speechRate
    val pitch = voiceManager.pitch
    val micRms = voiceManager.micRms

    init {
        // Voice recognition listener
        voiceManager.onSpeechRecognized = { recognizedText ->
            sendMessage(recognizedText)
        }
        voiceManager.onSpeechError = { errorMsg ->
            viewModelScope.launch {
                _userFeedback.emit(errorMsg)
            }
        }

        // Insert initial welcome message if chat history is empty
        viewModelScope.launch {
            chatDao.getAllMessages().collect { list ->
                if (list.isEmpty()) {
                    val welcomeMsg = ChatMessageEntity(
                        sender = "DAYANARA",
                        content = "Hola, soy Dayanara, tu asesora legal especializada en tipificación de delitos de corrupción y preservación de evidencias. Puedes preguntarme en voz alta o texto sobre sobornos, peculado, malversación, colusión o radicar una denuncia 100% anónima.",
                        timestamp = System.currentTimeMillis()
                    )
                    chatDao.insertMessage(welcomeMsg)
                }
            }
        }
    }

    fun setTab(tab: Int) {
        _currentTab.value = tab
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            // Save user message
            val userMsg = ChatMessageEntity(
                sender = "USER",
                content = content.trim(),
                timestamp = System.currentTimeMillis()
            )
            chatDao.insertMessage(userMsg)

            _isLoading.value = true

            // Stop any ongoing speech while thinking
            voiceManager.stop()

            val response = GeminiLegalService.getLegalCounsel(content)

            // Save Dayanara response
            val dayanaraMsg = ChatMessageEntity(
                sender = "DAYANARA",
                content = response,
                timestamp = System.currentTimeMillis()
            )
            chatDao.insertMessage(dayanaraMsg)

            _isLoading.value = false

            // Voice synthesis in real time (Dayanara responds speaking aloud in Spanish)
            if (!voiceManager.isMuted.value) {
                voiceManager.speak(response)
            }
        }
    }

    fun speakMessage(text: String) {
        voiceManager.speak(text)
    }

    fun toggleMute() {
        voiceManager.toggleMute()
    }

    fun pauseResumeSpeech() {
        if (voiceManager.isPaused.value) {
            voiceManager.resume()
        } else {
            voiceManager.pause()
        }
    }

    fun stopSpeech() {
        voiceManager.stop()
    }

    fun setSpeechRate(rate: Float) {
        voiceManager.setSpeechRate(rate)
    }

    fun setPitch(pitchVal: Float) {
        voiceManager.setPitch(pitchVal)
    }

    fun startVoiceInput() {
        voiceManager.startListening()
    }

    fun stopVoiceInput() {
        voiceManager.stopListening()
    }

    fun testVoice() {
        voiceManager.speak("Hola, soy Dayanara, tu asesora legal anticorrupción. La voz y tono están configurados correctamente.")
    }

    fun insertReport(report: ReportEntity) {
        viewModelScope.launch {
            reportDao.insertReport(report)
            _userFeedback.emit("Denuncia anónima ${report.reportCode} registrada exitosamente.")
        }
    }

    fun deleteReport(id: Long) {
        viewModelScope.launch {
            reportDao.deleteReport(id)
            _userFeedback.emit("Reporte eliminado localmente.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.destroy()
    }
}
