package com.example.myapplication2.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // Inicializa el TextToSpeech sin referenciar la variable dentro de su propio bloque:
    val tts = remember {
        val newTTS = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Ajusta el idioma por defecto
                //newTTS.language = Locale.getDefault()
            }
        })
        newTTS
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // State para el texto a leer
    var message by remember { mutableStateOf("") }

    // Datos para velocidades TTS
    val speeds = listOf("0.5x", "1.0x", "1.5x", "2.0x")
    var expanded by remember { mutableStateOf(false) }
    var selectedSpeed by remember { mutableStateOf(speeds[1]) } // 1.0x por defecto

    // Checkboxes
    var checkOption1 by remember { mutableStateOf(false) }
    var checkOption2 by remember { mutableStateOf(false) }

    // Radio buttons (para idioma)
    val radioOptions = listOf("Español", "Inglés", "Francés")
    var selectedRadioOption by remember { mutableStateOf(radioOptions[0]) }

    // Datos para la tabla
    val tableHeaders = listOf("Configuración", "Valor Seleccionado")
    val tableData = listOf(
        "Velocidad TTS" to selectedSpeed,
        "Idioma elegido" to selectedRadioOption,
        "Opción 1 activa" to if (checkOption1) "Sí" else "No",
        "Opción 2 activa" to if (checkOption2) "Sí" else "No"
    )

    // Función que usa TTS para “leer” el texto
    val speakText = {
        val speedFactor = when (selectedSpeed) {
            "0.5x" -> 0.5f
            "1.0x" -> 1.0f
            "1.5x" -> 1.5f
            else   -> 2.0f
        }
        tts.setSpeechRate(speedFactor)
        tts.setPitch(1.0f)

        val locale = when (selectedRadioOption) {
            "Español" -> Locale("es", "ES")
            "Inglés"  -> Locale.ENGLISH
            "Francés" -> Locale.FRENCH
            else      -> Locale.getDefault()
        }
        tts.language = locale

        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // Interfaz principal
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comunicación TTS") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Escribe un mensaje para pronunciar:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ComboBox de velocidades TTS
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedSpeed,
                    onValueChange = {},
                    label = { Text("Velocidad") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    speeds.forEach { speed ->
                        DropdownMenuItem(
                            text = { Text(speed) },
                            onClick = {
                                selectedSpeed = speed
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Radio buttons para idioma TTS
            Text("Idioma TTS:")
            radioOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (option == selectedRadioOption),
                        onClick = { selectedRadioOption = option }
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Checkboxes
            Text("Opciones Adicionales:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkOption1,
                    onCheckedChange = { checkOption1 = it }
                )
                Text("Activar Opción 1")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkOption2,
                    onCheckedChange = { checkOption2 = it }
                )
                Text("Activar Opción 2")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para pronunciar el mensaje
            Button(
                onClick = { speakText() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pronunciar Mensaje")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen en forma de “tabla”
            Text("Resumen de configuración:")
            Spacer(modifier = Modifier.height(8.dp))
            TableComponent(tableHeaders, tableData)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onBackClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar")
            }
        }
    }
}

// Composable que muestra una tabla simple con encabezados y filas
@Composable
fun TableComponent(
    headers: List<String>,
    data: List<Pair<String, String>>
) {
    Column {
        // Fila de encabezados
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Divider()

        // Filas con datos
        LazyColumn {
            items(data) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = row.first, modifier = Modifier.weight(1f))
                    Text(text = row.second, modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }
    }
}
