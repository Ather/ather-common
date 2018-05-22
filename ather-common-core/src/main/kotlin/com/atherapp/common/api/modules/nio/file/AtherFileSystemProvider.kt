package com.atherapp.common.api.modules.nio.file

import org.pf4j.ExtensionPoint
import java.nio.file.spi.FileSystemProvider

abstract class AtherFileSystemProvider : FileSystemProvider(), ExtensionPoint