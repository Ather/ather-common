package com.atherapp.common.json.gson

import kotlin.reflect.KClass

internal object DefaultInterfaceAdapters {
    val adapters: Array<Pair<KClass<out Any>, InterfaceImplementationAdapter<out Any, *>>>
        get() = arrayOf()
}