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
import media.thehoard.common.database.jooq.tables.records.AccountsRecord;

import org.jooq.Field;
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
public class Accounts extends TableImpl<AccountsRecord> {

    private static final long serialVersionUID = 624329854;

    /**
     * The reference instance of <code>thehoard.Accounts</code>
     */
    public static final Accounts ACCOUNTS = new Accounts();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AccountsRecord> getRecordType() {
        return AccountsRecord.class;
    }

    /**
     * The column <code>thehoard.Accounts.Id</code>.
     */
    public final TableField<AccountsRecord, Integer> ID = createField("Id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>thehoard.Accounts.Name</code>.
     */
    public final TableField<AccountsRecord, String> NAME = createField("Name", org.jooq.impl.SQLDataType.VARCHAR(30), this, "");

    /**
     * The column <code>thehoard.Accounts.Email</code>.
     */
    public final TableField<AccountsRecord, String> EMAIL = createField("Email", org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>thehoard.Accounts.PasswordHash</code>.
     */
    public final TableField<AccountsRecord, String> PASSWORDHASH = createField("PasswordHash", org.jooq.impl.SQLDataType.CHAR(64), this, "");

    /**
     * The column <code>thehoard.Accounts.PasswordSalt</code>.
     */
    public final TableField<AccountsRecord, String> PASSWORDSALT = createField("PasswordSalt", org.jooq.impl.SQLDataType.CHAR(64), this, "");

    /**
     * The column <code>thehoard.Accounts.DateCreated</code>.
     */
    public final TableField<AccountsRecord, Timestamp> DATECREATED = createField("DateCreated", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * The column <code>thehoard.Accounts.DateUpdated</code>.
     */
    public final TableField<AccountsRecord, Timestamp> DATEUPDATED = createField("DateUpdated", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * Create a <code>thehoard.Accounts</code> table reference
     */
    public Accounts() {
        this(DSL.name("Accounts"), null);
    }

    /**
     * Create an aliased <code>thehoard.Accounts</code> table reference
     */
    public Accounts(String alias) {
        this(DSL.name(alias), ACCOUNTS);
    }

    /**
     * Create an aliased <code>thehoard.Accounts</code> table reference
     */
    public Accounts(Name alias) {
        this(alias, ACCOUNTS);
    }

    private Accounts(Name alias, Table<AccountsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Accounts(Name alias, Table<AccountsRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.ACCOUNTS_ID_UNIQUE, Indexes.ACCOUNTS_NAME_UNIQUE, Indexes.ACCOUNTS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AccountsRecord> getPrimaryKey() {
        return Keys.KEY_ACCOUNTS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AccountsRecord>> getKeys() {
        return Arrays.<UniqueKey<AccountsRecord>>asList(Keys.KEY_ACCOUNTS_PRIMARY, Keys.KEY_ACCOUNTS_ID_UNIQUE, Keys.KEY_ACCOUNTS_NAME_UNIQUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Accounts as(String alias) {
        return new Accounts(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Accounts as(Name alias) {
        return new Accounts(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Accounts rename(String name) {
        return new Accounts(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Accounts rename(Name name) {
        return new Accounts(name, null);
    }
}