package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.api.IDBFMemoReader;
import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;
import net.iryndin.jdbf.util.BitUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class DBFRecordWithMemo extends DBFRecordImpl {

    private final IDBFMemoReader memoReader;

    public DBFRecordWithMemo(byte[] recordBytes, int recordNumber, IDBFMetadata metadata, Charset charset, IDBFMemoReader memoReader) {
        super(recordBytes, recordNumber, metadata, charset);
        this.memoReader = memoReader;
    }

    @Override
    public byte[] getBytes(String name) throws IOException {
        IDBFField fieldMeta = getField(name);
        if (fieldMeta != null && fieldMeta.getType() == DbfFieldTypeEnum.Memo) {
            byte[] dbfFieldBytes = new byte[fieldMeta.getLength()];
            System.arraycopy(super.recordBytes, fieldMeta.getOffset(), dbfFieldBytes, 0, fieldMeta.getLength());
            int offsetInBlocks = BitUtils.makeInt(dbfFieldBytes[0], dbfFieldBytes[1], dbfFieldBytes[2], dbfFieldBytes[3]);
            return memoReader.read(offsetInBlocks).getBytes();
        } else {
            return super.getBytes(name);
        }
    }
}
