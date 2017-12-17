/**
 * 
 */
package media.thehoard.common.providers.generic.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import media.thehoard.common.providers.generic.Provider;
import media.thehoard.common.providers.generic.ProviderConfiguration;
import media.thehoard.common.providers.generic.authorization.ProviderAuthorization;
import media.thehoard.common.providers.generic.authorization.ProviderCredential;
import media.thehoard.common.providers.generic.models.ProviderFile;

/**
 * @author Michael Haas
 *
 */
public abstract class ProviderService<
		AuthorizationClass extends ProviderAuthorization<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ConfigurationClass extends ProviderConfiguration<ConfigurationClass>,
		CredentialClass extends ProviderCredential<CredentialClass>,
		FileClass extends ProviderFile<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ProviderClass extends Provider<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ServiceClass extends ProviderService<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>> {
	public abstract About about();

	public abstract class About {
		// public abstract void get();
	}

	public abstract Cache<?> cache();

	public abstract class Cache<T extends FileClass> {
		public abstract void build();

		public abstract void update() throws IOException;

		public abstract void update(T updatedFile);
	}

	public abstract Changes changes();

	public abstract class Changes {
		public abstract String getChangeToken() throws IOException;

		// public abstract List<ProviderFile> list();

		public abstract void process() throws IOException;
	}

	public abstract Files<?> files();

	public abstract class Files<T extends FileClass> {
		public abstract T get(String providerFileId, boolean doPersist) throws IOException;
		
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

		// public abstract String generateVirtualPath(String providerFileId);
	}

	public abstract Permissions permissions();

	public abstract class Permissions {

	}
}