package net.iryndin.jdbf;

import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfReader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class TestDbfReader {

    @Test
    public void test1() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/gds_im.dbf");

        DbfRecord rec;
        try (DbfReader reader = new DbfReader(dbf)) {
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
