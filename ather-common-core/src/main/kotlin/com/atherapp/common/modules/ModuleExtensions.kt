package com.atherapp.common.modules

fun <T : Any> List<T>.get(id: String) {
    for (t in this) {
        if (t is AtherExtensionPoint) {}

    }
}