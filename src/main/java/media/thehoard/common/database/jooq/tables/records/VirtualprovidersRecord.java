/*
 * This file is generated by jOOQ.
*/
package media.thehoard.common.database.jooq.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * Virtual provider structures used for spanning across multiple providers
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VirtualprovidersRecord extends UpdatableRecordImpl<VirtualprovidersRecord> implements Record6<Integer, String, String, Timestamp, Timestamp, Timestamp> {

    private static final long serialVersionUID = -835645516;

    /**
     * Setter for <code>thehoard.VirtualProviders.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>thehoard.VirtualProviders.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>thehoard.VirtualProviders.uuid</code>.
     */
    public void setUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>thehoard.VirtualProviders.uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>thehoard.VirtualProviders.name</code>. This is required to be unique, because you will have a bad time otherwise.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>thehoard.VirtualProviders.name</code>. This is required to be unique, because you will have a bad time otherwise.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>thehoard.VirtualProviders.dateCreated</code>.
     */
    public void setDatecreated(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>thehoard.VirtualProviders.dateCreated</code>.
     */
    public Timestamp getDatecreated() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>thehoard.VirtualProviders.dateUpdated</code>.
     */
    public void setDateupdated(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>thehoard.VirtualProviders.dateUpdated</code>.
     */
    public Timestamp getDateupdated() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>thehoard.VirtualProviders.dateDeleted</code>.
     */
    public void setDatedeleted(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>thehoard.VirtualProviders.dateDeleted</code>.
     */
    public Timestamp getDatedeleted() {
        return (Timestamp) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, String, Timestamp, Timestamp, Timestamp> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, String, Timestamp, Timestamp, Timestamp> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Virtualproviders.VIRTUALPROVIDERS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Virtualproviders.VIRTUALPROVIDERS.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Virtualproviders.VIRTUALPROVIDERS.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return Virtualproviders.VIRTUALPROVIDERS.DATECREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return Virtualproviders.VIRTUALPROVIDERS.DATEUPDATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return Virtualproviders.VIRTUALPROVIDERS.DATEDELETED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component4() {
        return getDatecreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component5() {
        return getDateupdated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component6() {
        return getDatedeleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getDatecreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getDateupdated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getDatedeleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord value2(String value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord value4(Timestamp value) {
        setDatecreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord value5(Timestamp value) {
        setDateupdated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord value6(Timestamp value) {
        setDatedeleted(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualprovidersRecord values(Integer value1, String value2, String value3, Timestamp value4, Timestamp value5, Timestamp value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached VirtualprovidersRecord
     */
    public VirtualprovidersRecord() {
        super(Virtualproviders.VIRTUALPROVIDERS);
    }

    /**
     * Create a detached, initialised VirtualprovidersRecord
     */
    public VirtualprovidersRecord(Integer id, String uuid, String name, Timestamp datecreated, Timestamp dateupdated, Timestamp datedeleted) {
        super(Virtualproviders.VIRTUALPROVIDERS);

        set(0, id);
        set(1, uuid);
        set(2, name);
        set(3, datecreated);
        set(4, dateupdated);
        set(5, datedeleted);
    }
}