package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavySurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioControlsSheet(
    isSpeaking: Boolean,
    isPaused: Boolean,
    isMuted: Boolean,
    speechRate: Float,
    pitch: Float,
    onToggleMute: () -> Unit,
    onPauseResume: () -> Unit,
    onStopSpeech: () -> Unit,
    onSetSpeechRate: (Float) -> Unit,
    onSetPitch: (Float) -> Unit,
    onTestVoice: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavySurface,
        dragHandle = null,
        modifier = Modifier.testTag("audio_controls_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = JusticeGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Controles de Voz de Dayanara",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mute Switch Card
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = if (isMuted) Color.Gray else JusticeGold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Síntesis de Voz en Voz Alta",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isMuted) "Silenciada (Solo texto)" else "Activa (Dayanara habla)",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Switch(
                        checked = !isMuted,
                        onCheckedChange = { onToggleMute() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = JusticeGold,
                            checkedTrackColor = Color(0xFF1E3A66)
                        ),
                        modifier = Modifier.testTag("switch_mute_audio")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Playback Actions (Play / Pause / Stop)
            Text(
                text = "Control de Reproducción Activa",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPauseResume,
                    colors = ButtonDefaults.buttonColors(containerColor = JusticeGold, contentColor = NavyDark),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_modal_pause_resume")
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isPaused) "Reanudar" else "Pausar", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = onStopSpeech,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_modal_stop")
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detener", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Speech Rate Control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = CyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Velocidad de Lectura",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "${String.format("%.2f", speechRate)}x",
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent,
                    fontSize = 14.sp
                )
            }

            Slider(
                value = speechRate,
                onValueChange = onSetSpeechRate,
                valueRange = 0.5f..2.0f,
                steps = 5,
                colors = SliderDefaults.colors(
                    thumbColor = CyanAccent,
                    activeTrackColor = CyanAccent,
                    inactiveTrackColor = NavyCard
                ),
                modifier = Modifier.testTag("slider_speech_rate")
            )

            // Preset rate chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { rate ->
                    FilterChip(
                        selected = kotlin.math.abs(speechRate - rate) < 0.05f,
                        onClick = { onSetSpeechRate(rate) },
                        label = { Text("${rate}x", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanAccent,
                            selectedLabelColor = NavyDark,
                            containerColor = NavyCard,
                            labelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pitch Control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = JusticeGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tono de la Voz (Pitch)",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = if (pitch < 0.95f) "Grave" else if (pitch > 1.15f) "Agudo" else "Natural",
                    fontWeight = FontWeight.Bold,
                    color = JusticeGold,
                    fontSize = 14.sp
                )
            }

            Slider(
                value = pitch,
                onValueChange = onSetPitch,
                valueRange = 0.7f..1.5f,
                colors = SliderDefaults.colors(
                    thumbColor = JusticeGold,
                    activeTrackColor = JusticeGold,
                    inactiveTrackColor = NavyCard
                ),
                modifier = Modifier.testTag("slider_pitch")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Test Voice Button
            OutlinedButton(
                onClick = onTestVoice,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = JusticeGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_test_voice")
            ) {
                Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Probar voz de Dayanara")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
