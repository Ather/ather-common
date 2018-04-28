package com.atherapp.common.providers.v3;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public class MemoryCacheProviderFileSystem<FSType extends ProviderFileSystem> extends ProviderFileSystem {
	@Override
	public FileSystemProvider provider() {
		return null;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public String getSeparator() {
		return null;
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		return null;
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		return null;
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return null;
	}

	@Override
	public Path getPath(String first, String... more) {
		return null;
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		return null;
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		return null;
	}

	@Override
	public WatchService newWatchService() throws IOException {
		return null;
	}
}
