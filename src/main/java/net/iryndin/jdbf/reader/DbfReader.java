package net.iryndin.jdbf.reader;

import net.iryndin.jdbf.core.*;
import net.iryndin.jdbf.util.DbfMetadataUtils;
import net.iryndin.jdbf.util.IOUtils;

import java.io.*;
import java.util.Arrays;

public class DbfReader implements Closeable {

    private static final int HEADER_HALF_SIZE = 16;
	private InputStream dbfInputStream;
    private MemoReader memoReader;
    private DbfMetadata metadata;
    private byte[] oneRecordBuffer;
    private int recordsCounter = 0;
    private static final int BUFFER_SIZE = 8192;

    public DbfReader(File dbfFile) throws IOException {
        this(new FileInputStream(dbfFile));
    }

    public DbfReader(File dbfFile, File memoFile) throws IOException {
        this(new FileInputStream(dbfFile), new FileInputStream(memoFile));
    }

    public DbfReader(InputStream dbfInputStream) throws IOException {
        this.dbfInputStream = new BufferedInputStream(dbfInputStream, BUFFER_SIZE);
        readMetadata();
    }

    public DbfReader(InputStream dbfInputStream, InputStream memoInputStream) throws IOException {
        this.dbfInputStream = new BufferedInputStream(dbfInputStream, BUFFER_SIZE);
        this.memoReader = new MemoReader(memoInputStream);
        readMetadata();
    }

    public DbfMetadata getMetadata() {
        return metadata;
    }

    private void readMetadata() throws IOException {
        this.dbfInputStream.mark(1024*1024);
        metadata = new DbfMetadata();
        readHeader();
        DbfMetadataUtils.readFields(metadata, dbfInputStream);

        oneRecordBuffer = new byte[metadata.getOneRecordLength()];

        findFirstRecord();
    }

    private void readHeader() throws IOException {
        // 1. Allocate buffer
        byte[] bytes = new byte[HEADER_HALF_SIZE];
        // 2. Read 16 bytes
        if (IOUtils.readFully(dbfInputStream, bytes) != HEADER_HALF_SIZE)
            throw new IOException("The file is corrupted or is not a dbf file");

        // 3. Fill header fields
        DbfMetadataUtils.fillHeaderFields(metadata, bytes);
        // 4. Read next 16 bytes (for most DBF types these are reserved bytes)
        if (IOUtils.readFully(dbfInputStream, bytes) != HEADER_HALF_SIZE)
            throw new IOException("The file is corrupted or is not a dbf file");
    }

    @Override
    public void close() throws IOException {
        if (memoReader != null) {
            memoReader.close();
            memoReader = null;
        }
        if (dbfInputStream != null) {
            dbfInputStream.close();
            dbfInputStream = null;
        }
        metadata = null;
        recordsCounter = 0;
    }

    public void findFirstRecord() throws IOException {
        seek(dbfInputStream, metadata.getFullHeaderLength());
    }

    private void seek(InputStream inputStream, int position) throws IOException {
        inputStream.reset();
        inputStream.skip(position);
    }

    public DbfRecord read() throws IOException {
        Arrays.fill(oneRecordBuffer, (byte)0x0);
        int readLength = IOUtils.readFully(dbfInputStream, oneRecordBuffer);

        if (readLength < metadata.getOneRecordLength()) {
            return null;
        }

        return createDbfRecord();
    }

    private DbfRecord createDbfRecord() {
        return new DbfRecord(oneRecordBuffer, metadata, memoReader, ++recordsCounter);
    }
}
