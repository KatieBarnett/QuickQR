package dev.veryniche.quickqr.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val showWelcomeOnStart: Boolean,
    val lastReviewDate: Long,
    val numberOfOpens: Int,
)

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    private object PreferencesKeys {
        val SHOW_WELCOME_ON_START = booleanPreferencesKey("show_welcome_on_start")
        val LAST_REVIEW_DATE = longPreferencesKey("last_review_date")
        val NUMBER_OF_OPENS = intPreferencesKey("number_of_opens")
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
            val numberOfOpens = preferences[PreferencesKeys.NUMBER_OF_OPENS] ?: 0
            UserPreferences(
                showWelcomeOnStart = showWelcome,
                lastReviewDate = lastReviewDate,
                numberOfOpens = numberOfOpens,
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

    suspend fun incrementNumberOfOpens() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_OF_OPENS] = (preferences[PreferencesKeys.NUMBER_OF_OPENS] ?: 0) + 1
        }
    }
}
