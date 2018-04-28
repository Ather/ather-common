package com.atherapp.common.api.modules.ids

import com.atherapp.common.modules.AtherExtensionPoint

/**
 * ID Generation Module, used for generating random IDs, used for various IDs
 */
interface IdModule : AtherExtensionPoint {
    /**
     * Create a new ID, returning it's [String] representation
     */
    fun newId(): String

    /**
     * Create several IDs, using the passed [count], returning an [Array] of string IDs
     */
    fun newIds(count: Int): Array<String> = Array(count) { newId() }
}