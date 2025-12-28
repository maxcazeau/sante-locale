package com.santelocale.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for user preferences using Jetpack DataStore.
 * Stores user name and glucose unit preference.
 */
class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val GLUCOSE_UNIT = stringPreferencesKey("glucose_unit")

        const val DEFAULT_UNIT = "mg/dL"
    }

    /**
     * Flow of the user's name.
     */
    val userName: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    /**
     * Flow of the glucose unit preference.
     * Defaults to "mg/dL" (standard in Haiti).
     */
    val glucoseUnit: Flow<String> = dataStore.data.map { preferences ->
        preferences[GLUCOSE_UNIT] ?: DEFAULT_UNIT
    }

    /**
     * Update the user's name.
     */
    suspend fun updateUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    /**
     * Update the glucose unit preference.
     * @param unit Either "mg/dL" or "mmol/L"
     */
    suspend fun updateGlucoseUnit(unit: String) {
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
