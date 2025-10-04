package com.keagan.conclusion

import com.keagan.conclusion.util.QuotePicker
import org.junit.Assert.assertTrue
import org.junit.Test

class QuotePickerTest {

    @Test
    fun indexInBounds_wrapsIntoRange() {
        val size = 10
        // negative and large positive both wrap
        val a = QuotePicker.indexInBounds(-1, size)
        val b = QuotePicker.indexInBounds(11, size)
        assertTrue(a in 0 until size)
        assertTrue(b in 0 until size)
    }

    @Test
    fun quoteForDay_isStable() {
        val q1 = QuotePicker.quoteForDay(19_000) // arbitrary day index
        val q2 = QuotePicker.quoteForDay(19_000)
        assertTrue(q1 == q2)
        assertTrue(QuotePicker.all().contains(q1))
    }
}
