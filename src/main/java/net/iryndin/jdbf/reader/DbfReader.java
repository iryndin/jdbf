package net.iryndin.jdbf.reader;

import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.util.DbfMetadataUtils;

import java.io.*;

public class DbfReader {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private ByteArrayInputStream inputStream;
    private DbfMetadata metadata;
    private byte[] oneRecordBuffer;

    public DbfReader(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public DbfReader(InputStream inputStream) throws IOException {
        this.inputStream = new ByteArrayInputStream(toByteArray(inputStream));

        readMetadata();
    }

    public DbfMetadata getMetadata() {
        return metadata;
    }

    private void readMetadata() throws IOException {
        metadata = new DbfMetadata();
        readHeader();
        DbfMetadataUtils.readFields(metadata, inputStream);

        oneRecordBuffer = new byte[metadata.getOneRecordLength()];

        findFirstRecord();
    }

    private void readHeader() throws IOException {
        // 1. Allocate buffer
        byte[] bytes = new byte[16];
        // 2. Read 16 bytes
        inputStream.read(bytes);
        // 3. Fill header fields
        DbfMetadataUtils.fillHeaderFields(metadata, bytes);
        // 4. Read next 16 bytes (for most DBF types these are reserved bytes)
        inputStream.read(bytes);
    }

    public void close() throws IOException {
        inputStream.close();
    }

    public void findFirstRecord() throws IOException {
        seek(inputStream, metadata.getFullHeaderLength());
    }

    private void seek(ByteArrayInputStream inputStream, int position) {
        inputStream.reset();
        inputStream.skip(position);
    }

    public DbfRecord read() throws IOException {
        int readLength = inputStream.read(oneRecordBuffer);

        if (readLength == -1) {
            return null;
        }

        return createDbfRecord();
    }

    private DbfRecord createDbfRecord() {
        return new DbfRecord(oneRecordBuffer, metadata);
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[DEFAULT_BUFFER_SIZE];
        int dataLength;
        while ((dataLength = input.read(data)) > 0) {
            buffer.write(data, 0, dataLength);
        }

        return buffer.toByteArray();
    }
}
