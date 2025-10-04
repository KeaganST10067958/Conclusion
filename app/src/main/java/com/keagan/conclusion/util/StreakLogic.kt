package com.keagan.conclusion.util

import java.time.LocalDate

object StreakLogic {
    /**
     * @param lastOpen date of the last time the user opened/checked in (nullable when first time)
     * @param count current streak count (0+)
     * @param today date “now”
     * @return new streak count based on gap from lastOpen
     */
    fun nextCount(lastOpen: LocalDate?, count: Int, today: LocalDate): Int {
        if (lastOpen == null) return 1
        val gap = java.time.Period.between(lastOpen, today).days
        return when (gap) {
            0 -> count                   // same day, no double counting
            1 -> count + 1               // consecutive day
            else -> 1                    // missed at least one day, reset
        }
    }
}
