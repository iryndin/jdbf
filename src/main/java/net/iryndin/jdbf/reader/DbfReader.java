package net.iryndin.jdbf.reader;

import net.iryndin.jdbf.core.*;
import net.iryndin.jdbf.util.DbfMetadataUtils;
import net.iryndin.jdbf.util.IOUtils;

import java.io.*;

public class DbfReader implements Closeable {

    private ByteArrayInputStream dbfInputStream;
    private MemoReader memoReader;
    private DbfMetadata metadata;
    private byte[] oneRecordBuffer;

    public DbfReader(File dbfFile) throws IOException {
        this(new FileInputStream(dbfFile));
    }

    public DbfReader(File dbfFile, File memoFile) throws IOException {
        this(new FileInputStream(dbfFile), new FileInputStream(memoFile));
    }

    public DbfReader(InputStream dbfInputStream) throws IOException {
        this.dbfInputStream = new ByteArrayInputStream(IOUtils.toByteArray(dbfInputStream));
        readMetadata();
    }

    public DbfReader(InputStream dbfInputStream, InputStream memoInputStream) throws IOException {
        this.dbfInputStream = new ByteArrayInputStream(IOUtils.toByteArray(dbfInputStream));
        this.memoReader = new MemoReader(memoInputStream);
        readMetadata();
    }

    public DbfMetadata getMetadata() {
        return metadata;
    }

    private void readMetadata() throws IOException {
        metadata = new DbfMetadata();
        readHeader();
        DbfMetadataUtils.readFields(metadata, dbfInputStream);

        oneRecordBuffer = new byte[metadata.getOneRecordLength()];

        findFirstRecord();
    }

    private void readHeader() throws IOException {
        // 1. Allocate buffer
        byte[] bytes = new byte[16];
        // 2. Read 16 bytes
        dbfInputStream.read(bytes);
        // 3. Fill header fields
        DbfMetadataUtils.fillHeaderFields(metadata, bytes);
        // 4. Read next 16 bytes (for most DBF types these are reserved bytes)
        dbfInputStream.read(bytes);
    }

    @Override
    public void close() throws IOException {
        if (memoReader != null) {
            memoReader.close();
        }
        if (dbfInputStream != null) {
            dbfInputStream.close();
        }
    }

    public void findFirstRecord() throws IOException {
        seek(dbfInputStream, metadata.getFullHeaderLength());
    }

    private void seek(ByteArrayInputStream inputStream, int position) {
        inputStream.reset();
        inputStream.skip(position);
    }

    public DbfRecord read() throws IOException {
        int readLength = dbfInputStream.read(oneRecordBuffer);

        if (readLength == -1) {
            return null;
        }

        return createDbfRecord();
    }

    private DbfRecord createDbfRecord() {
        return new DbfRecord(oneRecordBuffer, metadata, memoReader);
    }
}
