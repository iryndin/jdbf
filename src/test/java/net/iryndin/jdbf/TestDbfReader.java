package net.iryndin.jdbf;

import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfReader;
import net.iryndin.jdbf.util.JdbfUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestDbfReader {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
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
    
    @Test
    public void testEmptyStream() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {});
    	exception.expect(IOException.class);
    	exception.expectMessage("The file is corrupted or is not a dbf file");
        try (DbfReader reader = new DbfReader(dbf)) {
        }
    }
    
    @Test
    public void testOneByteStreamWithGoodFileType() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {0x02});
    	exception.expect(IOException.class);
    	exception.expectMessage("The file is corrupted or is not a dbf file");
        try (DbfReader reader = new DbfReader(dbf)) {
        }
    }
    
    @Test
    public void testOneByteStreamWithBadFileType() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {0x02});
    	exception.expect(IOException.class);
    	exception.expectMessage("The file is corrupted or is not a dbf file");
        try (DbfReader reader = new DbfReader(dbf)) {
        }
    }
    
    @Test
    public void testSixteenByteStreamWithGoodFileType() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2});
    	exception.expect(IOException.class);
    	exception.expectMessage("The file is corrupted or is not a dbf file");
        try (DbfReader reader = new DbfReader(dbf)) {
        }
    }
    
    @Test
    public void testThirtyTwoByteStreamWithGoodFileType() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2 });
    	exception.expect(IOException.class);
    	exception.expectMessage("The file is corrupted or is not a dbf file");
        try (DbfReader reader = new DbfReader(dbf)) {
        }
    }
    
    @Test
    public void testSixtyFourByteStreamWithGoodFileType() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2 });
    	exception.expect(IOException.class);
    	exception.expectMessage("The file is corrupted or is not a dbf file");
        try (DbfReader reader = new DbfReader(dbf)) {
        }
    }
    
    @Test
    public void testSixtyFourByteStreamWithGoodFileTypeAndCloseHeader() throws IOException {
    	InputStream dbf = new ByteArrayInputStream(new byte[] {
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 
    			JdbfUtils.HEADER_TERMINATOR });
        try (DbfReader reader = new DbfReader(dbf)) {
        	assertNull(reader.read());
        }
    }
}
