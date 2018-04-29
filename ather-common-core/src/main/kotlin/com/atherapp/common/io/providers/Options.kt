package com.atherapp.common.io.providers

import java.text.ParseException

interface OpenOption {
    /**
     * @return this option as an http header
     */
    val header: Pair<String, String>

    /**
     * @return whether this option can be ignored or not
     */
    val mandatory: Boolean

    companion object {
        /**
         * Adds the given options to the given headers map
         */
        fun openOptionAddHeaders(options: Array<OpenOption>, headers: MutableMap<String, String>) {
            for (option in options) {
                val (key, value) = option.header
                if (key != "" && value != "")
                    headers[key] = value
            }
        }

        /**
         * Adds each header found in options to the headers map, skipping empty K/V pairs.
         *
         * @return empty map if options are empty
         */
        fun openOptionHeaders(options: Array<OpenOption>): MutableMap<String, String> {
            val headers = mutableMapOf<String, String>()
            openOptionAddHeaders(options, headers)
            return headers
        }
    }
}

fun LongRange.toRangeOption() = RangeOption(this.start, this.last)

class RangeOption(override var start: Long, override var endInclusive: Long) : ClosedRange<Long>, OpenOption {
    override val header: Pair<String, String>
        get() {
            var value = ""
            if (this.start >= 0)
                value += this.start.toString()
            value += "-"
            if (this.endInclusive >= 0)
                value += this.endInclusive.toString()

            return "Range" to value
        }

    override val mandatory: Boolean = true

    /**
     * Interprets the [RangeOption] into an offset and limit
     *
     * The offset is the start of the stream and the limit is
     * how many bytes should be read from it. If the limit is -1,
     * then the stream should be read to the end.
     *
     * @return [Pair] of Offset then Limit
     */
    fun decode(size: Long): Pair<Long, Long> {
        val offset: Long
        val limit: Long

        if (start >= 0) {
            offset = this.start
            limit = if (endInclusive >= 0)
                endInclusive - start + 1
            else
                -1
        } else {
            offset = if (endInclusive >= 0)
                size - endInclusive
            else 0

            limit = -1
        }

        return offset to limit
    }

    companion object {
        /**
         * Parse a header string into a [RangeOption]
         *
         * @throws [ParseException] if the range is invalid
         */
        fun parse(header: String): RangeOption {
            val preamble = "bytes="
            if (!header.startsWith(preamble))
                throw ParseException("Range header invalid, doesn't start with $preamble", 0)

            val range = header.substring(preamble.length)
            if (range.indexOf(',') >= 0)
                throw ParseException("Range header invalid, contains multiple ranges, which isn't supported", range.indexOf(','))

            val dash = range.indexOf('-')
            if (dash < 0)
                throw ParseException("Range head invalid, doesn't contains '-'", dash)

            val start = range.substring(0..dash)
            val end = range.substring(dash + 1)

            val startVal: Long
            val endVal: Long

            startVal = if (start != "")
                start.toLongOrNull() ?: throw ParseException("Invalid range start", 0)
            else
                -1
            endVal = if (end != "")
                end.toLongOrNull() ?: throw ParseException("Invalid range end", 0)
            else -1

            return (startVal..endVal).toRangeOption()
        }

        fun fixRangeOption(options: Array<OpenOption>, size: Long) {
            for (option in options) {
                if (option is RangeOption) {
                    if (option.start < 0) {
                        option.start = size - option.endInclusive
                        option.endInclusive = -1
                    }
                }
            }
        }
    }
}

/**
 * An HTTP range option with only a start
 */
class SeekOption(var offset: Long) : OpenOption {
    override val header: Pair<String, String>
        get() = "Range" to "bytes=$offset-"

    override val mandatory: Boolean = true
}

/**
 * A general purpose HTTP option
 */
class HttpOption(var key: String, var value: String) : OpenOption {
    override val header: Pair<String, String>
        get() = key to value

    override val mandatory: Boolean = false
}

/**
 * An option used to tell the local provider to limit
 * the number of hashes it calculates
 */
class HashesOption(var hashes: Set<Hash>) : OpenOption {
    override val header: Pair<String, String>
        get() = "" to ""

    override val mandatory: Boolean = false
}