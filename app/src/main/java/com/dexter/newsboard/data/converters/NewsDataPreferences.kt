@file:Suppress("PrivatePropertyName")

package com.dexter.newsboard.data.converters

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

class NewsDataPreferences() {
    private val DATA_STORE_NAME = "NEWS_DATA_STORE"
    private val LAST_SYNC_KEY = longPreferencesKey("LAST_SYNC_KEY")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)


    suspend fun getLastSyncTime(context: Context): Long {
        return context.dataStore.data.firstOrNull()?.get(LAST_SYNC_KEY) ?: -1L
    }

    suspend fun setLastSyncTime(context: Context, time: Long) {
        context.dataStore.edit { preferences ->
            val currentValue = preferences[LAST_SYNC_KEY] ?: 0
            preferences[LAST_SYNC_KEY] = time
        }
    }

}