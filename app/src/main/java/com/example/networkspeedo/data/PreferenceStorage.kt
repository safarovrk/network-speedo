package com.example.networkspeedo.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferenceStorage @Inject constructor(
    context: Context
) {
    companion object {
        const val PREF_FILE_NAME = "encrypted_shared_pref"

        const val PREF_THEME_KEY = "pref_theme_key"
        const val PREF_LOAD_ENDPOINT_KEY = "pref_load_endpoint_key"
        const val PREF_UPLOAD_ENDPOINT_KEY = "pref_upload_endpoint_key"
        const val PREF_LOAD_SPEED_KEY = "pref_load_key"
        const val PREF_UPLOAD_SPEED_KEY = "pref_upload_key"

        const val PREF_VALUE_SYSTEM_THEME = "pref_value_system_theme"
        const val PREF_VALUE_DARK_THEME = "pref_value_dark_theme"
        const val PREF_VALUE_LIGHT_THEME = "pref_value_light_theme"

        const val PREF_DEFAULT_VALUE_LOAD_ENDPOINT = "https://proof.ovh.net/files/100Mb.dat"
        const val PREF_DEFAULT_VALUE_UPLOAD_ENDPOINT = "https://de2.testmy.net/b/SmarTest/up"
        const val PREF_DEFAULT_VALUE_THEME = PREF_VALUE_SYSTEM_THEME
        const val PREF_DEFAULT_VALUE_LOAD = true
        const val PREF_DEFAULT_VALUE_UPLOAD = true
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPref: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREF_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveLoadEndpoint(endpoint: String) = withContext(Dispatchers.IO) {
        sharedPref.edit().putString(PREF_LOAD_ENDPOINT_KEY, endpoint).apply()
    }

    suspend fun getLoadEndpoint(): String = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getString(PREF_LOAD_ENDPOINT_KEY, PREF_DEFAULT_VALUE_LOAD_ENDPOINT) ?: ""
    }

    suspend fun saveUploadEndpoint(endpoint: String) = withContext(Dispatchers.IO) {
        sharedPref.edit().putString(PREF_UPLOAD_ENDPOINT_KEY, endpoint).apply()
    }

    suspend fun getUploadEndpoint(): String = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getString(PREF_UPLOAD_ENDPOINT_KEY, PREF_DEFAULT_VALUE_UPLOAD_ENDPOINT) ?: ""
    }

    suspend fun saveTheme(theme: String) = withContext(Dispatchers.IO) {
        sharedPref.edit().putString(PREF_THEME_KEY, theme).apply()
    }

    suspend fun getTheme(): String = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getString(PREF_THEME_KEY, PREF_VALUE_SYSTEM_THEME) ?: ""
    }

    suspend fun saveLoadCheckboxState(value: Boolean) = withContext(Dispatchers.IO) {
        sharedPref.edit().putBoolean(PREF_LOAD_SPEED_KEY, value).apply()
    }

    suspend fun getLoadCheckboxState(): Boolean = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getBoolean(PREF_LOAD_SPEED_KEY, PREF_DEFAULT_VALUE_LOAD)
    }

    suspend fun saveUploadCheckboxState(value: Boolean) = withContext(Dispatchers.IO) {
        sharedPref.edit().putBoolean(PREF_UPLOAD_SPEED_KEY, value).apply()
    }

    suspend fun getUploadCheckboxState(): Boolean = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getBoolean(PREF_UPLOAD_SPEED_KEY, PREF_DEFAULT_VALUE_UPLOAD)
    }
}