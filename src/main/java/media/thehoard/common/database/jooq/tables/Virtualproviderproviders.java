/*
 * This file is generated by jOOQ.
*/
package media.thehoard.common.database.jooq.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import media.thehoard.common.database.jooq.Indexes;
import media.thehoard.common.database.jooq.Keys;
import media.thehoard.common.database.jooq.Thehoard;
import media.thehoard.common.database.jooq.tables.records.VirtualproviderprovidersRecord;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * Provider references for virtual providers
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Virtualproviderproviders extends TableImpl<VirtualproviderprovidersRecord> {

    private static final long serialVersionUID = -365568343;

    /**
     * The reference instance of <code>thehoard.VirtualProviderProviders</code>
     */
    public static final Virtualproviderproviders VIRTUALPROVIDERPROVIDERS = new Virtualproviderproviders();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<VirtualproviderprovidersRecord> getRecordType() {
        return VirtualproviderprovidersRecord.class;
    }

    /**
     * The column <code>thehoard.VirtualProviderProviders.id</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.providerId</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, Integer> PROVIDERID = createField("providerId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.providerRootVirtualPath</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, String> PROVIDERROOTVIRTUALPATH = createField("providerRootVirtualPath", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.providerRootId</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, String> PROVIDERROOTID = createField("providerRootId", org.jooq.impl.SQLDataType.VARCHAR(45), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.virtualProviderId</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, Integer> VIRTUALPROVIDERID = createField("virtualProviderId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.dateCreated</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, Timestamp> DATECREATED = createField("dateCreated", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.dateUpdated</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, Timestamp> DATEUPDATED = createField("dateUpdated", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>thehoard.VirtualProviderProviders.dateDeleted</code>.
     */
    public final TableField<VirtualproviderprovidersRecord, Timestamp> DATEDELETED = createField("dateDeleted", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>thehoard.VirtualProviderProviders</code> table reference
     */
    public Virtualproviderproviders() {
        this(DSL.name("VirtualProviderProviders"), null);
    }

    /**
     * Create an aliased <code>thehoard.VirtualProviderProviders</code> table reference
     */
    public Virtualproviderproviders(String alias) {
        this(DSL.name(alias), VIRTUALPROVIDERPROVIDERS);
    }

    /**
     * Create an aliased <code>thehoard.VirtualProviderProviders</code> table reference
     */
    public Virtualproviderproviders(Name alias) {
        this(alias, VIRTUALPROVIDERPROVIDERS);
    }

    private Virtualproviderproviders(Name alias, Table<VirtualproviderprovidersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Virtualproviderproviders(Name alias, Table<VirtualproviderprovidersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "Provider references for virtual providers");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Thehoard.THEHOARD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.VIRTUALPROVIDERPROVIDERS_ID_UNIQUE, Indexes.VIRTUALPROVIDERPROVIDERS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<VirtualproviderprovidersRecord, Integer> getIdentity() {
        return Keys.IDENTITY_VIRTUALPROVIDERPROVIDERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<VirtualproviderprovidersRecord> getPrimaryKey() {
        return Keys.KEY_VIRTUALPROVIDERPROVIDERS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<VirtualproviderprovidersRecord>> getKeys() {
        return Arrays.<UniqueKey<VirtualproviderprovidersRecord>>asList(Keys.KEY_VIRTUALPROVIDERPROVIDERS_PRIMARY, Keys.KEY_VIRTUALPROVIDERPROVIDERS_ID_UNIQUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Virtualproviderproviders as(String alias) {
        return new Virtualproviderproviders(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Virtualproviderproviders as(Name alias) {
        return new Virtualproviderproviders(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Virtualproviderproviders rename(String name) {
        return new Virtualproviderproviders(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Virtualproviderproviders rename(Name name) {
        return new Virtualproviderproviders(name, null);
    }
}