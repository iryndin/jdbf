package net.iryndin.jdbf.api;

import net.iryndin.jdbf.core.MemoRecordTypeEnum;

public interface IDBFMemoRecord {
    byte[] getBytes();
    int getLength();
    MemoRecordTypeEnum getMemoType();
    int getBlockSize();
    int getOffsetInBlocks();
}
