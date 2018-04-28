/**
 *
 */
package com.atherapp.common.providers.generic.service;

import com.atherapp.common.providers.generic.Provider;
import com.atherapp.common.providers.generic.ProviderConfiguration;
import com.atherapp.common.providers.generic.authorization.ProviderAuthorization;
import com.atherapp.common.providers.generic.authorization.ProviderCredential;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.ProviderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Michael Haas
 */
public abstract class ProviderService<AuthorizationClass extends ProviderAuthorization<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>, ConfigurationClass extends ProviderConfiguration<ConfigurationClass>, CredentialClass extends ProviderCredential<CredentialClass>, FileClass extends ProviderFile<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>, ProviderClass extends Provider<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>, ServiceClass extends ProviderService<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>> {
	public abstract About about();

	public abstract Cache<?> cache();

	public abstract Changes changes();

	public abstract Files<?> files();

	public abstract Permissions permissions();

	public abstract ProviderClass getProvider();

	public abstract class About {
		// public v3 void get();
	}

	public abstract class Cache<T extends FileClass> {
		public abstract void build();

		public abstract void recursiveBuild(String rootPath);

		public abstract void verify();

		public abstract void update() throws IOException;

		public abstract void update(T updatedFile, boolean updateRemote) throws IOException;

		public abstract void updateParents(T driveFile, List<UUID> removeParents, List<UUID> addParents, boolean updateRemote);
	}

	public abstract class Changes {
		public abstract String getChangeToken() throws IOException;

		// public v3 List<ProviderFile> list();

		public abstract void process() throws IOException;
	}

	public abstract class Files<T extends FileClass> {
		public abstract T get(String providerFileId, boolean attemptLoad, boolean doPersist) throws IOException;

		public T get(String providerFileId, boolean doPersist) throws IOException {
			return get(providerFileId, doPersist, doPersist);
		}

		public T get(String providerFileId) throws IOException {
			return get(providerFileId, true);
		}

		public abstract byte[] cat(T file, long byteStart, long byteEnd) throws IOException;

		public byte[] cat(T file, long byteStart) throws IOException {
			return cat(file, byteStart, file.getSize());
		}

		public abstract File catToTempFile(T file, long byteStart, long byteEnd);

		public File catToTempFile(T file, long byteEnd) {
			return catToTempFile(file, 0, byteEnd);
		}

		public abstract boolean rm(T file);

		public abstract T mkdir(String virtualPath);

		public abstract List<FileClass> ls(String virtualPath);

		public abstract String getTemporaryUrl(T file, boolean doProxy);

		// XXX This should be overridden if a provider doesn't support temporary links
		public String getTemporaryUrl(T file) {
			return getTemporaryUrl(file, false);
		}

		// public v3 String generateVirtualPath(String providerFileId);

		//TODO
		public abstract T create(T file, boolean doPersist) throws IOException;
	}

	public abstract class Permissions {

	}
}
