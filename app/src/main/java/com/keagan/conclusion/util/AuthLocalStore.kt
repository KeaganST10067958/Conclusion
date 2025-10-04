package com.keagan.conclusion.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth_local_store")

object AuthLocalStore {
    private val KEY_USER = stringPreferencesKey("auth_user")

    fun currentUser(context: Context) =
        context.dataStore.data.map { it[KEY_USER] }

    suspend fun setUser(context: Context, user: String?) {
        context.dataStore.edit { prefs ->
            if (user == null) prefs.remove(KEY_USER) else prefs[KEY_USER] = user
        }
    }
}
