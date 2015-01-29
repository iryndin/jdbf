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

    protected final InputStream inputStream;
    protected final IDBFMetadata metadata;
    protected Charset charset = Charset.defaultCharset();

    public DBFReaderImpl(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.metadata = readHeaderAndFields();
        if (this.metadata.getHeader().getCharset() != null) {
            this.charset = this.metadata.getHeader().getCharset();
        }
    }

    private IDBFMetadata readHeaderAndFields() throws IOException {
        DBFMetadataReader metadataReader = new DBFMetadataReader(inputStream);
        IDBFMetadata md  = metadataReader.readDbfMetadata();
        inputStream.skip(md.getHeader().getFullHeaderLength() - metadataReader.getReadBytesCount());
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
            Arrays.fill(recordBuffer, (byte)0x0);
            try {
                int bytesRead = inputStream.read(recordBuffer);
                if (bytesRead != recordLength) {
                    throw new IllegalStateException("Read less bytes than record length! Bytes read: " + bytesRead +", record length: " + recordLength);
                }
                return new DBFRecordImpl(recordBuffer, currentRecordNumber++, metadata, charset);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported!");
        }
    }
}
