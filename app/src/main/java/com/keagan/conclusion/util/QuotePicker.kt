package com.keagan.conclusion.util

object QuotePicker {
    private val quotes = listOf(
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

    fun all(): List<String> = quotes

    /** Deterministic “quote of the day” by epoch day */
    fun quoteForDay(epochDays: Int): String {
        val idx = indexInBounds(epochDays, quotes.size)
        return quotes[idx]
    }

    /** Wrap any integer into [0, size) */
    fun indexInBounds(value: Int, size: Int): Int {
        if (size <= 0) return 0
        val m = value % size
        return if (m < 0) m + size else m
    }
}
