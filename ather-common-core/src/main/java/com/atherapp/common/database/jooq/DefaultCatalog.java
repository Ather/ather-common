/*
 * This file is generated by jOOQ.
 */
package com.atherapp.common.database.jooq;


import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(value = {"http://www.jooq.org", "jOOQ version:3.10.1"}, comments = "This class is generated by jOOQ")
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class DefaultCatalog extends CatalogImpl {

	/**
	 * The reference instance of <code></code>
	 */
	public static final DefaultCatalog DEFAULT_CATALOG = new DefaultCatalog();
	private static final long serialVersionUID = -1275162444;
	/**
	 * The schema <code>thehoard</code>.
	 */
	public final Thehoard THEHOARD = com.atherapp.common.database.jooq.Thehoard.THEHOARD;

	/**
	 * No further instances allowed
	 */
	private DefaultCatalog() {
		super("");
	}

	@Override
	public final List<Schema> getSchemas() {
		List result = new ArrayList();
		result.addAll(getSchemas0());
		return result;
	}

	private final List<Schema> getSchemas0() {
		return Arrays.<Schema>asList(Thehoard.THEHOARD);
	}
}
