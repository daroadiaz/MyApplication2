package com.example.myapplication2.viewmodel

import com.example.myapplication2.data.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        viewModel = AuthViewModel()
    }

    @Test
    fun login_withCorrectCredentials_returnsSuccessState() {
        // Arrange - Se utilizan credenciales de un usuario predefinido en AuthViewModel
        val username = "usuario1"
        val password = "password1"

        // Act
        viewModel.login(username, password)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Success)
        assertEquals("¡Bienvenido $username!", (state as AuthViewModel.AuthState.Success).message)
        
        // Verificar que el usuario actual se haya establecido correctamente
        val currentUser = viewModel.currentUser.value
        assertNotNull(currentUser)
        assertEquals(username, currentUser?.username)
        assertEquals(password, currentUser?.password)
    }

    @Test
    fun login_withIncorrectCredentials_returnsErrorState() {
        // Arrange
        val username = "usuario_inexistente"
        val password = "password_incorrecto"

        // Act
        viewModel.login(username, password)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("Usuario o contraseña incorrectos", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun login_withEmptyUsername_returnsErrorState() {
        // Arrange
        val username = ""
        val password = "password"

        // Act
        viewModel.login(username, password)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("El usuario no puede estar vacío", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun login_withEmptyPassword_returnsErrorState() {
        // Arrange
        val username = "usuario1"
        val password = ""

        // Act
        viewModel.login(username, password)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("La contraseña no puede estar vacía", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun register_withValidData_returnsSuccessState() {
        // Arrange
        val username = "nuevoUsuario"
        val password = "password123"
        val email = "nuevo@email.com"

        // Act
        viewModel.register(username, password, email)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Success)
        assertEquals("¡Registro exitoso! Bienvenido $username", (state as AuthViewModel.AuthState.Success).message)
        
        // Verificar que el usuario se haya creado correctamente
        val currentUser = viewModel.currentUser.value
        assertNotNull(currentUser)
        assertEquals(username, currentUser?.username)
        assertEquals(password, currentUser?.password)
        assertEquals(email, currentUser?.email)
    }

    @Test
    fun register_withExistingUsername_returnsErrorState() {
        // Arrange - Usando un usuario que ya existe en la lista predefinida
        val username = "usuario1"
        val password = "password123"
        val email = "nuevo@email.com"

        // Act
        viewModel.register(username, password, email)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("El usuario ya existe", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun register_withExistingEmail_returnsErrorState() {
        // Arrange - Usando un email que ya existe en la lista predefinida
        val username = "nuevoUsuario"
        val password = "password123"
        val email = "user1@email.com"

        // Act
        viewModel.register(username, password, email)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("El email ya está registrado", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun register_withInvalidEmail_returnsErrorState() {
        // Arrange
        val username = "nuevoUsuario"
        val password = "password123"
        val email = "emailInvalido"

        // Act
        viewModel.register(username, password, email)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("El formato del email no es válido", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun register_withShortPassword_returnsErrorState() {
        // Arrange
        val username = "nuevoUsuario"
        val password = "12345" // Menos de 6 caracteres
        val email = "nuevo@email.com"

        // Act
        viewModel.register(username, password, email)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("La contraseña debe tener al menos 6 caracteres", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun resetPassword_withValidEmail_returnsSuccessState() {
        // Arrange - Usando un email que existe en la lista predefinida
        val email = "user1@email.com"
        val newPassword = "nuevaPassword123"

        // Act
        viewModel.resetPassword(email, newPassword)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Success)
        assertEquals("Contraseña actualizada exitosamente", (state as AuthViewModel.AuthState.Success).message)
    }

    @Test
    fun resetPassword_withNonExistingEmail_returnsErrorState() {
        // Arrange
        val email = "noexiste@email.com"
        val newPassword = "nuevaPassword123"

        // Act
        viewModel.resetPassword(email, newPassword)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("No existe ninguna cuenta con este email", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun resetPassword_withInvalidEmail_returnsErrorState() {
        // Arrange
        val email = "emailInvalido"
        val newPassword = "nuevaPassword123"

        // Act
        viewModel.resetPassword(email, newPassword)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("El formato del email no es válido", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun resetPassword_withShortPassword_returnsErrorState() {
        // Arrange
        val email = "user1@email.com"
        val newPassword = "12345" // Menos de 6 caracteres

        // Act
        viewModel.resetPassword(email, newPassword)

        // Assert
        val state = viewModel.authState.value
        assertTrue(state is AuthViewModel.AuthState.Error)
        assertEquals("La nueva contraseña debe tener al menos 6 caracteres", (state as AuthViewModel.AuthState.Error).message)
    }

    @Test
    fun logout_clearsCurrentUserAndReturnsInitialState() {
        // Arrange - Primero hacemos login
        viewModel.login("usuario1", "password1")
        
        // Act
        viewModel.logout()
        
        // Assert
        assertNull(viewModel.currentUser.value)
        assertTrue(viewModel.authState.value is AuthViewModel.AuthState.Initial)
    }

    @Test
    fun clearError_changesErrorStateToInitial() {
        // Arrange - Primero generamos un error
        viewModel.login("", "password")
        assertTrue(viewModel.authState.value is AuthViewModel.AuthState.Error)
        
        // Act
        viewModel.clearError()
        
        // Assert
        assertTrue(viewModel.authState.value is AuthViewModel.AuthState.Initial)
    }
}