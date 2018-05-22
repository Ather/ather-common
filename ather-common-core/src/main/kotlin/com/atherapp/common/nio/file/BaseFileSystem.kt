package com.atherapp.common.nio.file

import com.atherapp.common.api.modules.nio.file.AtherFileSystem
import java.nio.file.FileStore
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.WatchService
import java.nio.file.attribute.UserPrincipalLookupService
import java.nio.file.spi.FileSystemProvider

class BaseFileSystem : AtherFileSystem() {
    override fun getPath(first: String?, vararg more: String?): Path {
        TODO("not implemented")
    }

    override fun newWatchService(): WatchService {
        TODO("not implemented")
    }

    override fun supportedFileAttributeViews(): MutableSet<String> {
        TODO("not implemented")
    }

    override fun isReadOnly(): Boolean {
        TODO("not implemented")
    }

    override fun getFileStores(): MutableIterable<FileStore> {
        TODO("not implemented")
    }

    override fun provider(): FileSystemProvider {
        TODO("not implemented")
    }

    override fun isOpen(): Boolean {
        TODO("not implemented")
    }

    override fun getUserPrincipalLookupService(): UserPrincipalLookupService {
        TODO("not implemented")
    }

    override fun close() {
        TODO("not implemented")
    }

    override fun getPathMatcher(syntaxAndPattern: String?): PathMatcher {
        TODO("not implemented")
    }

    override fun getRootDirectories(): MutableIterable<Path> {
        TODO("not implemented")
    }
}
