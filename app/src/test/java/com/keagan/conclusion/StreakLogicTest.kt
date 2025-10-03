package com.keagan.conclusion

import com.keagan.conclusion.util.StreakLogic
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test


class StreakLogicTest {

    @Test
    fun firstOpenStartsAtOne() {
        val today = LocalDate.of(2025, 1, 10)
        assertEquals(1, StreakLogic.nextCount(null, 0, today))
    }

    @Test
    fun sameDayDoesNotDoubleCount() {
        val today = LocalDate.of(2025, 1, 10)
        assertEquals(3, StreakLogic.nextCount(today, 3, today))
    }

    @Test
    fun consecutiveDayIncrements() {
        val yesterday = LocalDate.of(2025, 1, 9)
        val today = LocalDate.of(2025, 1, 10)
        assertEquals(6, StreakLogic.nextCount(yesterday, 5, today))
    }

    @Test
    fun gapResetsToOne() {
        val last = LocalDate.of(2025, 1, 8)
        val today = LocalDate.of(2025, 1, 10)
        assertEquals(1, StreakLogic.nextCount(last, 12, today))
    }
}