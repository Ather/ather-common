package com.atherapp.common.extensions

private val stringDelimiters: Regex = Regex(" |,|.|:|;|\\n|\\t")

fun String.firstToUpper(): String {
    require(!isNullOrEmpty())

    val trimmedString: String = this.trim()

    if (trimmedString.length > 1)
        return Character.toUpperCase(trimmedString[0]) + trimmedString.substring(1).toLowerCase()

    return trimmedString.toUpperCase()
}

fun String.wordCount(): Int {
    if (isNullOrEmpty())
        return 0

    val words: List<String> = split(stringDelimiters)
    return words.filter { !isNullOrEmpty() }.size
}

fun String.containsSpace(): Boolean = contains(' ')
