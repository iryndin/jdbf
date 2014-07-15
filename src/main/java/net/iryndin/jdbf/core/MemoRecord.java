package net.iryndin.jdbf.core;

import net.iryndin.jdbf.util.BitUtils;

import java.nio.charset.Charset;

public class MemoRecord {
    private final int blockSize;
    private final int offsetInBlocks;
    private byte[] value;
    private int length;
    private MemoRecordTypeEnum memoType;

    public MemoRecord(byte[] header, byte[] value, int blockSize, int offsetInBlocks) {
        this.value = value;
        calculateFields(header);
        this.blockSize = blockSize;
        this.offsetInBlocks = offsetInBlocks;
    }

    private void calculateFields(byte[] bytes) {
        int type = BitUtils.makeInt(bytes[3],bytes[2],bytes[1],bytes[0]);
        this.memoType = MemoRecordTypeEnum.fromInt(type);
        this.length = BitUtils.makeInt(bytes[7],bytes[6],bytes[5],bytes[4]);
    }

    public byte[] getValue() {
        return value;
    }

    public String getValueAsString(Charset charset) {
        return new String(value, charset);
    }

    /**
     * Memo record length in bytes
     * @return
     */
    public int getLength() {
        return length;
    }

    public MemoRecordTypeEnum getMemoType() {
        return memoType;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getOffsetInBlocks() {
        return offsetInBlocks;
    }
}
