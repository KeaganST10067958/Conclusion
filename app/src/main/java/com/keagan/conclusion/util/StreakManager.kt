package com.keagan.conclusion.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

// Single DataStore instance for streaks (scoped to Context)
private val Context.streakDataStore by preferencesDataStore(name = "planner_streak")

/** Pure logic so we can unit-test date math without Android/DataStore. */
object StreakLogic {
    /**
     * Returns the new streak given the last open date, current count, and today.
     * Rules:
     * - First ever open -> 1
     * - Same day -> keep currentCount (min 1)
     * - Consecutive next day -> +1
     * - Any gap -> 1
     */
    fun nextCount(lastOpen: LocalDate?, currentCount: Int, today: LocalDate): Int = when {
        lastOpen == null -> 1
        lastOpen == today -> if (currentCount <= 0) 1 else currentCount
        lastOpen.plusDays(1) == today -> currentCount + 1
        else -> 1
    }
}

class StreakManager(private val appContext: Context) {
    private val KEY_LAST_OPEN = stringPreferencesKey("last_open_yyyy_mm_dd")
    private val KEY_STREAK = intPreferencesKey("streak_count")
    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Call once per app start/day (we trigger it in App()).
     * Returns the updated streak.
     */
    suspend fun touchAndGetStreak(): Int {
        val ds = appContext.streakDataStore
        val today = LocalDate.now()
        var newCount = 0

        ds.edit { prefs ->
            val lastStr = prefs[KEY_LAST_OPEN]
            val last = lastStr?.let { LocalDate.parse(it, dateFmt) }
            val current = prefs[KEY_STREAK] ?: 0

            newCount = StreakLogic.nextCount(
                lastOpen = last,
                currentCount = current,
                today = today
            )

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

    /** Deterministic local quote-of-the-day (works offline). */
    fun quoteOfToday(): String {
        val quotes = DailyQuotes.pool
        val idx = abs(LocalDate.now().toEpochDay().toInt()) % quotes.size
        return quotes[idx]
    }
}

private object DailyQuotes {
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
