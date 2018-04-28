/**
 * 
 */
package com.atherapp.common.providers.generic.authorization;

import java.io.Serializable;

/**
 * @author Michael Haas
 *
 */
public abstract class ProviderCredential<CredentialClass extends ProviderCredential<CredentialClass>> implements Serializable {
	private static final long serialVersionUID = 5065086762708067307L;	
}
