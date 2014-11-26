package net.iryndin.jdbf;

import net.iryndin.jdbf.core.DbfField;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;
import net.iryndin.jdbf.core.DbfFileTypeEnum;
import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.util.DbfMetadataUtils;
import net.iryndin.jdbf.writer.DbfWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class TestDbfWriter {
    private final Map<String, Object> valueMap = new HashMap<>();
    private String filePath;
    private List<DbfField> fields = new ArrayList<>();

    public DbfField addCharDBFField(String name, int length) {
        final DbfField fld = new DbfField();
        fld.setName(name);
        fld.setType(DbfFieldTypeEnum.Character);
        fld.setLength(length);
        fields.add(fld);
        return fld;
    }

  /*if(encoding.equals("UTF-8"))
  lenght *= 2;*/

    public DbfField addNumDBFField(String name, int length, int decimal) {
        final DbfField fld = new DbfField();
        fld.setName(name);
        fld.setType(DbfFieldTypeEnum.Numeric);
        fld.setLength(length);
        fld.setNumberOfDecimalPlaces(decimal);
        fields.add(fld);
        return fld;
    }

    public DbfField addDateDBFField(String name) {
        final DbfField fld = new DbfField();
        fld.setName(name);
        fld.setType(DbfFieldTypeEnum.Date);
        fields.add(fld);
        return fld;
    }

    @Before
    public void prepareData() {
        valueMap.put("FIOISP", "Виноградова Ольга Евгеньевна");
        valueMap.put("NAME", "Вячеслав");
        valueMap.put("SURNAME", "Егоров");
        valueMap.put("DATER", "30.06.1971");
        valueMap.put("SECONDNAME", "Иванович");
        valueMap.put("UNICODE", new BigDecimal(1001731864));
        valueMap.put("NUMID", "6/14/19/69");
        filePath = "G:\\test\\" + new Date().getTime() + ".dbf";
        fields.add(addCharDBFField("FIOISP", 100));
        fields.add(addCharDBFField("NAME", 250));
        fields.add(addCharDBFField("SURNAME", 250));
        fields.add(addCharDBFField("DATER", 10));
        fields.add(addCharDBFField("SECONDNAME", 250));
        fields.add(addNumDBFField("UNICODE", 10, 10));
        fields.add(addCharDBFField("NUMID", 100));
    }

    @Test
    public void test() throws IOException {
        DbfMetadata dbfMetadata = new DbfMetadata();
        dbfMetadata.setFields(fields);
        dbfMetadata.setOneRecordLength(DbfMetadataUtils.calculateOneRecordLength(fields));
        dbfMetadata.setType(DbfFileTypeEnum.FoxBASE2);
        FileOutputStream fos = null;
        DbfWriter dbfWriter = null;
        try {
            fos = new FileOutputStream("111.dbf");
            dbfWriter = new DbfWriter(dbfMetadata, fos);
            final String encoding = "CP866";
            dbfWriter.setStringCharset(encoding);
            dbfWriter.write(valueMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dbfWriter != null) {
                dbfWriter.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}
