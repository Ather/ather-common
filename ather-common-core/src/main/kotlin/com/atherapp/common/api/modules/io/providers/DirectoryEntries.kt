package com.atherapp.common.api.modules.io.providers

/**
 * A slice of [ProviderObject] or [Directory]
 */
typealias DirectoryEntries = Array<DirectoryEntry>

/**
 * Return a string description of the [DirectoryEntry],
 * either "object", "directory", or "unknown"
 */
fun DirectoryEntry.type(): String {
    return when (this) {
        is ProviderObject -> "object"
        is Directory -> "directory"
        else -> "unknown"
    }
}