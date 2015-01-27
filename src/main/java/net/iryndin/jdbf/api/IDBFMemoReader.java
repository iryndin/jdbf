package net.iryndin.jdbf.api;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 */
public interface IDBFMemoReader extends Closeable {
    IDBFMemoMetadata getMetadata();
    IDBFMemoRecord read(int offsetInBlocks) throws IOException;
}
