/**
 *
 */
package com.atherapp.common.database.tables;

import com.atherapp.common.providers.generic.ProviderConfiguration;
import com.atherapp.common.providers.generic.authorization.ProviderCredential;
import com.atherapp.common.providers.generic.service.ProviderService;
import com.atherapp.common.providers.generic.ProviderConfiguration;
import com.atherapp.common.providers.generic.authorization.ProviderCredential;
import com.atherapp.common.providers.generic.service.ProviderService;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Michael Haas
 */
@Deprecated
@Entity
@Table(name = "Provider")
public class Provider<CredentialClass extends ProviderCredential<?>, ConfigurationClass extends ProviderConfiguration<?>, ProviderServiceClass extends ProviderService<?, ?, ?, ?, ?, ?>> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id", unique = true, nullable = false, insertable = false, updatable = false)
	private Long id;

	@Column(name = "Uuid", unique = true, nullable = false)
	private String uuid;

	@Column(name = "Alias")
	private String alias;

	@Column(name = "ProviderId", nullable = false)
	private Integer providerId;

	@Column(name = "Type", nullable = false)
	private Byte type;

	@Column(name = "Status", nullable = false)
	private Byte status;

	@Column(name = "DateCreated", nullable = false)
	private Timestamp dateCreated;

	@Column(name = "DateUpdated", nullable = false)
	private Timestamp dateUpdated;

	@Column(name = "BackoffDate")
	private Timestamp backoffDate;

	@Column(name = "Configuration", nullable = false)
	private ConfigurationClass configuration;

	@Column(name = "Credential", nullable = false)
	private CredentialClass credential;

	/**
	 * Set nulled values to defaults on creation
	 */
	@PrePersist
	protected void onCreate() {
		if (uuid == null) uuid = UUID.randomUUID().toString();
		if (alias == null) alias = "";
		if (type == null) type = com.atherapp.common.providers.generic.Provider.ProviderUsages.REPOSITORY;
		if (status == null) status = com.atherapp.common.providers.generic.Provider.ProviderStatus.DISABLED;
		if (dateCreated == null) dateCreated = new Timestamp(System.currentTimeMillis());
		if (dateUpdated == null) dateUpdated = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Update the update date when updating the provider
	 */
	@PreUpdate
	protected void onUpdate() {
		dateUpdated = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setId(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return UUID.fromString(uuid);
	}

	/**
	 * @param uuid the uuid to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setUuid(UUID uuid) {
		this.uuid = uuid.toString();
		return this;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	/**
	 * @return the providerId
	 */
	public Integer getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId the providerId to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setProviderId(Integer providerId) {
		this.providerId = providerId;
		return this;
	}

	/**
	 * @return the type
	 */
	public Byte getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setType(Byte type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setStatus(Byte status) {
		this.status = status;
		return this;
	}

	/**
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
		return this;
	}

	/**
	 * @return the dateUpdated
	 */
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * @param dateUpdated the dateUpdated to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
		return this;
	}

	/**
	 * @return the backoffDate
	 */
	public Timestamp getBackoffDate() {
		return backoffDate;
	}

	/**
	 * @param backoffDate the backoffDate to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setBackoffDate(Timestamp backoffDate) {
		this.backoffDate = backoffDate;
		return this;
	}

	/**
	 * @return the configuration
	 */
	public ConfigurationClass getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setConfiguration(ConfigurationClass configuration) {
		this.configuration = configuration;
		return this;
	}

	/**
	 * @return the credential
	 */
	public CredentialClass getCredential() {
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */
	public Provider<CredentialClass, ConfigurationClass, ProviderServiceClass> setCredential(CredentialClass credential) {
		this.credential = credential;
		return this;
	}
}
