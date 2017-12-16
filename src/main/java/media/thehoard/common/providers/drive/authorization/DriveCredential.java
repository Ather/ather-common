package media.thehoard.common.providers.drive.authorization;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import media.thehoard.common.providers.drive.Drive;
import media.thehoard.common.providers.generic.authorization.ProviderCredential;

import java.util.Collections;

public class DriveCredential extends ProviderCredential<DriveCredential> {
	private String accessToken;

	private Long expirationTimeMillis;

	private String refreshToken;

	private transient Credential tempCredential;

	public DriveCredential() {
	}

	public DriveCredential(Credential credential) {
		this.accessToken = credential.getAccessToken();
		this.expirationTimeMillis = credential.getExpirationTimeMilliseconds();
		this.refreshToken = credential.getRefreshToken();
	}

	public Credential getCredential(Drive drive) {
		if (tempCredential == null) {
			HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();
			tempCredential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory)
					.setClientSecrets(DriveAuthorization.clientSecrets)
					.setRefreshListeners(Collections
							                     .singleton(new DriveAuthorization.DriveCredentialRefreshListener(drive)))
					.build();
			tempCredential.setAccessToken(this.accessToken);
			tempCredential.setExpirationTimeMilliseconds(this.expirationTimeMillis);
			tempCredential.setRefreshToken(this.refreshToken);
		}

		return tempCredential;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public synchronized DriveCredential setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public Long getExpirationTimeMillis() {
		return expirationTimeMillis;
	}

	public synchronized DriveCredential setExpirationTimeMillis(Long expirationTimeMillis) {
		this.expirationTimeMillis = expirationTimeMillis;
		return this;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public synchronized DriveCredential setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}
}
