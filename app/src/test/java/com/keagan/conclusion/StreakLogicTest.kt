package com.keagan.conclusion

import com.keagan.conclusion.util.StreakLogic
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreakLogicTest {

    @Test
    fun firstOpenStartsAtOne() {
        val today = LocalDate.of(2025, 1, 1)
        assertEquals(1, StreakLogic.nextCount(lastOpen = null, count = 0, today = today))
    }

    @Test
    fun sameDayDoesNotDoubleCount() {
        val today = LocalDate.of(2025, 1, 1)
        assertEquals(3, StreakLogic.nextCount(lastOpen = today, count = 3, today = today))
    }

    @Test
    fun consecutiveDayIncrements() {
        val yesterday = LocalDate.of(2025, 1, 1)
        val today = LocalDate.of(2025, 1, 2)
        assertEquals(6, StreakLogic.nextCount(lastOpen = yesterday, count = 5, today = today))
    }

    @Test
    fun gapResetsToOne() {
        val twoDaysAgo = LocalDate.of(2025, 1, 1)
        val today = LocalDate.of(2025, 1, 3)
        assertEquals(1, StreakLogic.nextCount(lastOpen = twoDaysAgo, count = 9, today = today))
    }
}
