package com.atherapp.common.providers.generic.authorization;

import java.io.IOException;

import com.atherapp.common.providers.generic.ProviderConfiguration;
import com.atherapp.common.providers.generic.Provider;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.service.ProviderService;
import com.atherapp.common.providers.generic.ProviderConfiguration;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.service.ProviderService;

public abstract class ProviderAuthorization<
		AuthorizationClass extends ProviderAuthorization<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ConfigurationClass extends ProviderConfiguration<ConfigurationClass>,
		CredentialClass extends ProviderCredential<CredentialClass>,
		FileClass extends ProviderFile<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ProviderClass extends Provider<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ServiceClass extends ProviderService<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>> {
	protected ProviderClass provider;

	public ProviderAuthorization(ProviderClass provider) {
		this.provider = provider;
	}
	
	public ProviderAuthorization() {}
	
	public abstract AuthorizationClass self();

	public abstract String getAuthorizationUrl(boolean useSsl);

	public abstract void setAuthorization(boolean useSsl, String responseToken);

	public abstract ServiceClass getProviderService() throws IOException;

	public abstract ServiceClass getProviderService(CredentialClass credential);
	
	public AuthorizationClass setProvider(ProviderClass provider) {
		this.provider = provider;
		return self();
	}
}
