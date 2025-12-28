package com.santelocale.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Initialize Context.dataStore extension
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Repository for user preferences using Jetpack DataStore.
 * Stores user name and glucose unit preference.
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        // Preference keys
        private val USER_NAME = stringPreferencesKey("user_name")
        private val GLUCOSE_UNIT = stringPreferencesKey("glucose_unit")
    }

    /**
     * Flow of the user's name.
     * Default: "" (empty string)
     */
    val userNameFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    /**
     * Flow of the glucose unit preference.
     * Default: "mg/dL" (standard in Haiti)
     */
    val glucoseUnitFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[GLUCOSE_UNIT] ?: "mg/dL"
    }

    /**
     * Save the user's name.
     */
    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    /**
     * Save the glucose unit preference.
     * @param unit Either "mg/dL" or "mmol/L"
     */
    suspend fun saveUnit(unit: String) {
        dataStore.edit { preferences ->
            preferences[GLUCOSE_UNIT] = unit
        }
    }

    /**
     * Clear all user preferences (reset to defaults).
     */
    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
