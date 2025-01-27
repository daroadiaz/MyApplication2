package com.example.myapplication2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication2.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val users = mutableListOf(
        User("usuario1", "password1", "user1@email.com"),
        User("usuario2", "password2", "user2@email.com"),
        User("usuario3", "password3", "user3@email.com"),
        User("usuario4", "password4", "user4@email.com"),
        User("usuario5", "password5", "user5@email.com")
    )

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(username: String, password: String) {
        // Validación básica
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor, ingresa usuario y contraseña")
            return
        }

        val user = users.find { it.username == username }
        if (user == null) {
            _authState.value = AuthState.Error("Usuario no encontrado")
            return
        }
        if (user.password != password) {
            _authState.value = AuthState.Error("Contraseña incorrecta")
            return
        }

        _currentUser.value = user
        _authState.value = AuthState.Success("Inicio de sesión exitoso")
    }

    fun register(username: String, password: String, email: String) {
        // Validación básica
        if (username.isBlank() || password.isBlank() || email.isBlank()) {
            _authState.value = AuthState.Error("Todos los campos son obligatorios")
            return
        }

        // Ejemplo de tope de usuarios
        if (users.size >= 5) {
            _authState.value = AuthState.Error("Límite de usuarios alcanzado")
            return
        }

        // Si el correo ya existe
        if (users.any { it.email == email }) {
            _authState.value = AuthState.Error("Ya existe una cuenta con este correo")
            return
        }

        // Si el usuario ya existe
        if (users.any { it.username == username }) {
            _authState.value = AuthState.Error("El usuario '$username' ya existe")
            return
        }

        val newUser = User(username, password, email)
        users.add(newUser)
        _currentUser.value = newUser
        _authState.value = AuthState.Success("Registro exitoso")
    }

    fun resetPassword(email: String, newPassword: String) {
        if (email.isBlank() || newPassword.isBlank()) {
            _authState.value = AuthState.Error("Completa ambos campos para actualizar la contraseña")
            return
        }

        val userIndex = users.indexOfFirst { it.email == email }
        if (userIndex != -1) {
            val user = users[userIndex]
            users[userIndex] = user.copy(password = newPassword)
            _authState.value = AuthState.Success("Contraseña actualizada exitosamente")
        } else {
            _authState.value = AuthState.Error("Email no encontrado")
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Initial
    }

    /**
     * Función para restaurar el estado a 'Initial'
     * después de mostrar el mensaje en la UI.
     */
    fun setAuthStateToInitial() {
        _authState.value = AuthState.Initial
    }

    sealed class AuthState {
        object Initial : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
