package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.api.IDBFRecord;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 */
public class DBFReaderImpl implements IDBFReader {

    private final InputStream inputStream;
    private final IDBFMetadata metadata;
    private Charset charset = Charset.defaultCharset();

    public DBFReaderImpl(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("InputStream should support marks! InputStream.markSupported() should return true!");
        }
        this.metadata = readHeaderAndFields();
        positionToFirstRecord();
    }

    private void positionToFirstRecord() throws IOException {
        inputStream.skip(metadata.getHeader().getFullHeaderLength());
    }

    private IDBFMetadata readHeaderAndFields() throws IOException {
        inputStream.mark(Integer.MAX_VALUE);
        IDBFMetadata md  = new DBFMetadataReader(inputStream).readDbfMetadata();
        inputStream.reset();
        return md;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public IDBFMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public Iterator<IDBFRecord> iterator() {
        return new DBFRecordIterator(inputStream, metadata, charset);
    }

    private static class DBFRecordIterator implements Iterator<IDBFRecord> {

        private final InputStream inputStream;
        private final IDBFMetadata metadata;
        private final Charset charset;
        private int currentRecordNumber = 0;

        private DBFRecordIterator(InputStream inputStream, IDBFMetadata metadata, Charset charset) {
            this.inputStream = inputStream;
            this.metadata = metadata;
            this.charset = charset;
        }

        @Override
        public boolean hasNext() {
            return currentRecordNumber < metadata.getHeader().getRecordsQty();
        }

        @Override
        public IDBFRecord next() {
            final int recordLength = metadata.getHeader().getOneRecordLength();
            byte[] recordBuffer = new byte[recordLength];
            try {
                int bytesRead = inputStream.read(recordBuffer);
                if (bytesRead != recordLength) {
                    throw new IllegalStateException("Read less bytes than record length! Bytes read: " + bytesRead +", record length: " + recordLength);
                }
                Arrays.fill(recordBuffer, (byte)0x0);
                return new DBFRecordImpl(recordBuffer, currentRecordNumber++, metadata, charset);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported!");
        }
    };
}
