/*
 * This file is generated by jOOQ.
 */
package com.atherapp.common.database.jooq.tables;


import com.atherapp.common.database.jooq.Indexes;
import com.atherapp.common.database.jooq.Keys;
import com.atherapp.common.database.jooq.Thehoard;
import com.atherapp.common.database.jooq.tables.records.HomeaccountsRecord;
import com.atherapp.common.database.jooq.tables.records.HomeaccountsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(value = {"http://www.jooq.org", "jOOQ version:3.10.1"}, comments = "This class is generated by jOOQ")
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Homeaccounts extends TableImpl<HomeaccountsRecord> {

	/**
	 * The reference instance of <code>thehoard.HomeAccounts</code>
	 */
	public static final Homeaccounts HOMEACCOUNTS = new Homeaccounts();
	private static final long serialVersionUID = 227104271;
	/**
	 * The column <code>thehoard.HomeAccounts.Id</code>.
	 */
	public final TableField<HomeaccountsRecord, Integer> ID = createField("Id", org.jooq.impl.SQLDataType.INTEGER
			.nullable(false), this, "");
	/**
	 * The column <code>thehoard.HomeAccounts.UserId</code>.
	 */
	public final TableField<HomeaccountsRecord, Integer> USERID = createField("UserId", org.jooq.impl.SQLDataType.INTEGER, this, "");
	/**
	 * The column <code>thehoard.HomeAccounts.HomeId</code>.
	 */
	public final TableField<HomeaccountsRecord, Integer> HOMEID = createField("HomeId", org.jooq.impl.SQLDataType.INTEGER, this, "");
	/**
	 * The column <code>thehoard.HomeAccounts.Pin</code>.
	 */
	public final TableField<HomeaccountsRecord, Integer> PIN = createField("Pin", org.jooq.impl.SQLDataType.INTEGER, this, "");
	/**
	 * The column <code>thehoard.HomeAccounts.DateCreated</code>.
	 */
	public final TableField<HomeaccountsRecord, Timestamp> DATECREATED = createField("DateCreated", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");
	/**
	 * The column <code>thehoard.HomeAccounts.DateUpdated</code>.
	 */
	public final TableField<HomeaccountsRecord, Timestamp> DATEUPDATED = createField("DateUpdated", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

	/**
	 * Create a <code>thehoard.HomeAccounts</code> table reference
	 */
	public Homeaccounts() {
		this(DSL.name("HomeAccounts"), null);
	}

	/**
	 * Create an aliased <code>thehoard.HomeAccounts</code> table reference
	 */
	public Homeaccounts(String alias) {
		this(DSL.name(alias), HOMEACCOUNTS);
	}

	/**
	 * Create an aliased <code>thehoard.HomeAccounts</code> table reference
	 */
	public Homeaccounts(Name alias) {
		this(alias, HOMEACCOUNTS);
	}

	private Homeaccounts(Name alias, Table<HomeaccountsRecord> aliased) {
		this(alias, aliased, null);
	}

	private Homeaccounts(Name alias, Table<HomeaccountsRecord> aliased, Field<?>[] parameters) {
		super(alias, null, aliased, parameters, "");
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<HomeaccountsRecord> getRecordType() {
		return HomeaccountsRecord.class;
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
		return Arrays.<Index>asList(Indexes.HOMEACCOUNTS_ID_UNIQUE, Indexes.HOMEACCOUNTS_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<HomeaccountsRecord> getPrimaryKey() {
		return Keys.KEY_HOMEACCOUNTS_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<HomeaccountsRecord>> getKeys() {
		return Arrays.<UniqueKey<HomeaccountsRecord>>asList(Keys.KEY_HOMEACCOUNTS_PRIMARY, Keys.KEY_HOMEACCOUNTS_ID_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Homeaccounts as(String alias) {
		return new Homeaccounts(DSL.name(alias), this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Homeaccounts as(Name alias) {
		return new Homeaccounts(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Homeaccounts rename(String name) {
		return new Homeaccounts(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Homeaccounts rename(Name name) {
		return new Homeaccounts(name, null);
	}
}
