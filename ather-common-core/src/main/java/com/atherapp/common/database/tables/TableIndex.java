/**
 * 
 */
package com.atherapp.common.database.tables;

/**
 * @author Michael Haas
 *
 */
@Deprecated
public class TableIndex {
	public static class Accounts {
		public static final String TABLE_NAME = "Accounts";

		public static final String ID = "Id";
		public static final String NAME = "Name";
		public static final String EMAIL = "Email";
		public static final String PASSWORD_HASH = "PasswordHash";
		public static final String PASSWORD_SALT = "PasswordSalt";
		public static final String DATE_CREATED = "DateCreated";
		public static final String DATE_UPDATED = "DateUpdated";
	}

	public static class Authentication {
		public static final String TABLE_NAME = "Authentication";

		public static final String ID = "Id";
		public static final String ACCESS_TOKEN = "AccessToken";
		public static final String DEVICE_ID = "DeviceId";
		public static final String ACCOUNT_ID = "AccountId";
		public static final String IS_ACTIVE = "IsActive";
		public static final String DATE_CREATED = "DateCreated";
		public static final String DATE_REVOKED = "DateRevoked";
	}

	public static class HomeAccounts {
		public static final String TABLE_NAME = "HomeAccounts";

		public static final String ID = "id";
		public static final String ACCOUNT_ID = "accountId";
		public static final String HOME_ID = "homeId";
		public static final String PIN = "pin";
		public static final String DATE_JOINED = "dateJoined";
		public static final String DATE_LEFT = "dateLeft";
	}

	public static class Homes {
		public static final String TABLE_NAME = "Homes";

		public static final String ID = "id";
		public static final String OWNER_ID = "ownerId";
		public static final String NAME = "name";
		public static final String DATE_CREATED = "dateCreated";
		public static final String DATE_DELETED = "dateDeleted";
	}

	public static class MediaStreams {
		public static final String TABLE_NAME = "MediaStreams";

		public static final String INDEX = "Index";
		public static final String CODEC_NAME = "CodecName";
		public static final String PROFILE = "Profile";
		public static final String CODEC_TYPE = "CodecType";
		public static final String CODEC_TIME_BASE = "CodecTimeBase";
		public static final String WIDTH = "Width";
		public static final String HEIGHT = "Height";
		public static final String ASPECT_RATIO = "AspectRatio";
		public static final String PIXEL_FORMAT = "PixelFormat";
		public static final String BIT_DEPTH = "BitDepth";
		public static final String LEVEL = "Level";
		public static final String REFERENCE_FRAMES = "ReferenceFrames";
		public static final String AVERAGE_FRAME_RATE = "AverageFrameRate";
		public static final String TIME_BASE = "TimeBase";
		public static final String BIT_RATE = "BitRate";
		public static final String IS_DEFAULT = "IsDefault";
		public static final String IS_FORCED = "IsForced";
		public static final String IS_EXTERNAL = "IsExternal";
		public static final String LANGUAGE = "Language";
		public static final String TITLE = "Title";
		public static final String DURATION = "Duration";
		public static final String SAMPLE_RATE = "SampleRate";
		public static final String CHANNELS = "Channels";
		public static final String CHANNEL_LAYOUT = "ChannelLayout";
	}

	public static class Parties {
		public static final String TABLE_NAME = "Parties";

		public static final String ID = "id";
		public static final String OWNER_ID = "ownerId";
		public static final String NAME = "name";
		public static final String DATE_CREATED = "dateCreated";
		public static final String DATE_UPDATED = "dateUpdated";
	}

	public static class PartyAccounts {
		public static final String TABLE_NAME = "PartyAccounts";

		public static final String ID = "id";
		public static final String ACCOUNT_ID = "accountId";
		public static final String PARTY_ID = "partyId";
		public static final String IS_ACTIVE = "isActive";
		public static final String DATE_JOINED = "dateJoined";
		public static final String DATE_UPDATED = "dateUpdated";
		public static final String DATE_LEFT = "dateLeft";
	}

	public static class ProviderFiles {
		public static final String TABLE_NAME = "ProviderFiles";

		public static final String ID = "Id";
		public static final String UUID = "Uuid";
		public static final String PROVIDER_ID = "ProviderId";
		public static final String NAME = "Name";
		public static final String DESCRIPTION = "Description";
		public static final String STATUS = "Status";
		public static final String PROVIDER_FILE_ID = "ProviderFileId";
		public static final String VIRTUAL_PATH = "VirtualPath";
		public static final String PARENT_ID = "ParentId";
		public static final String TYPE = "Type";
		public static final String DATE_CREATED = "DateCreated";
		public static final String DATE_UPDATED = "DateUpdated";
		public static final String SIZE = "Size";
		public static final String HASH = "Hash";
	}

	@Deprecated
	public static class providers {
		public static final String TABLE_NAME = "providers";

		public static final String ID = "id";
		public static final String UUID = "uuid";
		public static final String PROVIDER_TYPE = "providerType";
		public static final String PROVIDER_NAME = "providerName";
		public static final String USER_EMAIL = "userEmail";
		public static final String USER_ID = "userId";
		public static final String DATE_CREATED = "dateCreated";
		public static final String DATE_UPDATED = "dateUpdated";
		public static final String LAST_ATTEMPT = "lastAttempt";
		public static final String CURRENT_BACKOFF = "currentBackoff";
		public static final String STATUS = "status";
		public static final String CREDENTIAL = "credential";
		public static final String CHANGE_TOKEN = "changeToken";
	}

	public static class ProvidersReImpl {
		public static final String TABLE_NAME = "ProvidersReImpl";

		public static final String ID = "Id";
		public static final String UUID = "Uuid";
		public static final String ALIAS = "Alias";
		public static final String PROVIDER_ALIAS = "ProviderAlias";
		public static final String TYPE_ID = "TypeId";
		public static final String USAGE_ID = "UsageId";
		public static final String STATUS = "Status";
		public static final String DATE_CREATED = "DateCreated";
		public static final String DATE_UPDATED = "DateUpdated";
		public static final String CONFIGURATION = "Configuration";
		public static final String CREDENTIAL = "Credential";
		public static final String CHANGE_TOKEN = "ChangeToken";
	}

	public static class VirtualProviderProviders {
		public static final String TABLE_NAME = "VirtualProviderProviders";

		public static final String ID = "id";
		public static final String PROVIDER_ID = "providerId";
		public static final String PROVIDER_ROOT_VIRTUAL_PATH = "providerRootVirtualPath";
		public static final String PROVIDER_ROOT_ID = "providerRootId";
		public static final String VIRTUAL_PROVIDER_ID = "virtualProviderId";
		public static final String DATE_CREATED = "dateCreated";
		public static final String DATE_UPDATED = "dateUpdated";
		public static final String DATE_DELETED = "dateDeleted";
	}

	public static class VirtualProviders {
		public static final String TABLE_NAME = "VirtualProviders";

		public static final String ID = "id";
		public static final String UUID = "uuid";
		public static final String NAME = "name";
		public static final String DATE_CREATED = "dateCreated";
		public static final String DATE_UPDATED = "dateUpdated";
		public static final String DATE_DELETED = "dateDeleted";
	}
}
