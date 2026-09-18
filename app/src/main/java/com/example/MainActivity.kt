package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.DayanaraViewModel
import com.example.ui.components.AudioControlsSheet
import com.example.ui.components.DayanaraAvatarHeader
import com.example.ui.components.LegalGuideDialog
import com.example.ui.screens.AnonymousReportScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ReportsHistoryScreen
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DayanaraApp()
            }
        }
    }
}

@Composable
fun DayanaraApp(viewModel: DayanaraViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentTab by viewModel.currentTab.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Audio states
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()
    val pitch by viewModel.pitch.collectAsState()
    val micRms by viewModel.micRms.collectAsState()

    var showAudioSettings by remember { mutableStateOf(false) }
    var showLegalGuide by remember { mutableStateOf(false) }

    // Audio permission request launcher
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceInput()
        } else {
            Toast.makeText(
                context,
                "Se requiere permiso de micrófono para hablar con Dayanara.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Collect feedback toasts
    LaunchedEffect(Unit) {
        viewModel.userFeedback.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NavyDark,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DayanaraAvatarHeader(
                isSpeaking = isSpeaking,
                isPaused = isPaused,
                isListening = isListening,
                isMuted = isMuted,
                micRms = micRms,
                speechRate = speechRate,
                onToggleMute = { viewModel.toggleMute() },
                onPauseResume = { viewModel.pauseResumeSpeech() },
                onStopSpeech = { viewModel.stopSpeech() },
                onOpenAudioSettings = { showAudioSettings = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NavySurface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { viewModel.setTab(0) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Asesoría Penal",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Asesoría", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_item_chat")
                )

                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { viewModel.setTab(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Denuncia Anónima",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Denunciar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_item_report")
                )

                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { viewModel.setTab(2) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FolderShared,
                            contentDescription = "Mis Denuncias",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Mis Radicados", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_item_history")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> {
                    ChatScreen(
                        messages = messages,
                        isLoading = isLoading,
                        isListening = isListening,
                        onSendMessage = { prompt -> viewModel.sendMessage(prompt) },
                        onStartVoiceInput = {
                            val permission = Manifest.permission.RECORD_AUDIO
                            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                viewModel.startVoiceInput()
                            } else {
                                audioPermissionLauncher.launch(permission)
                            }
                        },
                        onStopVoiceInput = { viewModel.stopVoiceInput() },
                        onSpeakMessage = { text -> viewModel.speakMessage(text) },
                        onOpenLegalGuide = { showLegalGuide = true }
                    )
                }
                1 -> {
                    AnonymousReportScreen(
                        onSubmitReport = { report -> viewModel.insertReport(report) },
                        onNavigateToHistory = { viewModel.setTab(2) }
                    )
                }
                2 -> {
                    ReportsHistoryScreen(
                        reports = reports,
                        onDeleteReport = { id -> viewModel.deleteReport(id) },
                        onNavigateToNewReport = { viewModel.setTab(1) }
                    )
                }
            }
        }
    }

    // Audio Controls Modal Sheet
    if (showAudioSettings) {
        AudioControlsSheet(
            isSpeaking = isSpeaking,
            isPaused = isPaused,
            isMuted = isMuted,
            speechRate = speechRate,
            pitch = pitch,
            onToggleMute = { viewModel.toggleMute() },
            onPauseResume = { viewModel.pauseResumeSpeech() },
            onStopSpeech = { viewModel.stopSpeech() },
            onSetSpeechRate = { rate -> viewModel.setSpeechRate(rate) },
            onSetPitch = { pitchVal -> viewModel.setPitch(pitchVal) },
            onTestVoice = { viewModel.testVoice() },
            onDismiss = { showAudioSettings = false }
        )
    }

    // Legal & Evidence Guide Dialog
    if (showLegalGuide) {
        LegalGuideDialog(
            onDismiss = { showLegalGuide = false },
            onSelectTopicToAsk = { topic ->
                viewModel.setTab(0)
                viewModel.sendMessage(topic)
            }
        )
    }
}

@Composable
private fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = NavyDark,
    selectedTextColor = JusticeGold,
    indicatorColor = JusticeGold,
    unselectedIconColor = Color(0xFF94A3B8),
    unselectedTextColor = Color(0xFF94A3B8)
)
