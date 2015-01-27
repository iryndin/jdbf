package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFMemoReader;
import net.iryndin.jdbf.api.IDBFMetadata;
import net.iryndin.jdbf.api.IDBFRecord;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

public class DBFReaderWithMemo extends DBFReaderImpl {

    private final IDBFMemoReader memoReader;

    public DBFReaderWithMemo(InputStream inputStream, InputStream memoStream) throws IOException {
        super(inputStream);
        this.memoReader = new DBFMemoReaderImpl(memoStream);
    }

    @Override
    public void close() throws IOException {
        memoReader.close();
        super.close();
    }

    @Override
    public Iterator<IDBFRecord> iterator() {
        return new DBFRecordIteratorWithMemo(inputStream, memoReader, metadata, charset);
    }

    private static class DBFRecordIteratorWithMemo implements Iterator<IDBFRecord> {

        private final InputStream inputStream;
        private final IDBFMemoReader memoReader;
        private final IDBFMetadata metadata;
        private final Charset charset;
        private int currentRecordNumber = 0;

        private DBFRecordIteratorWithMemo(InputStream inputStream, IDBFMemoReader memoReader, IDBFMetadata metadata, Charset charset) {
            this.inputStream = inputStream;
            this.memoReader = memoReader;
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
                return new DBFRecordWithMemo(recordBuffer, currentRecordNumber++, metadata, charset, memoReader);
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
