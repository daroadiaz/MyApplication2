package com.example.myapplication2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication2.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val users = mutableListOf(
        // Ejemplo con 5 usuarios
        User("usuario1", "password1", "user1@email.com"),
        User("usuario2", "password2", "user2@email.com"),
        // ...
    )

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(username: String, password: String) {
        val user = users.find { it.username == username && it.password == password }
        if (user != null) {
            _currentUser.value = user
            _authState.value = AuthState.Success("Inicio de sesión exitoso")
        } else {
            _authState.value = AuthState.Error("Credenciales inválidas")
        }
    }

    fun register(username: String, password: String, email: String) {
        if (users.size >= 5) {
            _authState.value = AuthState.Error("Límite de usuarios alcanzado")
            return
        }
        if (users.any { it.username == username }) {
            _authState.value = AuthState.Error("Usuario ya existe")
            return
        }
        val newUser = User(username, password, email)
        users.add(newUser)
        _currentUser.value = newUser
        _authState.value = AuthState.Success("Registro exitoso")
    }

    fun resetPassword(email: String, newPassword: String) {
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

    sealed class AuthState {
        object Initial : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
