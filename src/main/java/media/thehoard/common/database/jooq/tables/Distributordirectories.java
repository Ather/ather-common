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
import media.thehoard.common.database.jooq.tables.records.DistributordirectoriesRecord;

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
 * This table stores all the directories for each destination, to prevent 
 * constant API hits to parse destinations.
 * This is structured differently than the regular files/directories tables 
 * because it doesn't use foreign keys.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Distributordirectories extends TableImpl<DistributordirectoriesRecord> {

    private static final long serialVersionUID = 1475301559;

    /**
     * The reference instance of <code>thehoard.distributorDirectories</code>
     */
    public static final Distributordirectories DISTRIBUTORDIRECTORIES = new Distributordirectories();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DistributordirectoriesRecord> getRecordType() {
        return DistributordirectoriesRecord.class;
    }

    /**
     * The column <code>thehoard.distributorDirectories.id</code>.
     */
    public final TableField<DistributordirectoriesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>thehoard.distributorDirectories.parentDir</code>.
     */
    public final TableField<DistributordirectoriesRecord, Integer> PARENTDIR = createField("parentDir", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>thehoard.distributorDirectories.remoteId</code>.
     */
    public final TableField<DistributordirectoriesRecord, String> REMOTEID = createField("remoteId", org.jooq.impl.SQLDataType.VARCHAR(45).nullable(false), this, "");

    /**
     * The column <code>thehoard.distributorDirectories.title</code>.
     */
    public final TableField<DistributordirectoriesRecord, String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>thehoard.distributorDirectories.path</code>.
     */
    public final TableField<DistributordirectoriesRecord, String> PATH = createField("path", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>thehoard.distributorDirectories.dateCreated</code>.
     */
    public final TableField<DistributordirectoriesRecord, Timestamp> DATECREATED = createField("dateCreated", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>thehoard.distributorDirectories.dateUpdated</code>.
     */
    public final TableField<DistributordirectoriesRecord, Timestamp> DATEUPDATED = createField("dateUpdated", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>thehoard.distributorDirectories.dateDeleted</code>.
     */
    public final TableField<DistributordirectoriesRecord, Timestamp> DATEDELETED = createField("dateDeleted", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * Create a <code>thehoard.distributorDirectories</code> table reference
     */
    public Distributordirectories() {
        this(DSL.name("distributorDirectories"), null);
    }

    /**
     * Create an aliased <code>thehoard.distributorDirectories</code> table reference
     */
    public Distributordirectories(String alias) {
        this(DSL.name(alias), DISTRIBUTORDIRECTORIES);
    }

    /**
     * Create an aliased <code>thehoard.distributorDirectories</code> table reference
     */
    public Distributordirectories(Name alias) {
        this(alias, DISTRIBUTORDIRECTORIES);
    }

    private Distributordirectories(Name alias, Table<DistributordirectoriesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Distributordirectories(Name alias, Table<DistributordirectoriesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "This table stores all the directories for each destination, to prevent constant API hits to parse destinations.\nThis is structured differently than the regular files/directories tables because it doesn't use foreign keys.");
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
        return Arrays.<Index>asList(Indexes.DISTRIBUTORDIRECTORIES_ID_UNIQUE, Indexes.DISTRIBUTORDIRECTORIES_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<DistributordirectoriesRecord, Integer> getIdentity() {
        return Keys.IDENTITY_DISTRIBUTORDIRECTORIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<DistributordirectoriesRecord> getPrimaryKey() {
        return Keys.KEY_DISTRIBUTORDIRECTORIES_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DistributordirectoriesRecord>> getKeys() {
        return Arrays.<UniqueKey<DistributordirectoriesRecord>>asList(Keys.KEY_DISTRIBUTORDIRECTORIES_PRIMARY, Keys.KEY_DISTRIBUTORDIRECTORIES_ID_UNIQUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Distributordirectories as(String alias) {
        return new Distributordirectories(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Distributordirectories as(Name alias) {
        return new Distributordirectories(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Distributordirectories rename(String name) {
        return new Distributordirectories(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Distributordirectories rename(Name name) {
        return new Distributordirectories(name, null);
    }
}