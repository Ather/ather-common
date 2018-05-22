package com.atherapp.common.nio.file

import com.atherapp.common.api.modules.nio.file.AtherFileSystemProvider
import java.net.URI
import java.nio.channels.SeekableByteChannel
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileAttribute
import java.nio.file.attribute.FileAttributeView

class BaseFileSystemProvider: AtherFileSystemProvider() {
    override fun checkAccess(path: Path?, vararg modes: AccessMode?) {
        TODO("not implemented")
    }

    override fun copy(source: Path?, target: Path?, vararg options: CopyOption?) {
        TODO("not implemented")
    }

    override fun <V : FileAttributeView?> getFileAttributeView(path: Path?, type: Class<V>?, vararg options: LinkOption?): V {
        TODO("not implemented")
    }

    override fun isSameFile(path: Path?, path2: Path?): Boolean {
        TODO("not implemented")
    }

    override fun newFileSystem(uri: URI?, env: MutableMap<String, *>?): FileSystem {
        TODO("not implemented")
    }

    override fun getScheme(): String = "atr"

    override fun isHidden(path: Path?): Boolean {
        TODO("not implemented")
    }

    override fun newDirectoryStream(dir: Path?, filter: DirectoryStream.Filter<in Path>?): DirectoryStream<Path> {
        TODO("not implemented")
    }

    override fun newByteChannel(path: Path?, options: MutableSet<out OpenOption>?, vararg attrs: FileAttribute<*>?): SeekableByteChannel {
        TODO("not implemented")
    }

    override fun delete(path: Path?) {
        TODO("not implemented")
    }

    override fun <A : BasicFileAttributes?> readAttributes(path: Path?, type: Class<A>?, vararg options: LinkOption?): A {
        TODO("not implemented")
    }

    override fun readAttributes(path: Path?, attributes: String?, vararg options: LinkOption?): MutableMap<String, Any> {
        TODO("not implemented")
    }

    override fun getFileSystem(uri: URI?): FileSystem {
        TODO("not implemented")
    }

    override fun getPath(uri: URI?): Path {
        TODO("not implemented")
    }

    override fun getFileStore(path: Path?): FileStore {
        TODO("not implemented")
    }

    override fun setAttribute(path: Path?, attribute: String?, value: Any?, vararg options: LinkOption?) {
        TODO("not implemented")
    }

    override fun move(source: Path?, target: Path?, vararg options: CopyOption?) {
        TODO("not implemented")
    }

    override fun createDirectory(dir: Path?, vararg attrs: FileAttribute<*>?) {
        TODO("not implemented")
    }
}