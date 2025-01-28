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
        when {
            username.isBlank() -> {
                _authState.value = AuthState.Error("El usuario no puede estar vacío")
                return
            }
            password.isBlank() -> {
                _authState.value = AuthState.Error("La contraseña no puede estar vacía")
                return
            }
            else -> {
                val user = users.find { it.username == username && it.password == password }
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success("¡Bienvenido ${user.username}!")
                } else {
                    _authState.value = AuthState.Error("Usuario o contraseña incorrectos")
                }
            }
        }
    }

    fun register(username: String, password: String, email: String) {
        when {
            username.isBlank() -> {
                _authState.value = AuthState.Error("El usuario no puede estar vacío")
                return
            }
            password.isBlank() -> {
                _authState.value = AuthState.Error("La contraseña no puede estar vacía")
                return
            }
            email.isBlank() -> {
                _authState.value = AuthState.Error("El email no puede estar vacío")
                return
            }
            !email.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) -> {
                _authState.value = AuthState.Error("El formato del email no es válido")
                return
            }
            password.length < 6 -> {
                _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
                return
            }
            users.size >= 10 -> {
                _authState.value = AuthState.Error("Se ha alcanzado el límite máximo de usuarios (5)")
                return
            }
            users.any { it.username == username } -> {
                _authState.value = AuthState.Error("El usuario ya existe")
                return
            }
            users.any { it.email == email } -> {
                _authState.value = AuthState.Error("El email ya está registrado")
                return
            }
            else -> {
                val newUser = User(username, password, email)
                users.add(newUser)
                _currentUser.value = newUser
                _authState.value = AuthState.Success("¡Registro exitoso! Bienvenido $username")
            }
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        when {
            email.isBlank() -> {
                _authState.value = AuthState.Error("El email no puede estar vacío")
                return
            }
            !email.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) -> {
                _authState.value = AuthState.Error("El formato del email no es válido")
                return
            }
            newPassword.isBlank() -> {
                _authState.value = AuthState.Error("La nueva contraseña no puede estar vacía")
                return
            }
            newPassword.length < 6 -> {
                _authState.value = AuthState.Error("La nueva contraseña debe tener al menos 6 caracteres")
                return
            }
            else -> {
                val userIndex = users.indexOfFirst { it.email == email }
                if (userIndex != -1) {
                    val user = users[userIndex]
                    users[userIndex] = user.copy(password = newPassword)
                    _authState.value = AuthState.Success("Contraseña actualizada exitosamente")
                } else {
                    _authState.value = AuthState.Error("No existe ninguna cuenta con este email")
                }
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Initial
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Initial
        }
    }

    sealed class AuthState {
        object Initial : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
