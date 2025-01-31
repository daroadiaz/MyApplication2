package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication2.screens.*
import com.example.myapplication2.ui.theme.MyApplication2Theme
import com.example.myapplication2.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplication2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val authState = authViewModel.authState.collectAsState()
                    val currentUser = authViewModel.currentUser.collectAsState()

                    NavHost(navController = navController, startDestination = "login") {

                        // Pantalla de Login
                        composable("login") {
                            LoginScreen(
                                authState = authState.value,
                                onLoginClick = { username, password ->
                                    authViewModel.login(username, password)
                                    if (authState.value is AuthViewModel.AuthState.Success) {
                                        navController.navigate("home") {
                                            // Eliminamos de la pila la pantalla de login
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                },
                                onForgotPasswordClick = {
                                    navController.navigate("forgot_password")
                                },
                                onDismissError = {
                                    authViewModel.clearError()
                                }
                            )
                        }

                        // Pantalla de Registro
                        composable("register") {
                            RegisterScreen(
                                authState = authState.value,
                                onRegisterClick = { username, password, email ->
                                    authViewModel.register(username, password, email)
                                    if (authState.value is AuthViewModel.AuthState.Success) {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                },
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onDismissError = {
                                    authViewModel.clearError()
                                }
                            )
                        }

                        // Pantalla de Recuperar Contraseña
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                authState = authState.value,
                                onResetClick = { email, newPassword ->
                                    authViewModel.resetPassword(email, newPassword)
                                    if (authState.value is AuthViewModel.AuthState.Success) {
                                        navController.navigate("login") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                },
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onDismissError = {
                                    authViewModel.clearError()
                                }
                            )
                        }

                        // Pantalla principal (Home)
                        composable("home") {
                            currentUser.value?.let { user ->
                                HomeScreen(
                                    username = user.username,
                                    email = user.email,
                                    onLogoutClick = {
                                        authViewModel.logout()
                                        navController.navigate("login") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    },
                                    onNavigateToCommunication = {
                                        navController.navigate("communication")
                                    }
                                )
                            }
                        }

                        composable("communication") {
                            CommunicationScreen(
                                onBackClick = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
