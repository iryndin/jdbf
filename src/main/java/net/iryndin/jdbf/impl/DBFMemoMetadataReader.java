package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFMemoMetadata;
import net.iryndin.jdbf.util.BitUtils;

import java.io.IOException;
import java.io.InputStream;

public class DBFMemoMetadataReader {

    public static final int MEMO_HEADER_LENGTH = 0x200; // 512 bytes

    private final InputStream inputStream;
    private int readBytesCount = 0;

    public DBFMemoMetadataReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public IDBFMemoMetadata readMetadata() throws IOException {
        byte[] headerBytes = new byte[MEMO_HEADER_LENGTH];
        readBytesCount += inputStream.read(headerBytes);
        final int nextFreeBlockLocation = BitUtils.makeInt(headerBytes[3], headerBytes[2], headerBytes[1], headerBytes[0]);
        final int blockSize = BitUtils.makeInt(headerBytes[7], headerBytes[6]);

        return new IDBFMemoMetadata() {

            @Override
            public int getNextFreeBlockLocation() {
                return nextFreeBlockLocation;
            }

            @Override
            public int getBlockSize() {
                return blockSize;
            }
        };
    }

    public int getReadBytesCount() {
        return readBytesCount;
    }
}
