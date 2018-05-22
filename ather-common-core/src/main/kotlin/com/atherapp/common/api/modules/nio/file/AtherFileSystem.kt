package com.atherapp.common.api.modules.nio.file

import org.pf4j.ExtensionPoint
import java.nio.file.FileSystem
import java.nio.file.Path

abstract class AtherFileSystem : FileSystem(), ExtensionPoint {
    // Unix master race
    override fun getSeparator(): String = "/"

    /**
     * Example URIs
     *
     * @see FileSystem.getPath
     */
    abstract override fun getPath(first: String?, vararg more: String?): Path
}