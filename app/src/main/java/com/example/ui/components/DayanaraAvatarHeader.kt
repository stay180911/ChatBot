package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.JusticeGoldDark
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SecureGreen
import kotlin.math.sin

@Composable
fun DayanaraAvatarHeader(
    isSpeaking: Boolean,
    isPaused: Boolean,
    isListening: Boolean,
    isMuted: Boolean,
    micRms: Float,
    speechRate: Float,
    onToggleMute: () -> Unit,
    onPauseResume: () -> Unit,
    onStopSpeech: () -> Unit,
    onOpenAudioSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking || isListening) 1.08f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 600 else 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283f, // 2 * PI
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 1000 else 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    Surface(
        color = NavySurface,
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp)
            .testTag("dayanara_header_surface")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top action bar inside header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Anonymous safe badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SecureGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Anónimo & Cifrado",
                        color = SecureGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Audio Quick Controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Mute / Unmute Button
                    IconButton(
                        onClick = onToggleMute,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_toggle_mute")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Activar voz" else "Silenciar voz",
                            tint = if (isMuted) Color.Gray else JusticeGold
                        )
                    }

                    // Play / Pause if speech active
                    if (isSpeaking || isPaused) {
                        IconButton(
                            onClick = onPauseResume,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_pause_resume")
                        ) {
                            Icon(
                                imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isPaused) "Reanudar" else "Pausar",
                                tint = JusticeGold
                            )
                        }

                        IconButton(
                            onClick = onStopSpeech,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_stop_speech")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Detener",
                                tint = Color(0xFFEF4444)
                            )
                        }
                    }

                    // Audio Config Button
                    IconButton(
                        onClick = onOpenAudioSettings,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_audio_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Ajustes de Audio",
                            tint = CyanAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Dayanara Interactive Avatar with Glow
            val ringBrush = when {
                isListening -> Brush.sweepGradient(listOf(CyanAccent, CyanGlow, CyanAccent))
                isSpeaking -> Brush.sweepGradient(listOf(JusticeGold, JusticeGoldDark, JusticeGold))
                else -> Brush.sweepGradient(listOf(NavyLight, JusticeGoldDark, NavyLight))
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size((78 * pulseScale).dp)
                    .clip(CircleShape)
                    .border(3.dp, ringBrush, CircleShape)
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_dayanara_avatar),
                    contentDescription = "Avatar de Dayanara",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Title and Status
            Text(
                text = "Dayanara",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = when {
                    isListening -> "🎙️ Escuchando tu consulta..."
                    isSpeaking -> "🔊 Explicando en voz alta (${speechRate}x)..."
                    isPaused -> "⏸️ Locución pausada"
                    isMuted -> "🔇 Voz silenciada (Modo lectura)"
                    else -> "⚖️ Asesora Legal Penal Anticorrupción"
                },
                fontSize = 12.sp,
                color = when {
                    isListening -> CyanAccent
                    isSpeaking -> JusticeGold
                    else -> Color(0xFF94A3B8)
                },
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Real-Time Audio Waves (Animated Canvas)
            AudioWaveCanvas(
                isSpeaking = isSpeaking,
                isListening = isListening,
                micRms = micRms,
                wavePhase = wavePhase,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .testTag("audio_wave_canvas")
            )
        }
    }
}

@Composable
fun AudioWaveCanvas(
    isSpeaking: Boolean,
    isListening: Boolean,
    micRms: Float,
    wavePhase: Float,
    modifier: Modifier = Modifier
) {
    val barsCount = 28
    val primaryWaveColor = when {
        isListening -> CyanAccent
        isSpeaking -> JusticeGold
        else -> Color(0xFF334E68)
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val barWidth = (width / (barsCount * 1.6f)).coerceAtLeast(3f)
        val spacing = (width - (barsCount * barWidth)) / (barsCount + 1)

        for (i in 0 until barsCount) {
            val progress = i.toFloat() / barsCount.toFloat()
            val x = spacing + i * (barWidth + spacing)

            val barHeight = when {
                isListening -> {
                    // Reactive to mic amplitude
                    val factor = ((micRms / 10f).coerceIn(0.1f, 1f))
                    val wave = (sin((progress * 4 * Math.PI) + wavePhase).toFloat() + 1f) / 2f
                    (height * 0.2f) + (wave * height * 0.75f * factor)
                }
                isSpeaking -> {
                    // Active speaking dynamic wave
                    val wave1 = sin((progress * 3 * Math.PI) + wavePhase).toFloat()
                    val wave2 = sin((progress * 6 * Math.PI) - wavePhase * 1.3f).toFloat()
                    val combined = ((wave1 + wave2) / 2f).coerceIn(-1f, 1f)
                    val normalized = (combined + 1f) / 2f
                    (height * 0.15f) + (normalized * height * 0.8f)
                }
                else -> {
                    // Subtle resting breath
                    val subtleWave = (sin((progress * 2 * Math.PI) + wavePhase * 0.4f).toFloat() + 1f) / 2f
                    (height * 0.10f) + (subtleWave * height * 0.22f)
                }
            }

            val y = (height - barHeight) / 2f

            drawRoundRect(
                color = primaryWaveColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}
