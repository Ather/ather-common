package com.atherapp.common.json.gson

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken

/**
 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER int} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a string, it will attempt to parse it as an int.
 * If the next token's numeric value cannot be exactly represented by a Java {@code int},
 * this method throws.
 *
 * @throws IllegalStateException if the next token is not a literal value.
 * @throws NumberFormatException if the next literal value is not null but
 *      cannot be parsed as a number, or exactly represented as an int.
 */
fun JsonReader.nextIntOrNull(): Int? {
    if (this.peek() != JsonToken.NULL)
        return this.nextInt()
    this.nextNull()
    return null
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#BOOLEAN boolean} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 *
 * @throws IllegalStateException if the next token is not a boolean or if
 *     this reader is closed.
 */
fun JsonReader.nextBooleanOrNull(): Boolean? {
    if (this.peek() != JsonToken.NULL)
        return this.nextBoolean()
    this.nextNull()
    return null
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER double} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a string, it will attempt to parse it as a double using {@link Double#parseDouble(String)}.
 *
 * @throws IllegalStateException if the next token is not a literal value.
 * @throws NumberFormatException if the next literal value cannot be parsed
 *     as a double, or is non-finite.
 */
fun JsonReader.nextDoubleOrNull(): Double? {
    if (this.peek() != JsonToken.NULL)
        return this.nextDouble()
    this.nextNull()
    return null
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER long} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a string, this method will attempt to parse it as a long.
 * If the next token's numeric value cannot be exactly represented by a Java {@code long}, this method throws.
 *
 * @throws IllegalStateException if the next token is not a literal value.
 * @throws NumberFormatException if the next literal value cannot be parsed
 *     as a number, or exactly represented as a long.
 */
fun JsonReader.nextLongOrNull(): Long? {
    if (this.peek() != JsonToken.NULL)
        return this.nextLong()
    this.nextNull()
    return null
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#STRING string} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a number, it will return its string form.
 *
 * @throws IllegalStateException if the next token is not a string or if
 *     this reader is closed.
 */
fun JsonReader.nextStringOrNull(): String? {
    if (this.peek() != JsonToken.NULL)
        return this.nextString()
    this.nextNull()
    return null
}