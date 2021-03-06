/*
 * This file is generated by jOOQ.
 */
package com.atherapp.common.database.jooq;


import com.atherapp.common.database.jooq.tables.*;
import com.atherapp.common.database.jooq.tables.*;
import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.AbstractKeys;

import javax.annotation.Generated;


/**
 * A class modelling indexes of tables of the <code>thehoard</code> schema.
 */
@Generated(value = {"http://www.jooq.org", "jOOQ version:3.10.1"}, comments = "This class is generated by jOOQ")
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Indexes {

	// -------------------------------------------------------------------------
	// INDEX definitions
	// -------------------------------------------------------------------------

	public static final Index AUTHENTICATION_ID_UNIQUE = Indexes0.AUTHENTICATION_ID_UNIQUE;
	public static final Index AUTHENTICATION_PRIMARY = Indexes0.AUTHENTICATION_PRIMARY;
	public static final Index DEVICES_ID_UNIQUE = Indexes0.DEVICES_ID_UNIQUE;
	public static final Index DEVICES_PRIMARY = Indexes0.DEVICES_PRIMARY;
	public static final Index DISTRIBUTORDESTINATIONS_ID_UNIQUE = Indexes0.DISTRIBUTORDESTINATIONS_ID_UNIQUE;
	public static final Index DISTRIBUTORDESTINATIONS_PRIMARY = Indexes0.DISTRIBUTORDESTINATIONS_PRIMARY;
	public static final Index DISTRIBUTORDIRECTORIES_ID_UNIQUE = Indexes0.DISTRIBUTORDIRECTORIES_ID_UNIQUE;
	public static final Index DISTRIBUTORDIRECTORIES_PRIMARY = Indexes0.DISTRIBUTORDIRECTORIES_PRIMARY;
	public static final Index DISTRIBUTORQUEUE_ID_UNIQUE = Indexes0.DISTRIBUTORQUEUE_ID_UNIQUE;
	public static final Index DISTRIBUTORQUEUE_PRIMARY = Indexes0.DISTRIBUTORQUEUE_PRIMARY;
	public static final Index HOMEACCOUNTS_ID_UNIQUE = Indexes0.HOMEACCOUNTS_ID_UNIQUE;
	public static final Index HOMEACCOUNTS_PRIMARY = Indexes0.HOMEACCOUNTS_PRIMARY;
	public static final Index HOMES_ID_UNIQUE = Indexes0.HOMES_ID_UNIQUE;
	public static final Index HOMES_PRIMARY = Indexes0.HOMES_PRIMARY;
	public static final Index MEDIACHAPTERS_ID_UNIQUE = Indexes0.MEDIACHAPTERS_ID_UNIQUE;
	public static final Index MEDIACHAPTERS_PRIMARY = Indexes0.MEDIACHAPTERS_PRIMARY;
	public static final Index MEDIACHAPTERS_UUID_UNIQUE = Indexes0.MEDIACHAPTERS_UUID_UNIQUE;
	public static final Index MEDIAFILES_ID_UNIQUE = Indexes0.MEDIAFILES_ID_UNIQUE;
	public static final Index MEDIAFILES_PRIMARY = Indexes0.MEDIAFILES_PRIMARY;
	public static final Index MEDIAFILES_UUID_UNIQUE = Indexes0.MEDIAFILES_UUID_UNIQUE;
	public static final Index MEDIASTREAMS_ID_UNIQUE = Indexes0.MEDIASTREAMS_ID_UNIQUE;
	public static final Index MEDIASTREAMS_PRIMARY = Indexes0.MEDIASTREAMS_PRIMARY;
	public static final Index MEDIASTREAMS_UUID_UNIQUE = Indexes0.MEDIASTREAMS_UUID_UNIQUE;
	public static final Index PARTIES_ID_UNIQUE = Indexes0.PARTIES_ID_UNIQUE;
	public static final Index PARTIES_PRIMARY = Indexes0.PARTIES_PRIMARY;
	public static final Index PARTYACCOUNTS_ID_UNIQUE = Indexes0.PARTYACCOUNTS_ID_UNIQUE;
	public static final Index PARTYACCOUNTS_PRIMARY = Indexes0.PARTYACCOUNTS_PRIMARY;
	public static final Index PROVIDERFILERELATIONSHIPS_ID_UNIQUE = Indexes0.PROVIDERFILERELATIONSHIPS_ID_UNIQUE;
	public static final Index PROVIDERFILERELATIONSHIPS_PRIMARY = Indexes0.PROVIDERFILERELATIONSHIPS_PRIMARY;
	public static final Index PROVIDERFILERELATIONSHIPS_UUIDINDEX = Indexes0.PROVIDERFILERELATIONSHIPS_UUIDINDEX;
	public static final Index PROVIDERFILES_FILEINDEX = Indexes0.PROVIDERFILES_FILEINDEX;
	public static final Index PROVIDERFILES_ID_UNIQUE = Indexes0.PROVIDERFILES_ID_UNIQUE;
	public static final Index PROVIDERFILES_PRIMARY = Indexes0.PROVIDERFILES_PRIMARY;
	public static final Index PROVIDERFILES_UUID_UNIQUE = Indexes0.PROVIDERFILES_UUID_UNIQUE;
	public static final Index PROVIDERS_ID_UNIQUE = Indexes0.PROVIDERS_ID_UNIQUE;
	public static final Index PROVIDERS_PRIMARY = Indexes0.PROVIDERS_PRIMARY;
	public static final Index PROVIDERS_UUID_UNIQUE = Indexes0.PROVIDERS_UUID_UNIQUE;
	public static final Index PROVIDERSOLD_ID_UNIQUE = Indexes0.PROVIDERSOLD_ID_UNIQUE;
	public static final Index PROVIDERSOLD_PRIMARY = Indexes0.PROVIDERSOLD_PRIMARY;
	public static final Index PROVIDERSOLD_UUID_UNIQUE = Indexes0.PROVIDERSOLD_UUID_UNIQUE;
	public static final Index SERVERS_ID_UNIQUE = Indexes0.SERVERS_ID_UNIQUE;
	public static final Index SERVERS_PRIMARY = Indexes0.SERVERS_PRIMARY;
	public static final Index USERS_ID_UNIQUE = Indexes0.USERS_ID_UNIQUE;
	public static final Index USERS_NAME_UNIQUE = Indexes0.USERS_NAME_UNIQUE;
	public static final Index USERS_PRIMARY = Indexes0.USERS_PRIMARY;
	public static final Index VIRTUALPROVIDERPROVIDERS_ID_UNIQUE = Indexes0.VIRTUALPROVIDERPROVIDERS_ID_UNIQUE;
	public static final Index VIRTUALPROVIDERPROVIDERS_PRIMARY = Indexes0.VIRTUALPROVIDERPROVIDERS_PRIMARY;
	public static final Index VIRTUALPROVIDERS_ID_UNIQUE = Indexes0.VIRTUALPROVIDERS_ID_UNIQUE;
	public static final Index VIRTUALPROVIDERS_NAME_UNIQUE = Indexes0.VIRTUALPROVIDERS_NAME_UNIQUE;
	public static final Index VIRTUALPROVIDERS_PRIMARY = Indexes0.VIRTUALPROVIDERS_PRIMARY;
	public static final Index VIRTUALPROVIDERS_UUID_UNIQUE = Indexes0.VIRTUALPROVIDERS_UUID_UNIQUE;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Indexes0 extends AbstractKeys {
		public static Index AUTHENTICATION_ID_UNIQUE = createIndex("id_UNIQUE", Authentication.AUTHENTICATION, new OrderField[]{
				Authentication.AUTHENTICATION.ID}, true);
		public static Index AUTHENTICATION_PRIMARY = createIndex("PRIMARY", Authentication.AUTHENTICATION, new OrderField[]{
				Authentication.AUTHENTICATION.ID}, true);
		public static Index DEVICES_ID_UNIQUE = createIndex("id_UNIQUE", Devices.DEVICES, new OrderField[]{
				Devices.DEVICES.ID}, true);
		public static Index DEVICES_PRIMARY = createIndex("PRIMARY", Devices.DEVICES, new OrderField[]{
				Devices.DEVICES.ID}, true);
		public static Index DISTRIBUTORDESTINATIONS_ID_UNIQUE = createIndex("id_UNIQUE", Distributordestinations.DISTRIBUTORDESTINATIONS, new OrderField[]{
				Distributordestinations.DISTRIBUTORDESTINATIONS.ID}, true);
		public static Index DISTRIBUTORDESTINATIONS_PRIMARY = createIndex("PRIMARY", Distributordestinations.DISTRIBUTORDESTINATIONS, new OrderField[]{
				Distributordestinations.DISTRIBUTORDESTINATIONS.ID}, true);
		public static Index DISTRIBUTORDIRECTORIES_ID_UNIQUE = createIndex("id_UNIQUE", Distributordirectories.DISTRIBUTORDIRECTORIES, new OrderField[]{
				Distributordirectories.DISTRIBUTORDIRECTORIES.ID}, true);
		public static Index DISTRIBUTORDIRECTORIES_PRIMARY = createIndex("PRIMARY", Distributordirectories.DISTRIBUTORDIRECTORIES, new OrderField[]{
				Distributordirectories.DISTRIBUTORDIRECTORIES.ID}, true);
		public static Index DISTRIBUTORQUEUE_ID_UNIQUE = createIndex("id_UNIQUE", Distributorqueue.DISTRIBUTORQUEUE, new OrderField[]{
				Distributorqueue.DISTRIBUTORQUEUE.ID}, true);
		public static Index DISTRIBUTORQUEUE_PRIMARY = createIndex("PRIMARY", Distributorqueue.DISTRIBUTORQUEUE, new OrderField[]{
				Distributorqueue.DISTRIBUTORQUEUE.ID}, true);
		public static Index HOMEACCOUNTS_ID_UNIQUE = createIndex("id_UNIQUE", Homeaccounts.HOMEACCOUNTS, new OrderField[]{
				Homeaccounts.HOMEACCOUNTS.ID}, true);
		public static Index HOMEACCOUNTS_PRIMARY = createIndex("PRIMARY", Homeaccounts.HOMEACCOUNTS, new OrderField[]{
				Homeaccounts.HOMEACCOUNTS.ID}, true);
		public static Index HOMES_ID_UNIQUE = createIndex("id_UNIQUE", Homes.HOMES, new OrderField[]{
				Homes.HOMES.ID}, true);
		public static Index HOMES_PRIMARY = createIndex("PRIMARY", Homes.HOMES, new OrderField[]{Homes.HOMES.ID}, true);
		public static Index MEDIACHAPTERS_ID_UNIQUE = createIndex("Id_UNIQUE", Mediachapters.MEDIACHAPTERS, new OrderField[]{
				Mediachapters.MEDIACHAPTERS.ID}, true);
		public static Index MEDIACHAPTERS_PRIMARY = createIndex("PRIMARY", Mediachapters.MEDIACHAPTERS, new OrderField[]{
				Mediachapters.MEDIACHAPTERS.ID}, true);
		public static Index MEDIACHAPTERS_UUID_UNIQUE = createIndex("Uuid_UNIQUE", Mediachapters.MEDIACHAPTERS, new OrderField[]{
				Mediachapters.MEDIACHAPTERS.UUID}, true);
		public static Index MEDIAFILES_ID_UNIQUE = createIndex("Id_UNIQUE", Mediafiles.MEDIAFILES, new OrderField[]{
				Mediafiles.MEDIAFILES.ID}, true);
		public static Index MEDIAFILES_PRIMARY = createIndex("PRIMARY", Mediafiles.MEDIAFILES, new OrderField[]{
				Mediafiles.MEDIAFILES.ID}, true);
		public static Index MEDIAFILES_UUID_UNIQUE = createIndex("Uuid_UNIQUE", Mediafiles.MEDIAFILES, new OrderField[]{
				Mediafiles.MEDIAFILES.UUID}, true);
		public static Index MEDIASTREAMS_ID_UNIQUE = createIndex("Id_UNIQUE", Mediastreams.MEDIASTREAMS, new OrderField[]{
				Mediastreams.MEDIASTREAMS.ID}, true);
		public static Index MEDIASTREAMS_PRIMARY = createIndex("PRIMARY", Mediastreams.MEDIASTREAMS, new OrderField[]{
				Mediastreams.MEDIASTREAMS.ID}, true);
		public static Index MEDIASTREAMS_UUID_UNIQUE = createIndex("Uuid_UNIQUE", Mediastreams.MEDIASTREAMS, new OrderField[]{
				Mediastreams.MEDIASTREAMS.UUID}, true);
		public static Index PARTIES_ID_UNIQUE = createIndex("id_UNIQUE", Parties.PARTIES, new OrderField[]{
				Parties.PARTIES.ID}, true);
		public static Index PARTIES_PRIMARY = createIndex("PRIMARY", Parties.PARTIES, new OrderField[]{
				Parties.PARTIES.ID}, true);
		public static Index PARTYACCOUNTS_ID_UNIQUE = createIndex("id_UNIQUE", Partyaccounts.PARTYACCOUNTS, new OrderField[]{
				Partyaccounts.PARTYACCOUNTS.ID}, true);
		public static Index PARTYACCOUNTS_PRIMARY = createIndex("PRIMARY", Partyaccounts.PARTYACCOUNTS, new OrderField[]{
				Partyaccounts.PARTYACCOUNTS.ID}, true);
		public static Index PROVIDERFILERELATIONSHIPS_ID_UNIQUE = createIndex("Id_UNIQUE", Providerfilerelationships.PROVIDERFILERELATIONSHIPS, new OrderField[]{
				Providerfilerelationships.PROVIDERFILERELATIONSHIPS.ID}, true);
		public static Index PROVIDERFILERELATIONSHIPS_PRIMARY = createIndex("PRIMARY", Providerfilerelationships.PROVIDERFILERELATIONSHIPS, new OrderField[]{
				Providerfilerelationships.PROVIDERFILERELATIONSHIPS.ID}, true);
		public static Index PROVIDERFILERELATIONSHIPS_UUIDINDEX = createIndex("UUIDIndex", Providerfilerelationships.PROVIDERFILERELATIONSHIPS, new OrderField[]{
				Providerfilerelationships.PROVIDERFILERELATIONSHIPS.FILEUUID,
				Providerfilerelationships.PROVIDERFILERELATIONSHIPS.PARENTUUID}, false);
		public static Index PROVIDERFILES_FILEINDEX = createIndex("FileIndex", Providerfiles.PROVIDERFILES, new OrderField[]{
				Providerfiles.PROVIDERFILES.UUID, Providerfiles.PROVIDERFILES.PROVIDERUUID,
				Providerfiles.PROVIDERFILES.MIMETYPE, Providerfiles.PROVIDERFILES.STATUS,
				Providerfiles.PROVIDERFILES.PROVIDERFILEID, Providerfiles.PROVIDERFILES.TYPE,
				Providerfiles.PROVIDERFILES.DATECREATED, Providerfiles.PROVIDERFILES.DATEUPDATED,
				Providerfiles.PROVIDERFILES.SIZE, Providerfiles.PROVIDERFILES.HASH}, false);
		public static Index PROVIDERFILES_ID_UNIQUE = createIndex("id_UNIQUE", Providerfiles.PROVIDERFILES, new OrderField[]{
				Providerfiles.PROVIDERFILES.ID}, true);
		public static Index PROVIDERFILES_PRIMARY = createIndex("PRIMARY", Providerfiles.PROVIDERFILES, new OrderField[]{
				Providerfiles.PROVIDERFILES.ID}, true);
		public static Index PROVIDERFILES_UUID_UNIQUE = createIndex("Uuid_UNIQUE", Providerfiles.PROVIDERFILES, new OrderField[]{
				Providerfiles.PROVIDERFILES.UUID}, true);
		public static Index PROVIDERS_ID_UNIQUE = createIndex("Id_UNIQUE", Providers.PROVIDERS, new OrderField[]{
				Providers.PROVIDERS.ID}, true);
		public static Index PROVIDERS_PRIMARY = createIndex("PRIMARY", Providers.PROVIDERS, new OrderField[]{
				Providers.PROVIDERS.ID}, true);
		public static Index PROVIDERS_UUID_UNIQUE = createIndex("Uuid_UNIQUE", Providers.PROVIDERS, new OrderField[]{
				Providers.PROVIDERS.UUID}, true);
		public static Index PROVIDERSOLD_ID_UNIQUE = createIndex("id_UNIQUE", Providersold.PROVIDERSOLD, new OrderField[]{
				Providersold.PROVIDERSOLD.ID}, true);
		public static Index PROVIDERSOLD_PRIMARY = createIndex("PRIMARY", Providersold.PROVIDERSOLD, new OrderField[]{
				Providersold.PROVIDERSOLD.ID}, true);
		public static Index PROVIDERSOLD_UUID_UNIQUE = createIndex("uuid_UNIQUE", Providersold.PROVIDERSOLD, new OrderField[]{
				Providersold.PROVIDERSOLD.UUID}, true);
		public static Index SERVERS_ID_UNIQUE = createIndex("id_UNIQUE", Servers.SERVERS, new OrderField[]{
				Servers.SERVERS.ID}, true);
		public static Index SERVERS_PRIMARY = createIndex("PRIMARY", Servers.SERVERS, new OrderField[]{
				Servers.SERVERS.ID}, true);
		public static Index USERS_ID_UNIQUE = createIndex("id_UNIQUE", Users.USERS, new OrderField[]{
				Users.USERS.ID}, true);
		public static Index USERS_NAME_UNIQUE = createIndex("name_UNIQUE", Users.USERS, new OrderField[]{
				Users.USERS.NAME}, true);
		public static Index USERS_PRIMARY = createIndex("PRIMARY", Users.USERS, new OrderField[]{Users.USERS.ID}, true);
		public static Index VIRTUALPROVIDERPROVIDERS_ID_UNIQUE = createIndex("id_UNIQUE", Virtualproviderproviders.VIRTUALPROVIDERPROVIDERS, new OrderField[]{
				Virtualproviderproviders.VIRTUALPROVIDERPROVIDERS.ID}, true);
		public static Index VIRTUALPROVIDERPROVIDERS_PRIMARY = createIndex("PRIMARY", Virtualproviderproviders.VIRTUALPROVIDERPROVIDERS, new OrderField[]{
				Virtualproviderproviders.VIRTUALPROVIDERPROVIDERS.ID}, true);
		public static Index VIRTUALPROVIDERS_ID_UNIQUE = createIndex("id_UNIQUE", Virtualproviders.VIRTUALPROVIDERS, new OrderField[]{
				Virtualproviders.VIRTUALPROVIDERS.ID}, true);
		public static Index VIRTUALPROVIDERS_NAME_UNIQUE = createIndex("name_UNIQUE", Virtualproviders.VIRTUALPROVIDERS, new OrderField[]{
				Virtualproviders.VIRTUALPROVIDERS.NAME}, true);
		public static Index VIRTUALPROVIDERS_PRIMARY = createIndex("PRIMARY", Virtualproviders.VIRTUALPROVIDERS, new OrderField[]{
				Virtualproviders.VIRTUALPROVIDERS.ID}, true);
		public static Index VIRTUALPROVIDERS_UUID_UNIQUE = createIndex("uuid_UNIQUE", Virtualproviders.VIRTUALPROVIDERS, new OrderField[]{
				Virtualproviders.VIRTUALPROVIDERS.UUID}, true);
	}
}
