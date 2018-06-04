package com.atherapp.common.modules

import com.atherapp.common.modules.manifest.ModuleManifest
import java.nio.file.Path

interface ModuleWrapper {
    val moduleManager: ModuleManager
    val moduleManifest: ModuleManifest
    val modulePath: Path
    val moduleClassLoader: ClassLoader
    var moduleFactory: ModuleFactory
    var moduleState: ModuleState
    //var runtimeMode: RuntimeMode

    val module: Module

    val moduleId
        get() = "${moduleManifest.group}/${moduleManifest.artifact}"
}

abstract class BaseModuleWrapper(
        override val moduleManager: ModuleManager,
        override val moduleManifest: ModuleManifest,
        override val modulePath: Path,
        override val moduleClassLoader: ClassLoader
) : ModuleWrapper {
    override var moduleFactory: ModuleFactory = DefaultModuleFactory

    override var moduleState: ModuleState = DefaultModuleState.CREATED

    abstract override var module: Module
        internal set
}