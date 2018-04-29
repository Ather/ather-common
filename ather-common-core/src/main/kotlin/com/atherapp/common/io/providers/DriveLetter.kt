package com.atherapp.common.io.providers

import org.apache.commons.lang3.SystemUtils

object DriveLetter {
    fun isDriveLetter(name: String): Boolean {
        return if (SystemUtils.IS_OS_WINDOWS) {
            if (name.length != 1)
                return false
            val c = name[0]
            (c in 'a'..'z') || (c in 'A'..'Z')
        } else
            false
    }
}