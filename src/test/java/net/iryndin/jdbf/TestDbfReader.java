package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;
import net.iryndin.jdbf.core.DbfFileTypeEnum;
import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfReader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestDbfReader {

    @Test
    public void test1() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/gds_im.dbf");


        try (IDBFReader reader = JDBF.createDBFReader(dbf)) {
            IDBFMetadata meta = reader.getMetadata();

            {
                IDBFField f = meta.getField("KONTR");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(1, f.getLength());
                assertEquals(1, f.getOffset());
            }

            {
                IDBFField f = meta.getField("N_MDP");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(8, f.getLength());
                assertEquals(2, f.getOffset());
            }

            {
                IDBFField f = meta.getField("W_LIST_NO");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Numeric, f.getType());
                assertEquals(2, f.getLength());
                assertEquals(10, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G32");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Numeric, f.getType());
                assertEquals(3, f.getLength());
                assertEquals(12, f.getOffset());
            }

            {
                IDBFField f = meta.getField("N_RECEIVER");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Numeric, f.getType());
                assertEquals(1, f.getLength());
                assertEquals(15, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G33");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(10, f.getLength());
                assertEquals(16, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G312");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(250, f.getLength());
                assertEquals(26, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G35");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Numeric, f.getType());
                assertEquals(13, f.getLength());
                assertEquals(276, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G311");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(9, f.getLength());
                assertEquals(289, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G318");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(14, f.getLength());
                assertEquals(298, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G315");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Numeric, f.getType());
                assertEquals(11, f.getLength());
                assertEquals(312, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G317C");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(3, f.getLength());
                assertEquals(323, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G221");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(3, f.getLength());
                assertEquals(326, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G221_BUK");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(3, f.getLength());
                assertEquals(329, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G42");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Numeric, f.getType());
                assertEquals(15, f.getLength());
                assertEquals(332, f.getOffset());
            }

            {
                IDBFField f = meta.getField("KODS_PT");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(3, f.getLength());
                assertEquals(347, f.getOffset());
            }

            {
                IDBFField f = meta.getField("KODS_ABC2");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(2, f.getLength());
                assertEquals(350, f.getOffset());
            }

            {
                IDBFField f = meta.getField("N_TTH");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(30, f.getLength());
                assertEquals(352, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G442REGNU");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(28, f.getLength());
                assertEquals(382, f.getOffset());
            }

            {
                IDBFField f = meta.getField("DELIV_PPP");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(6, f.getLength());
                assertEquals(410, f.getOffset());
            }

            {
                IDBFField f = meta.getField("G40T");
                assertNotNull(f);
                assertEquals(DbfFieldTypeEnum.Character, f.getType());
                assertEquals(6, f.getLength());
                assertEquals(410, f.getOffset());
            }




            System.out.println(meta);
            /*
            DbfMetadata meta = reader.getMetadata();

            assertEquals(5, meta.getRecordsQty());
            assertEquals(28, meta.getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.toMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
            */
        }
    }

    @Test
    public void test11() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/gds_im.dbf");

        DbfRecord rec;
        try (DbfReader reader = new DbfReader(dbf)) {
            DbfMetadata meta = reader.getMetadata();
            System.out.println(meta);
            /*

            assertEquals(5, meta.getRecordsQty());
            assertEquals(28, meta.getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.toMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
            */
        }
    }

    @Test
    public void test2() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/tir_im.dbf");

        DbfRecord rec;
        try (DbfReader reader = new DbfReader(dbf)) {
            DbfMetadata meta = reader.getMetadata();

            assertEquals(1, meta.getRecordsQty());
            assertEquals(117, meta.getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.toMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        }
    }
}
