package com.keagan.conclusion.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Simple streak tracker:
 * - Stores last done day (YYYY-MM-DD) and current streak count in DataStore.
 * - Resets to 0 if you miss a day (i.e., not done yesterday or today).
 * - Increments if you mark today after doing it yesterday; stays same if already marked today.
 */
class StreakManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "streak_store")

    private val KEY_STREAK  = intPreferencesKey("streak_count")
    private val KEY_LASTDAY = stringPreferencesKey("streak_last_day") // YYYY-MM-DD
    private val FMT = DateTimeFormatter.ISO_LOCAL_DATE

    /** Observe the current streak as a Flow<Int>. */
    fun observe(): Flow<Int> = context.dataStore.data.map { prefs ->
        val saved = prefs[KEY_STREAK] ?: 0
        val last  = prefs[KEY_LASTDAY]?.let { LocalDate.parse(it, FMT) }
        val today = LocalDate.now()

        // If last completion was yesterday or today, keep saved value.
        // If it's another day (missed), return 0 (but do not write; write happens on mark/load if desired)
        return@map when {
            last == null -> 0
            last == today || last == today.minusDays(1) -> saved
            else -> 0
        }
    }

    /** Mark today as done: increments if yesterday was done, sets to 1 if not, keeps same if already done today. */
    suspend fun markTodayDone() {
        val today = LocalDate.now()
        context.dataStore.edit { prefs ->
            val saved = prefs[KEY_STREAK] ?: 0
            val last  = prefs[KEY_LASTDAY]?.let { LocalDate.parse(it, FMT) }

            val newCount = when {
                last == null -> 1
                last == today -> saved            // already marked today
                last == today.minusDays(1) -> saved + 1
                else -> 1                         // missed a day, reset streak
            }

            prefs[KEY_STREAK] = newCount
            prefs[KEY_LASTDAY] = today.format(FMT)
        }
    }
}
