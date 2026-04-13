package com.pasha.sufi.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    val name: String? = null,  // Сделаем name опциональным
    val role: Int
)

data class RegisterRequest(val email: String, val password: String, val name: String)
data class LoginRequest(val email: String, val password: String)
data class ChangePasswordRequest(val currentPassword: String, val newPassword: String)
data class PasswordResetRequest(val email: String)

data class AuthResponse(
    val token: String,
    val user: User
)

data class MeResponse(
    val user: User
)