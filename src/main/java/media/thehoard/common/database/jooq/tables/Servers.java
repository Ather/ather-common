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
import media.thehoard.common.database.jooq.tables.records.ServersRecord;

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
public class Servers extends TableImpl<ServersRecord> {

    private static final long serialVersionUID = 1817478185;

    /**
     * The reference instance of <code>thehoard.servers</code>
     */
    public static final Servers SERVERS = new Servers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ServersRecord> getRecordType() {
        return ServersRecord.class;
    }

    /**
     * The column <code>thehoard.servers.id</code>.
     */
    public final TableField<ServersRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>thehoard.servers.uuid</code>.
     */
    public final TableField<ServersRecord, String> UUID = createField("uuid", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>thehoard.servers.ip</code>.
     */
    public final TableField<ServersRecord, String> IP = createField("ip", org.jooq.impl.SQLDataType.VARCHAR(15), this, "");

    /**
     * The column <code>thehoard.servers.serverType</code>.
     */
    public final TableField<ServersRecord, Integer> SERVERTYPE = createField("serverType", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>thehoard.servers</code> table reference
     */
    public Servers() {
        this(DSL.name("servers"), null);
    }

    /**
     * Create an aliased <code>thehoard.servers</code> table reference
     */
    public Servers(String alias) {
        this(DSL.name(alias), SERVERS);
    }

    /**
     * Create an aliased <code>thehoard.servers</code> table reference
     */
    public Servers(Name alias) {
        this(alias, SERVERS);
    }

    private Servers(Name alias, Table<ServersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Servers(Name alias, Table<ServersRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.SERVERS_ID_UNIQUE, Indexes.SERVERS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<ServersRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SERVERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ServersRecord> getPrimaryKey() {
        return Keys.KEY_SERVERS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ServersRecord>> getKeys() {
        return Arrays.<UniqueKey<ServersRecord>>asList(Keys.KEY_SERVERS_PRIMARY, Keys.KEY_SERVERS_ID_UNIQUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Servers as(String alias) {
        return new Servers(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Servers as(Name alias) {
        return new Servers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Servers rename(String name) {
        return new Servers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Servers rename(Name name) {
        return new Servers(name, null);
    }
}