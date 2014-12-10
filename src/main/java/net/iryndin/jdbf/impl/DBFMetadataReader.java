package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.api.IDBFHeader;
import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;
import net.iryndin.jdbf.core.DbfFileTypeEnum;
import net.iryndin.jdbf.util.BitUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reads header and metadata info
 */
public class DBFMetadataReader {

    public static final int FIELD_RECORD_LENGTH = 32;
    public static final int HEADER_TERMINATOR = 0x0D;

    private final InputStream inputStream;

    public DBFMetadataReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public IDBFMetadata readDbfMetadata() throws IOException {
        IDBFHeader header = readHeader();
        List<IDBFField> fields = readFields(header);
        return new DBFMetadataImpl(header, fields);
    }

    public IDBFHeader readHeader() throws IOException {
        byte[] headerBytes = new byte[16];
        int bytesRead = inputStream.read(headerBytes);
        if (bytesRead != 16) {
            throw new IOException("When reading DBF header should read exactly 16 bytes! Bytes read instead: " + bytesRead);
        }

        DbfFileTypeEnum type = DbfFileTypeEnum.fromInt(headerBytes[0]);
        Date updateDate = parseHeaderUpdateDate(headerBytes[1], headerBytes[2], headerBytes[3], type);
        int recordsQty = BitUtils.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]);
        int fullHeaderLength = BitUtils.makeInt(headerBytes[8], headerBytes[9]);
        int oneRecordLength = BitUtils.makeInt(headerBytes[10], headerBytes[11]);
        byte uncompletedTxFlag = headerBytes[14];
        byte ecnryptionFlag = headerBytes[15];

        return new DBFHeaderImpl(type, updateDate, recordsQty, fullHeaderLength, oneRecordLength, uncompletedTxFlag, ecnryptionFlag);
    }

    public List<IDBFField> readFields(IDBFHeader header) throws IOException {
        List<IDBFField> fields = new ArrayList<>();
        byte[] fieldBytes = new byte[FIELD_RECORD_LENGTH];
        int headerLength = 0;
        int fieldLength = 0;
        while (true) {
            inputStream.read(fieldBytes);
            IDBFField field = readDbfField(fieldBytes);
            fields.add(field);

            fieldLength += field.getLength();
            headerLength += fieldBytes.length;

            long oldAvailable = inputStream.available();
            int terminator = inputStream.read();
            if (terminator == HEADER_TERMINATOR) {
                break;
            } else {
                inputStream.reset();
                inputStream.skip(inputStream.available() - oldAvailable);
            }
        }
        fieldLength += 1;
        headerLength += 32;
        headerLength += 1;

        if (headerLength != header.getFullHeaderLength()) {
            throw new IllegalStateException("headerLength != header.getFullHeaderLength()");
        }
        if (fieldLength != header.getOneRecordLength()) {
            throw new IllegalStateException("fieldLength != header.getOneRecordLength()");
        }

        return fields;
    }

    /**
     *
     * Read field value according to this info:
     * Title: Table File Structure (.dbc, .dbf, .frx, .lbx, .mnx, .pjx, .scx, .vcx)
     * http://msdn.microsoft.com/en-US/library/st4a0s68(v=vs.80).aspx
     *
     * @param fieldBytes
     * @return
     */
    private static IDBFField readDbfField(byte[] fieldBytes) {

        String name;
        int length;
        int numberOfDecimalPlaces;

        // 1. Set name
        {
            int i = 0;
            for (i = 0; i < 11 && fieldBytes[i] > 0; i++) ;
            name = new String(fieldBytes, 0, i);
        }
        // 2. Set type
        DbfFieldTypeEnum type = DbfFieldTypeEnum.fromChar((char) fieldBytes[11]);
        // 3. Set length
        {
            length = fieldBytes[16];
            if (length < 0) {
                length = 256 + length;
            }
        }
        // 4. Set number of decimal places
        numberOfDecimalPlaces = fieldBytes[17];

        return new DBFFieldImpl(name, type, length, numberOfDecimalPlaces);
    }

    private static Date parseHeaderUpdateDate(byte yearByte, byte monthByte, byte dayByte, DbfFileTypeEnum fileType) {
        int year = yearByte + 2000 - 1900;
        switch (fileType) {
            case FoxBASEPlus1:
                year = yearByte;
        }
        int month = monthByte - 1;
        int day = dayByte;
        return new Date(year,month,day);

    }
}
