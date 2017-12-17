/*
 * This file is generated by jOOQ.
*/
package media.thehoard.common.database.jooq.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
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
public class MediafilesRecord extends UpdatableRecordImpl<MediafilesRecord> implements Record9<Integer, String, String, Short, String, Double, Double, Integer, Byte> {

    private static final long serialVersionUID = -509266298;

    /**
     * Setter for <code>thehoard.MediaFiles.Id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.Id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.Uuid</code>.
     */
    public void setUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.Uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.ProviderFileUuid</code>.
     */
    public void setProviderfileuuid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.ProviderFileUuid</code>.
     */
    public String getProviderfileuuid() {
        return (String) get(2);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.StreamCount</code>.
     */
    public void setStreamcount(Short value) {
        set(3, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.StreamCount</code>.
     */
    public Short getStreamcount() {
        return (Short) get(3);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.Format</code>.
     */
    public void setFormat(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.Format</code>.
     */
    public String getFormat() {
        return (String) get(4);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.StartTime</code>.
     */
    public void setStarttime(Double value) {
        set(5, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.StartTime</code>.
     */
    public Double getStarttime() {
        return (Double) get(5);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.Duration</code>.
     */
    public void setDuration(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.Duration</code>.
     */
    public Double getDuration() {
        return (Double) get(6);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.BitRate</code>.
     */
    public void setBitrate(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.BitRate</code>.
     */
    public Integer getBitrate() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>thehoard.MediaFiles.ProbeScore</code>.
     */
    public void setProbescore(Byte value) {
        set(8, value);
    }

    /**
     * Getter for <code>thehoard.MediaFiles.ProbeScore</code>.
     */
    public Byte getProbescore() {
        return (Byte) get(8);
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
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Integer, String, String, Short, String, Double, Double, Integer, Byte> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Integer, String, String, Short, String, Double, Double, Integer, Byte> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Mediafiles.MEDIAFILES.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Mediafiles.MEDIAFILES.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Mediafiles.MEDIAFILES.PROVIDERFILEUUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field4() {
        return Mediafiles.MEDIAFILES.STREAMCOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Mediafiles.MEDIAFILES.FORMAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field6() {
        return Mediafiles.MEDIAFILES.STARTTIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field7() {
        return Mediafiles.MEDIAFILES.DURATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return Mediafiles.MEDIAFILES.BITRATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field9() {
        return Mediafiles.MEDIAFILES.PROBESCORE;
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
        return getProviderfileuuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component4() {
        return getStreamcount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getFormat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double component6() {
        return getStarttime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double component7() {
        return getDuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component8() {
        return getBitrate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component9() {
        return getProbescore();
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
        return getProviderfileuuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value4() {
        return getStreamcount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getFormat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value6() {
        return getStarttime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value7() {
        return getDuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getBitrate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value9() {
        return getProbescore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value2(String value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value3(String value) {
        setProviderfileuuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value4(Short value) {
        setStreamcount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value5(String value) {
        setFormat(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value6(Double value) {
        setStarttime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value7(Double value) {
        setDuration(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value8(Integer value) {
        setBitrate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord value9(Byte value) {
        setProbescore(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MediafilesRecord values(Integer value1, String value2, String value3, Short value4, String value5, Double value6, Double value7, Integer value8, Byte value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MediafilesRecord
     */
    public MediafilesRecord() {
        super(Mediafiles.MEDIAFILES);
    }

    /**
     * Create a detached, initialised MediafilesRecord
     */
    public MediafilesRecord(Integer id, String uuid, String providerfileuuid, Short streamcount, String format, Double starttime, Double duration, Integer bitrate, Byte probescore) {
        super(Mediafiles.MEDIAFILES);

        set(0, id);
        set(1, uuid);
        set(2, providerfileuuid);
        set(3, streamcount);
        set(4, format);
        set(5, starttime);
        set(6, duration);
        set(7, bitrate);
        set(8, probescore);
    }
}
