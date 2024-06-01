package dev.veryniche.quickqr.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val showWelcomeOnStart: Boolean,
    val lastReviewDate: Long,
)

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    private object PreferencesKeys {
        val SHOW_WELCOME_ON_START = booleanPreferencesKey("show_welcome_on_start")
        val LAST_REVIEW_DATE = longPreferencesKey("last_review_date")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val showWelcome = preferences[PreferencesKeys.SHOW_WELCOME_ON_START] ?: true
            val lastReviewDate = preferences[PreferencesKeys.LAST_REVIEW_DATE] ?: -1L
            UserPreferences(
                showWelcomeOnStart = showWelcome,
                lastReviewDate = lastReviewDate
            )
        }

    suspend fun updateShowWelcomeOnStart(showWelcome: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_WELCOME_ON_START] = showWelcome
        }
    }

    suspend fun updateLastReviewDate() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_REVIEW_DATE] = System.currentTimeMillis()
        }
    }
}
