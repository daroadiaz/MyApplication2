package com.example.myapplication2

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication2.screens.LoginScreen
import com.example.myapplication2.screens.RegisterScreen
import com.example.myapplication2.viewmodel.AuthViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysAllUIElements() {
        // Configuración del composable
        composeTestRule.setContent {
            LoginScreen(
                authState = AuthViewModel.AuthState.Initial,
                onLoginClick = { _, _ -> },
                onRegisterClick = {},
                onForgotPasswordClick = {},
                onDismissError = {}
            )
        }

        // Verificar que todos los elementos de la UI están presentes
        composeTestRule.onNodeWithText("Iniciar Sesión", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Usuario").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrarse").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsErrorMessage() {
        // Configuración del composable con un estado de error
        composeTestRule.setContent {
            LoginScreen(
                authState = AuthViewModel.AuthState.Error("Mensaje de error de prueba"),
                onLoginClick = { _, _ -> },
                onRegisterClick = {},
                onForgotPasswordClick = {},
                onDismissError = {}
            )
        }

        // Verificar que se muestra el mensaje de error
        composeTestRule.onNodeWithText("Mensaje de error de prueba").assertIsDisplayed()
    }

    @Test
    fun registerScreen_displaysAllUIElements() {
        // Configuración del composable
        composeTestRule.setContent {
            RegisterScreen(
                authState = AuthViewModel.AuthState.Initial,
                onRegisterClick = { _, _, _ -> },
                onBackClick = {},
                onDismissError = {}
            )
        }

        // Verificar que todos los elementos de la UI están presentes
        composeTestRule.onNodeWithText("Registro").assertIsDisplayed()
        composeTestRule.onNodeWithText("Usuario").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrarse").assertIsDisplayed()
        composeTestRule.onNodeWithText("Volver al inicio de sesión").assertIsDisplayed()
    }

    @Test
    fun registerScreen_showsErrorMessage() {
        // Configuración del composable con un estado de error
        composeTestRule.setContent {
            RegisterScreen(
                authState = AuthViewModel.AuthState.Error("Error de registro de prueba"),
                onRegisterClick = { _, _, _ -> },
                onBackClick = {},
                onDismissError = {}
            )
        }

        // Verificar que se muestra el mensaje de error
        composeTestRule.onNodeWithText("Error de registro de prueba").assertIsDisplayed()
    }
}