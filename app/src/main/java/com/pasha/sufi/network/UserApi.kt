package com.pasha.sufi.network

import com.pasha.sufi.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("api/auth-service/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("api/auth-service/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @GET("api/auth-service/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<MeResponse>

    @POST("api/admin-backend/users/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body body: ChangePasswordRequest
    ): Response<Unit>

    @POST("api/auth-service/password-reset/request")
    suspend fun passwordResetRequest(@Body body: PasswordResetRequest): Response<Unit>

    @DELETE("api/admin-backend/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<Unit>
}