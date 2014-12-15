package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.api.IDBFRecord;
import net.iryndin.jdbf.core.DbfField;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class DBFRecordImpl implements IDBFRecord {

    public final static int EMPTY = 0x20;

    public final static String NUMERIC_OVERFLOW = "*";

    private final byte[] recordBytes;
    private final int recordNumber;
    private final IDBFMetadata metadata;
    private Charset charset;

    public DBFRecordImpl(byte[] recordBytes, int recordNumber, IDBFMetadata metadata, Charset charset) {
        this.recordBytes = recordBytes;
        this.recordNumber = recordNumber;
        this.metadata = metadata;
        this.charset = charset;
    }

    public static final ThreadLocal<SimpleDateFormat> dateFormatRef = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public int getRecordNumber() {
        return recordNumber;
    }

    @Override
    public boolean isDeleted() {
        return this.recordBytes[0] == 0x2A;
    }

    @Override
    public Collection<IDBFField> getFields() {
        return metadata.getFields();
    }

    @Override
    public IDBFField getField(String name) {
        return metadata.getField(name);
    }

    @Override
    public byte[] getBytes(String name) {
        byte[] bytes = null;
        IDBFField fieldMeta = getField(name);
        if (fieldMeta != null) {
            bytes = new byte[fieldMeta.getLength()];
            System.arraycopy(bytes, fieldMeta.getOffset(), bytes, 0, fieldMeta.getLength());
        }
        return bytes;
    }

    @Override
    public String getString(String name) {
        return getString(name, getCharset());
    }

    @Override
    public String getString(String name, Charset strCharset) {
        IDBFField fieldMeta = getField(name);
        if (fieldMeta != null) {
            byte[] bytes = new byte[fieldMeta.getLength()];
            System.arraycopy(bytes, fieldMeta.getOffset(), bytes, 0, fieldMeta.getLength());

            int actualOffset = fieldMeta.getOffset();
            int actualLength = fieldMeta.getLength();

            // check for empty strings
            while ((actualLength > 0) && (bytes[actualOffset] == EMPTY)) {
                actualOffset++;
                actualLength--;
            }

            while ((actualLength > 0) && (bytes[actualOffset + actualLength - 1] == EMPTY)) {
                actualLength--;
            }

            if (actualLength == 0) {
                return null;
            }

            return new String(bytes, actualOffset, actualLength, charset);
        }
        return null;
    }

    @Override
    public Date getDate(String name) {
        String str = getString(name);
        try {
            return dateFormatRef.get().parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean getBoolean(String name) {
        String s = getString(name);
        if (s != null) {
            if (s.equalsIgnoreCase("t")) {
                return Boolean.TRUE;
            } else if (s.equalsIgnoreCase("f")) {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String name) {
        String str = getString(name).trim();
        if (str != null && !str.isEmpty()) {
            if (str.contains(NUMERIC_OVERFLOW)) {
                return null;
            }
            return new BigDecimal(str);
        }
        return null;
    }

    @Override
    public Double getDouble(String name) {
        BigDecimal n = getBigDecimal(name);
        return n != null ? n.doubleValue() : null;
    }

    @Override
    public Integer getInt(String name) {
        BigDecimal n = getBigDecimal(name);
        return n != null ? n.intValue() : null;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        for (IDBFField field : getFields()) {
            switch (field.getType()) {
                case Character:
                case Varchar:
                    map.put(field.getName(), getString(field.getName()));
                    break;
                case Currency:
                case Numeric:
                    map.put(field.getName(), getBigDecimal(field.getName()));
                    break;
                case Float:
                case Double:
                    map.put(field.getName(), getDouble(field.getName()));
                    break;
                case Integer:
                    map.put(field.getName(), getInt(field.getName()));
                    break;
                case Date:
                case DateTime:
                    map.put(field.getName(), getDate(field.getName()));
                    break;
                case Logical:
                    map.put(field.getName(), getBoolean(field.getName()));
                    break;
                case Memo:
                    // TODO: implement MemoReader
                    break;
                case General:
                case Picture:
                case Blob:
                case Varbinary:
                case NullFlags:
                default:
                    map.put(field.getName(), getBytes(field.getName()));
                    break;
            }
        }

        return map;
    }
}
