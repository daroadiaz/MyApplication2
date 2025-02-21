package com.example.myapplication2.data

import org.junit.Assert.*
import org.junit.Test

class UserTest {
    
    @Test
    fun userDataClass_createsUserWithCorrectProperties() {
        // Arrange
        val username = "testUser"
        val password = "testPass"
        val email = "test@example.com"
        
        // Act
        val user = User(username, password, email)
        
        // Assert
        assertEquals(username, user.username)
        assertEquals(password, user.password)
        assertEquals(email, user.email)
    }
    
    @Test
    fun userDataClass_equalityWorksCorrectly() {
        // Arrange
        val user1 = User("user", "pass", "email@example.com")
        val user2 = User("user", "pass", "email@example.com")
        val user3 = User("different", "pass", "email@example.com")
        
        // Assert
        assertEquals(user1, user2)
        assertNotEquals(user1, user3)
    }
    
    @Test
    fun userDataClass_copyWorksCorrectly() {
        // Arrange
        val originalUser = User("user", "pass", "email@example.com")
        
        // Act
        val copiedUser = originalUser.copy(password = "newpass")
        
        // Assert
        assertEquals("user", copiedUser.username)
        assertEquals("newpass", copiedUser.password)
        assertEquals("email@example.com", copiedUser.email)
    }
}