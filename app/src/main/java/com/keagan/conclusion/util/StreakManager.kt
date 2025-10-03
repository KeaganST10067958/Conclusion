package com.keagan.conclusion.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private val Context.streakDataStore by preferencesDataStore(name = "planner_streak")

class StreakManager(private val appContext: Context) {

    private val KEY_LAST_OPEN = stringPreferencesKey("last_open_yyyy_mm_dd")
    private val KEY_STREAK = intPreferencesKey("streak_count")
    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE


    suspend fun touchAndGetStreak(): Int {
        val ds = appContext.streakDataStore
        val today = LocalDate.now()
        var newCount = 0   // ✅ initialize so the compiler is happy

        ds.edit { prefs ->
            val lastStr = prefs[KEY_LAST_OPEN]
            val last = lastStr?.let { LocalDate.parse(it, dateFmt) }

            newCount = when {
                last == null -> 1
                last == today -> (prefs[KEY_STREAK] ?: 1)                // already counted today
                last.plusDays(1) == today -> (prefs[KEY_STREAK] ?: 0) + 1
                else -> 1                                                // streak broken
            }

            prefs[KEY_LAST_OPEN] = today.format(dateFmt)
            prefs[KEY_STREAK] = newCount
        }
        return newCount
    }


    /** Read current streak without modifying it. */
    suspend fun currentStreak(): Int {
        val prefs = appContext.streakDataStore.data.first()
        return prefs[KEY_STREAK] ?: 0
    }

    /** A deterministic “quote of the day” from a local list (works offline). */
    fun quoteOfToday(): String {
        val quotes = DailyQuotes.pool
        val idx = abs(LocalDate.now().toEpochDay().toInt()) % quotes.size
        return quotes[idx]
    }
}

private object DailyQuotes {
    // Add or replace freely. Keep short, motivational, study-oriented lines.
    val pool = listOf(
        "Small wins compound—study 20 minutes now.",
        "Show up today. Momentum beats perfection.",
        "Future you will thank present you.",
        "Consistency turns hard into habit.",
        "Start with the smallest possible step.",
        "You don’t need more time, just a start.",
        "Track the streak, not the stress.",
        "Progress, not perfection.",
        "Make it easy to begin—open the book.",
        "Done is greater than perfect."
    )
}
