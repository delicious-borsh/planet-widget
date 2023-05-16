package com.example.planetwidget.presentation

import org.junit.Test

internal class LongFormatKtTest {

    @Test
    fun separateWithDotsTest() {
        val input = 1231231232L

        val result = input.toFormattedString()

        assert(result == "1.231.231.232 km")
    }
}