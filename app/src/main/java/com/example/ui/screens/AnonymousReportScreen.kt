package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ReportEntity
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SecureGreen
import com.example.ui.theme.SecureGreenBg
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnonymousReportScreen(
    onSubmitReport: (ReportEntity) -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current

    val crimeOptions = listOf(
        "Cohecho / Soborno (Exigencia o entrega de dádivas)",
        "Peculado / Malversación (Apropiación o desvío de fondos públicos)",
        "Colusión en Licitaciones (Direccionamiento de contratos y compras)",
        "Nepotismo (Contratación de familiares en el Estado)",
        "Tráfico de Influencias (Intercesión indebida a cambio de beneficio)",
        "Enriquecimiento Ilícito (Desbalance patrimonial no justificado)",
        "Otra irregularidad o falta contra la administración pública"
    )

    val evidenceOptions = listOf(
        "📑 Documentos oficiales / Contratos / TDR",
        "🎙️ Grabaciones de audio / video",
        "💬 Exportación de chats (WhatsApp / Signal)",
        "💳 Comprobantes bancarios / Transferencias",
        "👥 Testigos presenciales de los hechos"
    )

    var selectedCrime by remember { mutableStateOf(crimeOptions[0]) }
    var crimeDropdownExpanded by remember { mutableStateOf(false) }

    var entityName by remember { mutableStateOf("") }
    var suspectOfficials by remember { mutableStateOf("") }
    var incidentDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var narrative by remember { mutableStateOf("") }
    val selectedEvidences = remember { mutableStateListOf<String>() }

    var validationError by remember { mutableStateOf<String?>(null) }
    var generatedReportSuccess by remember { mutableStateOf<ReportEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("anonymous_report_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Strict Anonymity Assurance Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = SecureGreenBg),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = SecureGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Garantía Estricta de Anonimato",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• No solicitamos tu nombre, DNI, teléfono ni correo.\n• No registramos direcciones IP ni cookies.\n• Se generará un código criptográfico único para tu control personal.",
                        fontSize = 12.sp,
                        color = Color(0xFFA7F3D0),
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Section Title
        Text(
            text = "Formulario Estructurado de Denuncia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = JusticeGold
        )
        Text(
            text = "Diligencia la información con la mayor precisión técnica posible para facilitar la investigación de los hechos.",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8)
        )

        // 1. Clasificación del Delito
        Column {
            Text(
                text = "1. Clasificación del Delito o Irregularidad *",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    color = NavySurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334E68)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { crimeDropdownExpanded = true }
                        .testTag("dropdown_crime_type")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCrime,
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = JusticeGold
                        )
                    }
                }

                DropdownMenu(
                    expanded = crimeDropdownExpanded,
                    onDismissRequest = { crimeDropdownExpanded = false },
                    modifier = Modifier
                        .background(NavySurface)
                        .fillMaxWidth(0.9f)
                ) {
                    crimeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White, fontSize = 13.sp) },
                            onClick = {
                                selectedCrime = option
                                crimeDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // 2. Entidad Involucrada
        Column {
            Text(
                text = "2. Entidad Pública o Institución Involucrada *",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = entityName,
                onValueChange = { entityName = it },
                placeholder = { Text("Ej. Ministerio de Transportes, Municipalidad Provincial...", color = Color(0xFF64748B), fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_entity_name"),
                colors = textFieldColors(),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // 3. Funcionarios o Cargos Señalados
        Column {
            Text(
                text = "3. Funcionarios o Cargos Señalados",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 13.sp
            )
            Text(
                text = "Puedes indicar sus cargos, nombres o apodos si los conoces. (No indiques tus datos personales).",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = suspectOfficials,
                onValueChange = { suspectOfficials = it },
                placeholder = { Text("Ej. Director de Abastecimiento, Gerente de Obras...", color = Color(0xFF64748B), fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_officials"),
                colors = textFieldColors(),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // 4. Fecha Aproximada y Lugar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fecha Aprox.",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = incidentDate,
                    onValueChange = { incidentDate = it },
                    placeholder = { Text("Ej. Mayo 2026", color = Color(0xFF64748B), fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_date"),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lugar / Dependencia",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    placeholder = { Text("Ej. Sede Central, Oficina 402", color = Color(0xFF64748B), fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_location"),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // 5. Relato Circunstanciado de los Hechos
        Column {
            Text(
                text = "5. Relato Circunstanciado de los Hechos *",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 13.sp
            )
            Text(
                text = "Detalla los hechos respondiendo: ¿Qué ocurrió? ¿Cómo se solicitó o ejecutó el acto ilícito? ¿Qué montos o beneficios se pactaron?",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = narrative,
                onValueChange = { narrative = it },
                placeholder = {
                    Text(
                        "Describe detalladamente los hechos irregulares sin incluir información que revele tu identidad personal...",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                },
                minLines = 4,
                maxLines = 8,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_narrative"),
                colors = textFieldColors(),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // 6. Tipos de Evidencias Disponibles
        Column {
            Text(
                text = "6. Tipos de Evidencias que Sustentan la Denuncia",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 13.sp
            )
            Text(
                text = "Selecciona las pruebas que posees. Recuerda mantener los archivos originales sin editar para la cadena de custodia.",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                evidenceOptions.forEach { evidence ->
                    val isSelected = selectedEvidences.contains(evidence)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedEvidences.remove(evidence)
                            else selectedEvidences.add(evidence)
                        },
                        label = { Text(evidence, fontSize = 12.sp) },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = JusticeGold,
                            selectedLabelColor = NavyDark,
                            selectedLeadingIconColor = NavyDark,
                            containerColor = NavyCard,
                            labelColor = Color.White
                        )
                    )
                }
            }
        }

        // Validation Error Message
        if (validationError != null) {
            Surface(
                color = Color(0x33EF4444),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = validationError ?: "",
                    color = AlertRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Submit Button
        Button(
            onClick = {
                if (entityName.isBlank()) {
                    validationError = "Por favor indica la entidad pública o institución involucrada."
                    return@Button
                }
                if (narrative.isBlank() || narrative.length < 20) {
                    validationError = "Por favor ingresa un relato circunstanciado de los hechos (mínimo 20 caracteres)."
                    return@Button
                }

                validationError = null

                // Generate anonymous code and SHA-256 hash
                val randomSuffix = UUID.randomUUID().toString().take(6).uppercase()
                val code = "DEN-${SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())}-$randomSuffix"
                val rawToHash = "$code|$selectedCrime|$entityName|$narrative|${System.currentTimeMillis()}"
                val hash = MessageDigest.getInstance("SHA-256")
                    .digest(rawToHash.toByteArray())
                    .joinToString("") { "%02x".format(it) }
                    .take(16)

                val newReport = ReportEntity(
                    reportCode = code,
                    crimeType = selectedCrime,
                    entityName = entityName.trim(),
                    suspectOfficials = suspectOfficials.ifBlank { "No especificado" }.trim(),
                    narrative = narrative.trim(),
                    incidentDate = incidentDate.ifBlank { "Aproximada / Reciente" }.trim(),
                    location = location.ifBlank { "No especificado" }.trim(),
                    evidenceTypes = if (selectedEvidences.isEmpty()) "Sin evidencias adjuntas declaradas" else selectedEvidences.joinToString(", "),
                    hashSignature = hash,
                    timestamp = System.currentTimeMillis()
                )

                onSubmitReport(newReport)
                generatedReportSuccess = newReport
            },
            colors = ButtonDefaults.buttonColors(containerColor = JusticeGold, contentColor = NavyDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_submit_report")
        ) {
            Icon(imageVector = Icons.Default.Security, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generar y Radicar Denuncia Anónima", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Success Dialog with tracking code and receipt
    if (generatedReportSuccess != null) {
        val report = generatedReportSuccess!!
        val formattedSummary = """
        🏛️ RADICACIÓN DE DENUNCIA ANÓNIMA
        Código de Seguimiento: ${report.reportCode}
        Hash Criptográfico de Integridad: ${report.hashSignature}
        
        Delito: ${report.crimeType}
        Entidad: ${report.entityName}
        Funcionarios: ${report.suspectOfficials}
        Fecha/Lugar: ${report.incidentDate} - ${report.location}
        
        Hechos Circunstanciados:
        ${report.narrative}
        
        Evidencias Disponibles:
        ${report.evidenceTypes}
        
        Garantía: Denuncia anónima generada localmente sin rastreo de IP ni datos identificables.
        """.trimIndent()

        AlertDialog(
            onDismissRequest = {
                generatedReportSuccess = null
                onNavigateToHistory()
            },
            containerColor = NavySurface,
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SecureGreen,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    text = "¡Denuncia Anónima Generada!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Tu reporte ha sido radicado bajo estricto anonimato local.",
                        fontSize = 13.sp,
                        color = Color(0xFFCBD5E1)
                    )

                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Código Único de Seguimiento:",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = report.reportCode,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = JusticeGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Hash SHA-256: ${report.hashSignature}",
                                fontSize = 10.sp,
                                color = CyanAccent
                            )
                        }
                    }

                    Text(
                        text = "Guarda este código para tus registros. Puedes copiar el reporte formal para remitirlo a fiscalías o contraloría sin revelar tu identidad.",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(formattedSummary))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JusticeGold, contentColor = NavyDark)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copiar Reporte Completo")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        generatedReportSuccess = null
                        onNavigateToHistory()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Ver en Mis Denuncias")
                }
            }
        )
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = NavySurface,
    unfocusedContainerColor = NavySurface,
    focusedBorderColor = JusticeGold,
    unfocusedBorderColor = Color(0xFF334E68)
)
