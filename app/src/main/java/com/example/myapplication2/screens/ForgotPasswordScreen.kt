package com.example.myapplication2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication2.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    authState: AuthViewModel.AuthState,
    onResetClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
    onDismissError: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Recuperar Contraseña",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = authState is AuthViewModel.AuthState.Error && authState.message.contains("email", ignoreCase = true)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nueva Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = authState is AuthViewModel.AuthState.Error && authState.message.contains("contraseña", ignoreCase = true)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onResetClick(email, newPassword) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Contraseña")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackClick) {
            Text("Volver al inicio de sesión")
        }

        // Mostrar mensajes de error o éxito
        if (authState is AuthViewModel.AuthState.Error) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = onDismissError) {
                        Text("Cerrar")
                    }
                }
            ) {
                Text(authState.message)
            }
        }
    }
}