package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.impl.DBFReaderImpl;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class JDBF {
    /**
     * Factory method that creates IDBFReader
     *
     * @param inputStream InputStream that holds DBF file
     * @return IDBFReader implementation
     * @throws IOException if IO errors occur
     */
    public static IDBFReader createDBFReader(InputStream inputStream) throws IOException {
        return new DBFReaderImpl(inputStream);
    }
}
