package com.moralabs.pet.core.data.repository.impl

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.moralabs.pet.MainActivity
import com.moralabs.pet.core.data.remote.dto.AuthenticationDto
import com.moralabs.pet.core.data.repository.AuthenticationRepository

class AuthenticationRepositoryImpl : AuthenticationRepository {

    companion object {
        private const val USER_KEY = "user"
    }

    private var _authentication: AuthenticationDto? = null

    private var preferences: SharedPreferences? = null

    private fun createPreferences(): SharedPreferences? {
        if (preferences != null) return preferences

        preferences = MainActivity.instance?.let {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            EncryptedSharedPreferences.create(
                "encrypted_preferences",
                masterKeyAlias,
                it,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        return preferences
    }

    init {
        createAuthDto()
    }

    private fun createAuthDto() {
        createPreferences()

        preferences?.let { preferences ->
            preferences.getString(USER_KEY, null)?.let {
                _authentication = Gson().fromJson(it, AuthenticationDto::class.java)
            }

            if (_authentication == null) {
                _authentication = AuthenticationDto()

                preferences.edit()?.putString(USER_KEY, Gson().toJson(_authentication))?.commit()
            }
        }
    }

    override fun isLoggedIn(): Boolean {

        if (_authentication == null) createAuthDto()

        return _authentication?.bearerKey != null

    }

    override fun isGuest() = _authentication?.isGuest == true

    override fun logout(): Boolean {
        _authentication?.apply {
            this.bearerKey = null
            this.userId = null
            this.isGuest = true

            preferences?.edit()?.putString(USER_KEY, Gson().toJson(_authentication))?.commit()
        }

        return true
    }

    override fun login(userId: String?, bearerToken: String, refreshToken: String): Boolean {
        _authentication?.apply {
            this.bearerKey = bearerToken
            this.refreshKey = refreshToken
            this.isGuest = false

            preferences?.edit()?.putString(USER_KEY, Gson().toJson(_authentication))?.commit()
        }

        return true
    }

    override fun guestLogin(bearerToken: String): Boolean {
        _authentication?.apply {
            this.bearerKey = bearerToken
            this.isGuest = true

            preferences?.edit()?.putString(USER_KEY, Gson().toJson(_authentication))?.commit()
        }
        return true
    }

    override fun refreshLogin(bearerToken: String?, refreshToken: String?): Boolean {
        _authentication?.apply {
            this.bearerKey = bearerToken
            this.refreshKey = refreshToken

            preferences?.edit()?.putString(USER_KEY, Gson().toJson(_authentication))?.commit()
        }
        return true
    }

    override fun getAuthentication() = _authentication

    override fun requestHeaders() = _authentication?.let {
        hashMapOf(
            "Authorization" to if (it.bearerKey == null) null else "Bearer ${it.bearerKey}",
            "Accept-Language" to it.language,
            "Pet-Channel" to it.channel,
            "Pet-DeviceModel" to it.deviceModel,
            "Pet-DeviceBrand" to it.deviceBrand,
            "Pet-DeviceVersion" to it.deviceVersion,
            "Pet-ApplicationId" to it.applicationId,
            "Pet-ApplicationVersion" to it.applicationVersion,
            "Pet-ApplicationCode" to it.applicationCode.toString(),
        )
    } ?: run {
        hashMapOf()
    }
}