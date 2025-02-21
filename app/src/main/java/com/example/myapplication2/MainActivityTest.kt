package com.example.myapplication2

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreen_isDisplayedAtStart() {
        // Verificar que se muestra la pantalla de login al iniciar la app
        composeTestRule.onNodeWithText("Iniciar Sesión", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsErrorOnEmptyCredentials() {
        // Clic en el botón de login sin ingresar credenciales
        composeTestRule.onNodeWithText("Iniciar Sesión", useUnmergedTree = true)
            .performClick()

        // Verificar que se muestra el mensaje de error
        composeTestRule.onNodeWithText("El usuario no puede estar vacío").assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigatesToRegisterScreen() {
        // Clic en el botón de registro
        composeTestRule.onNodeWithText("Registrarse").performClick()

        // Verificar que se navega a la pantalla de registro
        composeTestRule.onNodeWithText("Registro").assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigatesToForgotPasswordScreen() {
        // Clic en el botón de olvidé mi contraseña
        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?").performClick()

        // Verificar que se navega a la pantalla de recuperar contraseña
        composeTestRule.onNodeWithText("Recuperar Contraseña").assertIsDisplayed()
    }

    @Test
    fun loginScreen_performsSuccessfulLogin() {
        // Ingresar credenciales válidas
        composeTestRule.onNodeWithText("Usuario").performTextInput("usuario1")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password1")

        // Clic en el botón de login
        composeTestRule.onNodeWithText("Iniciar Sesión", useUnmergedTree = true)
            .performClick()

        // Verificar que se navega a la pantalla principal
        composeTestRule.onNodeWithText("Bienvenido, usuario1").assertIsDisplayed()
    }
}