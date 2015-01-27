package net.iryndin.jdbf.api;

/**
 *
 */
public interface IDBFMemoMetadata {
    int getNextFreeBlockLocation();
    public int getBlockSize();
}
