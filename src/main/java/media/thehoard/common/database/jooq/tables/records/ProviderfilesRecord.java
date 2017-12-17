/*
 * This file is generated by jOOQ.
*/
package media.thehoard.common.database.jooq.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


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
public class ProviderfilesRecord extends UpdatableRecordImpl<ProviderfilesRecord> implements Record13<Integer, String, String, String, String, String, Byte, String, Byte, Timestamp, Timestamp, Long, String> {

    private static final long serialVersionUID = 377625896;

    /**
     * Setter for <code>thehoard.ProviderFiles.Id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Uuid</code>.
     */
    public void setUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.ProviderUuid</code>.
     */
    public void setProvideruuid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.ProviderUuid</code>.
     */
    public String getProvideruuid() {
        return (String) get(2);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Name</code>.
     */
    public void setName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Name</code>.
     */
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.MimeType</code>.
     */
    public void setMimetype(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.MimeType</code>.
     */
    public String getMimetype() {
        return (String) get(4);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Description</code>.
     */
    public void setDescription(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Description</code>.
     */
    public String getDescription() {
        return (String) get(5);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Status</code>.
     */
    public void setStatus(Byte value) {
        set(6, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Status</code>.
     */
    public Byte getStatus() {
        return (Byte) get(6);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.ProviderFileId</code>.
     */
    public void setProviderfileid(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.ProviderFileId</code>.
     */
    public String getProviderfileid() {
        return (String) get(7);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Type</code>.
     */
    public void setType(Byte value) {
        set(8, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Type</code>.
     */
    public Byte getType() {
        return (Byte) get(8);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.DateCreated</code>.
     */
    public void setDatecreated(Timestamp value) {
        set(9, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.DateCreated</code>.
     */
    public Timestamp getDatecreated() {
        return (Timestamp) get(9);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.DateUpdated</code>.
     */
    public void setDateupdated(Timestamp value) {
        set(10, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.DateUpdated</code>.
     */
    public Timestamp getDateupdated() {
        return (Timestamp) get(10);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Size</code>.
     */
    public void setSize(Long value) {
        set(11, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Size</code>.
     */
    public Long getSize() {
        return (Long) get(11);
    }

    /**
     * Setter for <code>thehoard.ProviderFiles.Hash</code>.
     */
    public void setHash(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>thehoard.ProviderFiles.Hash</code>.
     */
    public String getHash() {
        return (String) get(12);
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
    // Record13 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Integer, String, String, String, String, String, Byte, String, Byte, Timestamp, Timestamp, Long, String> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Integer, String, String, String, String, String, Byte, String, Byte, Timestamp, Timestamp, Long, String> valuesRow() {
        return (Row13) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Providerfiles.PROVIDERFILES.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Providerfiles.PROVIDERFILES.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Providerfiles.PROVIDERFILES.PROVIDERUUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Providerfiles.PROVIDERFILES.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Providerfiles.PROVIDERFILES.MIMETYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Providerfiles.PROVIDERFILES.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field7() {
        return Providerfiles.PROVIDERFILES.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Providerfiles.PROVIDERFILES.PROVIDERFILEID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field9() {
        return Providerfiles.PROVIDERFILES.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field10() {
        return Providerfiles.PROVIDERFILES.DATECREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field11() {
        return Providerfiles.PROVIDERFILES.DATEUPDATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field12() {
        return Providerfiles.PROVIDERFILES.SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return Providerfiles.PROVIDERFILES.HASH;
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
        return getProvideruuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getMimetype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component7() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getProviderfileid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component9() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component10() {
        return getDatecreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component11() {
        return getDateupdated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component12() {
        return getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component13() {
        return getHash();
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
        return getProvideruuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getMimetype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value7() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getProviderfileid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value9() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value10() {
        return getDatecreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value11() {
        return getDateupdated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value12() {
        return getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value13() {
        return getHash();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value2(String value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value3(String value) {
        setProvideruuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value4(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value5(String value) {
        setMimetype(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value6(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value7(Byte value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value8(String value) {
        setProviderfileid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value9(Byte value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value10(Timestamp value) {
        setDatecreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value11(Timestamp value) {
        setDateupdated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value12(Long value) {
        setSize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord value13(String value) {
        setHash(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderfilesRecord values(Integer value1, String value2, String value3, String value4, String value5, String value6, Byte value7, String value8, Byte value9, Timestamp value10, Timestamp value11, Long value12, String value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProviderfilesRecord
     */
    public ProviderfilesRecord() {
        super(Providerfiles.PROVIDERFILES);
    }

    /**
     * Create a detached, initialised ProviderfilesRecord
     */
    public ProviderfilesRecord(Integer id, String uuid, String provideruuid, String name, String mimetype, String description, Byte status, String providerfileid, Byte type, Timestamp datecreated, Timestamp dateupdated, Long size, String hash) {
        super(Providerfiles.PROVIDERFILES);

        set(0, id);
        set(1, uuid);
        set(2, provideruuid);
        set(3, name);
        set(4, mimetype);
        set(5, description);
        set(6, status);
        set(7, providerfileid);
        set(8, type);
        set(9, datecreated);
        set(10, dateupdated);
        set(11, size);
        set(12, hash);
    }
}