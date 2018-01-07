/**
 * 
 */
package media.thehoard.common.providers.drive.authorization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

import media.thehoard.common.configuration.HoardConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;

import media.thehoard.common.providers.drive.DriveConfiguration;
import media.thehoard.common.providers.drive.Drive;
import media.thehoard.common.providers.drive.models.DriveFile;
import media.thehoard.common.providers.drive.service.DriveService;
import media.thehoard.common.providers.generic.authorization.ProviderAuthorization;

/**
 * @author Michael Haas
 *
 */
public class DriveAuthorization extends ProviderAuthorization<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	public static final Logger DRIVE_AUTHORIZATION_LOGGER = LoggerFactory.getLogger(DriveAuthorization.class);

	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public static GoogleClientSecrets clientSecrets;

	static {
		loadSecrets();
	}

	/**
	 * @param provider
	 */
	public DriveAuthorization(Drive provider) {
		super(provider);

		loadSecrets();
	}

	public DriveAuthorization() {
		loadSecrets();
	}

	private static void loadSecrets() {
		try {
			//TODO Make this more customizable
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
					new InputStreamReader(new FileInputStream(new File(HoardConfiguration.CONFIGURATION_LOCATION + "providers/drive/client_secret.json"))));
		} catch (IOException e) {
			DRIVE_AUTHORIZATION_LOGGER.error("Failed to initialize google client secrets for Google Drive authorization", e);
		}
	}

	@Override
	public DriveAuthorization self() {
		return this;
	}

	@Override
	public String getAuthorizationUrl(boolean useSsl) {
		return new GoogleAuthorizationCodeRequestUrl(clientSecrets, getMatchingRedirectUri(useSsl), Arrays.asList(DriveScopes.DRIVE)).setState(provider.getUuid().toString())
				.setApprovalPrompt("force").setAccessType("offline").build();
	}

	private String getMatchingRedirectUri(boolean useSsl) {
		String returnUri = null;

		for (String redirectUri : clientSecrets.getDetails().getRedirectUris()) {
			if (redirectUri.contains("/settings/providers/callback/drive")) {
				returnUri = redirectUri;
				if (useSsl && redirectUri.startsWith("https://"))
					return returnUri;
			}
		}

		return returnUri;
	}

	@Override
	public void setAuthorization(boolean useSsl, String responseToken) {
		try {
			HttpTransport transport = new NetHttpTransport();
			Credential tempCredential = new GoogleCredential.Builder().setRefreshListeners(Collections.singleton(new DriveCredentialRefreshListener(this.provider)))
					.setTransport(transport).setJsonFactory(JSON_FACTORY).setClientSecrets(clientSecrets).build()
					.setFromTokenResponse(new GoogleAuthorizationCodeTokenRequest(transport, JSON_FACTORY, clientSecrets.getDetails().getClientId(),
							clientSecrets.getDetails().getClientSecret(), responseToken, getMatchingRedirectUri(useSsl)).execute());

			com.google.api.services.drive.Drive tempDrive = new com.google.api.services.drive.Drive.Builder(transport, JSON_FACTORY, tempCredential).setApplicationName(DriveService.APPLICATION_NAME).build();

			try {
				About about = tempDrive.about().get().setFields("user(emailAddress, permissionId)").execute();

				this.provider.update(this, new DriveCredential(tempCredential), about);
			} catch (IOException e) {
				throw new IOException("Failed to update credential entry with new authorization information.", e);
			}
		} catch (IOException e) {
			DRIVE_AUTHORIZATION_LOGGER.error("Failed to save authorization information for Google Drive.", e);
		}
	}

	public static class DriveCredentialRefreshListener implements CredentialRefreshListener {
		private Drive drive;

		public DriveCredentialRefreshListener(Drive drive) {
			this.drive = drive;
		}

		@Override
		public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
			DRIVE_AUTHORIZATION_LOGGER.info("Persisting updated credential for provider: " + this.drive.getUuid().toString());
			this.drive.persistCredential(new DriveCredential(credential));
		}

		@Override
		public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
			DRIVE_AUTHORIZATION_LOGGER.error("Failed to refresh drive authorization token: " + drive.getUuid().toString(), tokenErrorResponse.toString());
		}
	}

	private DriveService service = null;

	@Override
	public DriveService getProviderService() {
		if (service == null)
			service = new DriveService(this.provider);

		return service;
	}

	@Override
	public DriveService getProviderService(DriveCredential credential) {
		return new DriveService(this.provider, credential);
	}
}
