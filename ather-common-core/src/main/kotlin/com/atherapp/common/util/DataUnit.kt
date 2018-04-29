package com.atherapp.common.util

/**
 * @author Michael Haas
 */
enum class DataUnit constructor(private val base: Double, private val exponent: Double) {
    Bit(1.0, 0.0),
    Kilobit(1000.0, 1.0),
    Megabit(1000.0, 2.0),
    Gigabit(1000.0, 3.0),
    Terabit(1000.0, 4.0),
    Petabit(1000.0, 5.0),

    Byte(8.0, 0.0),
    Kilobyte(8 * 1000.0, 1.0),
    Megabyte(8 * 1000.0, 2.0),
    Gigabyte(8 * 1000.0, 3.0),
    Terabyte(8 * 1000.0, 4.0),
    Petabyte(8 * 1000.0, 5.0),

    Kibibyte(1024.0, 1.0),
    Mebibyte(1024.0, 2.0),
    Gibibyte(1024.0, 3.0),
    Tebibyte(1024.0, 4.0),
    Pebibyte(1024.0, 5.0);

    private val bitValue get() = Math.pow(base, exponent)

    fun toBit(value: Long) = toBit(value.toDouble())
    fun toBit(value: Double) = (value / Bit.bitValue).toLong()
    fun toKilobit(value: Long) = toKilobit(value.toDouble())
    fun toKilobit(value: Double) = value / Kilobit.bitValue
    fun toMegabit(value: Long) = toMegabit(value.toDouble())
    fun toMegabit(value: Double) = value / Megabit.bitValue
    fun toGigabit(value: Long) = toGigabit(value.toDouble())
    fun toGigabit(value: Double) = value / Gigabit.bitValue
    fun toTerabit(value: Long) = toTerabit(value.toDouble())
    fun toTerabit(value: Double) = value / Terabit.bitValue
    fun toPetabit(value: Long) = toPetabit(value.toDouble())
    fun toPetabit(value: Double) = value / Petabit.bitValue

    fun toByte(value: Long) = toBit(value) * 8.0
    fun toByte(value: Double) = toBit(value) * 8.0
    fun toKilobyte(value: Long) = toKilobit(value) * 8
    fun toKilobyte(value: Double) = toKilobit(value) * 8
    fun toMegabyte(value: Long) = toMegabit(value) * 8
    fun toMegabyte(value: Double) = toMegabit(value) * 8
    fun toGigabyte(value: Long) = toGigabit(value) * 8
    fun toGigabyte(value: Double) = toGigabit(value) * 8
    fun toTerabyte(value: Long) = toTerabit(value) * 8
    fun toTerabyte(value: Double) = toTerabit(value) * 8
    fun toPetabyte(value: Long) = toPetabit(value) * 8
    fun toPetabyte(value: Double) = toPetabit(value) * 8

    fun toKilibyte(value: Long) = toKibibyte(value.toDouble())
    fun toKibibyte(value: Double) = value / Kibibyte.bitValue
    fun toMegibyte(value: Long) = toMebibyte(value.toDouble())
    fun toMebibyte(value: Double) = value / Mebibyte.bitValue
    fun toGigibyte(value: Long) = toGibibyte(value.toDouble())
    fun toGibibyte(value: Double) = value / Gibibyte.bitValue
    fun toTeribyte(value: Long) = toTebibyte(value.toDouble())
    fun toTebibyte(value: Double) = value / Tebibyte.bitValue
    fun toPetibyte(value: Long) = toPebibyte(value.toDouble())
    fun toPebibyte(value: Double) = value / Pebibyte.bitValue
}
