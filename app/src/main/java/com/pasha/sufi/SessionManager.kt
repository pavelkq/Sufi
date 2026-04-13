package com.pasha.sufi

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.pasha.sufi.models.User

class SessionManager(context: Context) {
    companion object {
        @Volatile
        private var instance: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context.applicationContext).also { instance = it }
            }
        }
    }

    private val prefs: SharedPreferences

    init {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        prefs = EncryptedSharedPreferences.create(
            "sufi_secure_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAuthData(token: String, user: User, emailConfirmed: Boolean) {
        prefs.edit().apply {
            putString("token", token)
            putString("user_email", user.email)
            putString("user_name", user.name ?: "")
            putInt("user_id", user.id)
            putInt("user_role", user.role)
            putBoolean("email_confirmed", emailConfirmed)
            apply()
        }
    }

    fun saveCredentials(email: String, password: String) {
        prefs.edit().apply {
            putString("saved_email", email)
            putString("saved_password", password)
            apply()
        }
    }

    fun getSavedEmail(): String? = prefs.getString("saved_email", null)
    fun getSavedPassword(): String? = prefs.getString("saved_password", null)

    fun updateUserData(user: User) {
        prefs.edit().apply {
            putString("user_email", user.email)
            putString("user_name", user.name ?: "")
            putInt("user_role", user.role)
            apply()
        }
    }

    fun updateToken(newToken: String) {
        prefs.edit().apply {
            putString("token", newToken)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString("token", null)

    fun getUserName(): String? = prefs.getString("user_name", null)

    fun getUserId(): Int = prefs.getInt("user_id", -1)

    fun getUserRole(): Int = prefs.getInt("user_role", -1)

    fun isEmailConfirmed(): Boolean = prefs.getBoolean("email_confirmed", false)

    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun clearCredentials() {
        prefs.edit().remove("saved_email").remove("saved_password").apply()
    }
}