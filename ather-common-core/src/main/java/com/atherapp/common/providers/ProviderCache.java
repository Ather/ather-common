package com.atherapp.common.providers;

import com.atherapp.common.configuration.AtherConfiguration;
import com.atherapp.common.database.DatabaseConnector;
import com.atherapp.common.providers.drive.Drive;
import com.atherapp.common.providers.drive.models.DriveFile;
import com.atherapp.common.providers.generic.Provider;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.models.ProviderFileRelationship;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.atherapp.common.providers.drive.Drive;
import com.atherapp.common.providers.drive.models.DriveFile;
import com.atherapp.common.providers.generic.Provider;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.models.ProviderFileRelationship;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.atherapp.common.database.jooq.Tables.*;

/**
 * @author Michael Haas
 */
public class ProviderCache {
	public static final Logger PROVIDER_CACHE_LOGGER = LoggerFactory.getLogger(ProviderCache.class);

	private static LoadingCache<UUID, Provider<?, ?, ?, ?, ?, ?>> providerCache = Caffeine.newBuilder().maximumSize(30)
			.refreshAfterWrite(1, TimeUnit.HOURS).build(ProviderCache::loadProvider);
	private static LoadingCache<UUID, List<ProviderFileRelationship>> fileRelationshipCache = Caffeine.newBuilder()
			.maximumSize(40000).refreshAfterWrite(24, TimeUnit.HOURS).build(ProviderCache::loadFileRelationships);
	private static LoadingCache<UUID, ProviderFile<?, ?, ?, ?, ?, ?>> fileCache = Caffeine.newBuilder()
			.maximumSize(30000).refreshAfterWrite(12, TimeUnit.HOURS).build(ProviderCache::loadFile);

	private static Provider<?, ?, ?, ?, ?, ?> loadProvider(UUID key) {
		try (Connection conn = DatabaseConnector.getConnection()) {
			Record rs = DSL.using(AtherConfiguration.getJooqConfiguration(conn)).select(PROVIDERS.TYPEID)
					.from(PROVIDERS).where(PROVIDERS.UUID.eq(key.toString())).fetchOne();

			if (rs != null) {
				switch (rs.get(PROVIDERS.TYPEID)) {
					case Provider.ProviderTypes.DRIVE:
						return new Drive(key);
				}
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load provider into cache: " + key.toString(), e);
		}

		return null;
	}

	private static List<ProviderFileRelationship> loadFileRelationships(UUID key) {
		// Initialize to length 1, because most files have only one parent
		List<ProviderFileRelationship> relationships = new ArrayList<>(1);
		try (Connection conn = DatabaseConnector.getConnection()) {
			PROVIDER_CACHE_LOGGER.trace("Loading file relationships for file: " + key.toString());
			List<Record2<Integer, String>> records = DSL.using(AtherConfiguration.getJooqConfiguration(conn))
					.select(PROVIDERFILERELATIONSHIPS.ID, PROVIDERFILERELATIONSHIPS.PARENTUUID)
					.from(PROVIDERFILERELATIONSHIPS).where(PROVIDERFILERELATIONSHIPS.FILEUUID.eq(key.toString()))
					.fetch();

			PROVIDER_CACHE_LOGGER.trace("Adding relationship records to array: " + key.toString());
			for (Record rs : records)
				relationships.add(new ProviderFileRelationship(rs.get(PROVIDERFILERELATIONSHIPS.ID), key, UUID
						.fromString(rs.get(PROVIDERFILERELATIONSHIPS.PARENTUUID))));

			PROVIDER_CACHE_LOGGER.trace("Returning file relationships: " + key.toString());
			return relationships;
		} catch (DataAccessException | SQLException e) {
			PROVIDER_CACHE_LOGGER.warn("Failed to load provider file relationships into cache: " + key.toString(), e);
		}

		return relationships;
	}

	public static List<UUID> getChildUuids(UUID parentFile) {
		List<UUID> children = null;

		try (Connection conn = DatabaseConnector.getConnection()) {
			Result<Record1<String>> fetch = DSL.using(AtherConfiguration.getJooqConfiguration(conn))
					.select(PROVIDERFILERELATIONSHIPS.FILEUUID).from(PROVIDERFILERELATIONSHIPS)
					.where(PROVIDERFILERELATIONSHIPS.PARENTUUID.eq(parentFile.toString())).fetch();
			children = new ArrayList<>(fetch.size());
			for (Record rs : fetch)
				children.add(UUID.fromString(rs.get(PROVIDERFILERELATIONSHIPS.FILEUUID)));
		} catch (SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load file children: " + parentFile.toString(), e);
		}

		return children;
	}

	/* TODO
	private static <FileType extends ProviderFile<?, ?, ?, ?, ?, ?>> List<FileType> getChildrenFiles(UUID parentFile) {
		getChildUuids(parentFile).stream().map(e -> new FileType(e));
	}*/

	private static ProviderFile<?, ?, ?, ?, ?, ?> loadFile(UUID key) {
		try (Connection conn = DatabaseConnector.getConnection()) {
			Record rs = DSL.using(AtherConfiguration.getJooqConfiguration(conn)).select(PROVIDERFILES.PROVIDERUUID)
					.from(PROVIDERFILES).where(PROVIDERFILES.UUID.eq(key.toString())).fetchOne();

			if (rs != null) {
				Provider<?, ?, ?, ?, ?, ?> tempProvider = providerCache
						.get(UUID.fromString(rs.get(PROVIDERFILES.PROVIDERUUID)));
				if (tempProvider != null) {
					switch (tempProvider.getTypeId()) {
						case Provider.ProviderTypes.DRIVE:
							DriveFile tempFile = new DriveFile((Drive) tempProvider);
							tempFile.setUuid(key);
							tempFile.load();
							return tempFile;
					}
				}
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load provider into cache: " + key.toString(), e);
		}

		return null;
	}

	public static <FileClass extends ProviderFile<?, ?, ?, ?, ?, ?>> FileClass getProviderFile(UUID key) {
		return (FileClass) fileCache.get(key);
	}

	/*
	public static ProviderFile<?, ?, ?, ?, ?, ?> getProviderFile(UUID key) {
		return fileCache.get(key);
	}*/

	public static LoadingCache<UUID, ProviderFile<?, ?, ?, ?, ?, ?>> providerFiles() {
		return fileCache;
	}

	public static Provider<?, ?, ?, ?, ?, ?> getProvider(UUID key) {
		return providerCache.get(key);
	}

	public static LoadingCache<UUID, Provider<?, ?, ?, ?, ?, ?>> providers() {
		return providerCache;
	}

	public static List<ProviderFileRelationship> getFileRelationships(UUID key) {
		return fileRelationshipCache.get(key);
	}

	public static <FileClass extends ProviderFile<?, ?, ?, ?, ?, ?>> List<FileClass> getFileParents(UUID key) {
		List<FileClass> parents = new ArrayList<>(1);
		List<ProviderFileRelationship> tempRelationships = fileRelationshipCache.get(key);
		if (tempRelationships != null) for (ProviderFileRelationship relationship : tempRelationships)
			parents.add(getProviderFile(relationship.getParentUuid()));
		return parents;
	}

	public static LoadingCache<UUID, List<ProviderFileRelationship>> providerFileRelationships() {
		return fileRelationshipCache;
	}
}
