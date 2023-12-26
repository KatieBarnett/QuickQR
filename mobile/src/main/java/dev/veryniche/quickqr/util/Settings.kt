package dev.veryniche.quickqr.util

import androidx.datastore.preferences.core.booleanPreferencesKey

object Settings {
    const val DATA_STORE_KEY = "settings"

    val KEY_SHOW_WELCOME = booleanPreferencesKey("key_show_welcome")
}
