package net.iryndin.jdbf.core;

import net.iryndin.jdbf.util.BitUtils;

/**
 * See http://msdn.microsoft.com/en-US/library/8599s21w(v=vs.80).aspx
 */
public class MemoFileHeader {
    private byte[] headerBytes;
    private int nextFreeBlockLocation;
    private int blockSize;

    public static MemoFileHeader create(byte[] headerBytes) {
        MemoFileHeader h = new MemoFileHeader();
        h.setHeaderBytes(headerBytes);
        h.calculateHeaderFields();
        return h;
    }

    private void calculateHeaderFields() {
        this.nextFreeBlockLocation = BitUtils.makeInt(headerBytes[3],headerBytes[2],headerBytes[1],headerBytes[0]);
        this.blockSize = BitUtils.makeInt(headerBytes[7],headerBytes[6]);
    }

    private void setHeaderBytes(byte[] headerBytes) {
        this.headerBytes = headerBytes;
    }

    public int getNextFreeBlockLocation() {
        return nextFreeBlockLocation;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public String toString() {
        return "MemoFileHeader{" +
                "nextFreeBlockLocation=" + nextFreeBlockLocation +
                ", blockSize=" + blockSize +
                '}';
    }
}
