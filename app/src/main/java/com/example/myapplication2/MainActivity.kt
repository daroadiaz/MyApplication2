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
import com.example.myapplication2.screens.ForgotPasswordScreen
import com.example.myapplication2.screens.HomeScreen
import com.example.myapplication2.screens.LoginScreen
import com.example.myapplication2.screens.RegisterScreen
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

                    // Observamos el estado de autenticación
                    val authState = authViewModel.authState.collectAsState()
                    val currentUser = authViewModel.currentUser.collectAsState()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { username, password ->
                                    authViewModel.login(username, password)
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                },
                                onForgotPasswordClick = {
                                    navController.navigate("forgot_password")
                                }
                            )

                            // Si login es exitoso, pasa a home
                            if (authState.value is AuthViewModel.AuthState.Success) {
                                navController.navigate("home")
                            }
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterClick = { username, password, email ->
                                    authViewModel.register(username, password, email)
                                },
                                onBackClick = { navController.navigateUp() }
                            )

                            // Si registro es exitoso, pasa a home
                            if (authState.value is AuthViewModel.AuthState.Success) {
                                navController.navigate("home")
                            }
                        }

                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onResetClick = { email, newPassword ->
                                    authViewModel.resetPassword(email, newPassword)
                                },
                                onBackClick = { navController.navigateUp() }
                            )

                            // Si se resetearon las credenciales con éxito, vuelve al login
                            if (authState.value is AuthViewModel.AuthState.Success) {
                                navController.navigate("login")
                            }
                        }

                        composable("home") {
                            // Mostrar solo si hay usuario
                            currentUser.value?.let { user ->
                                HomeScreen(
                                    username = user.username,
                                    email = user.email,
                                    onLogoutClick = {
                                        authViewModel.logout()
                                        navController.navigate("login") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
