package net.iryndin.jdbf.util;

import net.iryndin.jdbf.core.DbfField;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;
import net.iryndin.jdbf.core.DbfFileTypeEnum;
import net.iryndin.jdbf.core.DbfMetadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbfMetadataUtils {

    public static DbfMetadata fromFieldsString(String s) {
        List<DbfField> fields = JdbfUtils.createFieldsFromString(s);

        DbfMetadata metadata = new DbfMetadata();

        metadata.setType(DbfFileTypeEnum.FoxBASEPlus1);
        metadata.setUpdateDate(new Date());
        //metadata.setRecordsQty(recordsQty);
        int fullHeaderLength = calculateFullHeaderLength(fields);
        metadata.setFullHeaderLength(fullHeaderLength);
        int oneRecordLength = calculateOneRecordLength(fields);
        metadata.setOneRecordLength(oneRecordLength);

        metadata.setFields(fields);

        return metadata;
    }

    private static int calculateOneRecordLength(List<DbfField> fields) {
        int result = 0;
        for (DbfField field : fields) {
            result += field.getLength();
        }
        result++;
        return result;
    }

    private static int calculateFullHeaderLength(List<DbfField> fields) {
        int result = 32;
        result += 32 * fields.size();
        result++;
        return result;
    }

//	public static byte[] toByteArray(DbfMetadata metadata) {
//		
//	}

    public static void fillHeaderFields(DbfMetadata metadata, byte[] headerBytes) {
        metadata.setType(DbfFileTypeEnum.fromInt(headerBytes[0]));
        metadata.setUpdateDate(JdbfUtils.createDate(headerBytes[1], headerBytes[2], headerBytes[3]));
        metadata.setRecordsQty(BitUtils.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]));
        metadata.setFullHeaderLength(BitUtils.makeInt(headerBytes[8], headerBytes[9]));
        metadata.setOneRecordLength(BitUtils.makeInt(headerBytes[10], headerBytes[11]));
        metadata.setUncompletedTxFlag(headerBytes[14]);
        metadata.setEcnryptionFlag(headerBytes[15]);
    }

    public static void readFields(DbfMetadata metadata, ByteArrayInputStream inputStream) throws IOException {
        List<DbfField> fields = new ArrayList<DbfField>();
        byte[] fieldBytes = new byte[JdbfUtils.FIELD_RECORD_LENGTH];
        int headerLength = 0;
        int fieldLength = 0;
        while (true) {
            inputStream.read(fieldBytes);
            DbfField field = createDbfField(fieldBytes);
            fields.add(field);

            fieldLength += field.getLength();
            headerLength += fieldBytes.length;

            long oldAvailable = inputStream.available();
            int terminator = inputStream.read();
            if (terminator == JdbfUtils.HEADER_TERMINATOR) {
                break;
            } else {
                inputStream.reset();
                inputStream.skip(inputStream.available() - oldAvailable);
            }
        }
        fieldLength += 1;
        headerLength += 32;
        headerLength += 1;

        if (headerLength != metadata.getFullHeaderLength()) {
            // can throw Exception here
        }
        if (fieldLength != metadata.getOneRecordLength()) {
            // can throw Exception here
        }

        metadata.setFields(fields);
    }

    public static DbfField createDbfField(byte[] fieldBytes) {
        DbfField field = new DbfField();
        // 1. Set name
        {
            int i = 0;
            for (i = 0; i < 11 && fieldBytes[i] > 0; i++) ;
            field.setName(new String(fieldBytes, 0, i));
        }
        // 2. Set type
        field.setType(DbfFieldTypeEnum.fromChar((char) fieldBytes[11]));
        // 3. Set length
        {
            int length = fieldBytes[16];
            if (length < 0) {
                length = 256 + length;
            }
            field.setLength(length);
        }
        // 4. Set number of decimal places
        field.setNumberOfDecimalPlaces(fieldBytes[17]);

        return field;
    }

    public static void writeDbfField(DbfField field, byte[] fieldBytes) {
        BitUtils.memset(fieldBytes, 0);
        byte[] nameBytes = field.getName().getBytes();
        int nameLength = nameBytes.length;
        if (nameLength > 11) {
            // throw error here!
        }
        System.arraycopy(nameBytes, 0, fieldBytes, 0, nameBytes.length);
        fieldBytes[11] = field.getType().toByte();
        int length = field.getLength();
        fieldBytes[16] = (byte) (length & 0xff);
        fieldBytes[17] = (byte) (field.getNumberOfDecimalPlaces() & 0xff);
    }

    @SuppressWarnings("deprecation")
    public static byte[] toByteArrayHeader(DbfMetadata metadata) {
        byte[] headerBytes = new byte[16];
        BitUtils.memset(headerBytes, 0);


        headerBytes[0] = metadata.getType().toByte();

        Date updateDate = metadata.getUpdateDate();
        // date
        if (updateDate == null) {
            updateDate = new Date();
        }
        // write date bytes
        {
            byte[] dateBytes = JdbfUtils.writeDateForHeader(updateDate);
            headerBytes[1] = dateBytes[0];
            headerBytes[2] = dateBytes[1];
            headerBytes[3] = dateBytes[2];
        }

        byte[] b = BitUtils.makeByte4(metadata.getRecordsQty());
        headerBytes[4] = b[0];
        headerBytes[5] = b[1];
        headerBytes[6] = b[2];
        headerBytes[7] = b[3];

        b = BitUtils.makeByte2(metadata.getFullHeaderLength());
        headerBytes[8] = b[0];
        headerBytes[9] = b[1];

        b = BitUtils.makeByte2(metadata.getOneRecordLength());
        headerBytes[10] = b[0];
        headerBytes[11] = b[1];

        headerBytes[12] = 0;
        headerBytes[13] = 0;

        headerBytes[14] = metadata.getUncompletedTxFlag();
        headerBytes[15] = metadata.getEcnryptionFlag();

        return headerBytes;
    }
}
