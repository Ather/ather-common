/*
 * This file is generated by jOOQ.
*/
package media.thehoard.common.database.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import media.thehoard.common.database.jooq.Indexes;
import media.thehoard.common.database.jooq.Keys;
import media.thehoard.common.database.jooq.Thehoard;
import media.thehoard.common.database.jooq.tables.records.ProviderfilerelationshipsRecord;

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
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Providerfilerelationships extends TableImpl<ProviderfilerelationshipsRecord> {

    private static final long serialVersionUID = 1454574435;

    /**
     * The reference instance of <code>thehoard.ProviderFileRelationships</code>
     */
    public static final Providerfilerelationships PROVIDERFILERELATIONSHIPS = new Providerfilerelationships();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProviderfilerelationshipsRecord> getRecordType() {
        return ProviderfilerelationshipsRecord.class;
    }

    /**
     * The column <code>thehoard.ProviderFileRelationships.Id</code>.
     */
    public final TableField<ProviderfilerelationshipsRecord, Integer> ID = createField("Id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>thehoard.ProviderFileRelationships.FileUuid</code>.
     */
    public final TableField<ProviderfilerelationshipsRecord, String> FILEUUID = createField("FileUuid", org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>thehoard.ProviderFileRelationships.ParentUuid</code>.
     */
    public final TableField<ProviderfilerelationshipsRecord, String> PARENTUUID = createField("ParentUuid", org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * Create a <code>thehoard.ProviderFileRelationships</code> table reference
     */
    public Providerfilerelationships() {
        this(DSL.name("ProviderFileRelationships"), null);
    }

    /**
     * Create an aliased <code>thehoard.ProviderFileRelationships</code> table reference
     */
    public Providerfilerelationships(String alias) {
        this(DSL.name(alias), PROVIDERFILERELATIONSHIPS);
    }

    /**
     * Create an aliased <code>thehoard.ProviderFileRelationships</code> table reference
     */
    public Providerfilerelationships(Name alias) {
        this(alias, PROVIDERFILERELATIONSHIPS);
    }

    private Providerfilerelationships(Name alias, Table<ProviderfilerelationshipsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Providerfilerelationships(Name alias, Table<ProviderfilerelationshipsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
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
        return Arrays.<Index>asList(Indexes.PROVIDERFILERELATIONSHIPS_ID_UNIQUE, Indexes.PROVIDERFILERELATIONSHIPS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<ProviderfilerelationshipsRecord, Integer> getIdentity() {
        return Keys.IDENTITY_PROVIDERFILERELATIONSHIPS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProviderfilerelationshipsRecord> getPrimaryKey() {
        return Keys.KEY_PROVIDERFILERELATIONSHIPS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProviderfilerelationshipsRecord>> getKeys() {
        return Arrays.<UniqueKey<ProviderfilerelationshipsRecord>>asList(Keys.KEY_PROVIDERFILERELATIONSHIPS_PRIMARY, Keys.KEY_PROVIDERFILERELATIONSHIPS_ID_UNIQUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Providerfilerelationships as(String alias) {
        return new Providerfilerelationships(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Providerfilerelationships as(Name alias) {
        return new Providerfilerelationships(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Providerfilerelationships rename(String name) {
        return new Providerfilerelationships(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Providerfilerelationships rename(Name name) {
        return new Providerfilerelationships(name, null);
    }
}