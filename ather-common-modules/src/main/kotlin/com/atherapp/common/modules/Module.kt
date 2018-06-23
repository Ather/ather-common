package com.atherapp.common.modules

import mu.KLogging

/**
 * Basic Module which loads additional functionality at runtime
 */
interface Module : ModuleWrapper {
    val wrapper: ModuleWrapper

    fun start()

    fun stop()
}

abstract class BaseModule(
        override val wrapper: ModuleWrapper
) : Module, ModuleWrapper by wrapper {
    companion object : KLogging()
}