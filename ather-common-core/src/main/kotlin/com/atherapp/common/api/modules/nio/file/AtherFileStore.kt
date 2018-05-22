package com.atherapp.common.api.modules.nio.file

import org.pf4j.ExtensionPoint
import java.nio.file.FileStore

abstract class AtherFileStore : FileStore(), ExtensionPoint {

    /*
    /**
     * Get the basic information about an [AtherFileStore].
     *
     * @return [AtherFileStoreAbout] containing usage information
     *
     * @throws java.io.IOException if an I/O error occurs
     */
    abstract fun getAbout(): AtherFileStoreAbout

    @Deprecated("", ReplaceWith("getAbout().total"))
    override fun getTotalSpace(): Long = getAbout().total

    @Deprecated("", ReplaceWith("getAbout().free"))
    override fun getUnallocatedSpace(): Long = getAbout().free

    @Deprecated("", ReplaceWith("getAbout().free"))
    override fun getUsableSpace(): Long = getAbout().free*/

    //TODO Treat this entire object as an about call
}