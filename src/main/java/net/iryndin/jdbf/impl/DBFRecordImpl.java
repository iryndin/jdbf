package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.api.IDBFRecord;

import java.nio.charset.Charset;
import java.util.Collection;

/**
 *
 */
public class DBFRecordImpl implements IDBFRecord {
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
}
