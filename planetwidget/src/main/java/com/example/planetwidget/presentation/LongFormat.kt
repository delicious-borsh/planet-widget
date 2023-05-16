package com.example.planetwidget.presentation

fun Long.toFormattedString(): String {
    return this
        .toString()
        .reversed()
        .splitInSubstringsOf3OrLess()
        .joinToString(separator = ".")
        .reversed() + " km"
}

private fun String.splitInSubstringsOf3OrLess(): List<String> {
    val array = this.toCharArray()

    val result = mutableListOf<String>()
    val buffer = mutableListOf<Char>()

    array.forEachIndexed { index, char ->
        buffer.add(char)

        if (index == this.length - 1) {
            result.add(buffer.joinToString(separator = ""))
            return result
        }

        if (buffer.size == 3) {
            result.add(buffer.joinToString(separator = ""))
            buffer.clear()
        }
    }
    return result
}