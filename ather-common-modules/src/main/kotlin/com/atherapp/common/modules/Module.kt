package com.atherapp.common.modules

import mu.KLogging

/**
 * Basic Module which loads additional functionality at runtime
 */
interface Module {
    val wrapper: ModuleWrapper

    fun start()

    fun stop()
}

abstract class BaseModule : Module {
    companion object : KLogging()
}