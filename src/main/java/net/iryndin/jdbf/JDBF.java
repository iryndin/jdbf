package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.impl.DBFReaderImpl;

import java.io.*;

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

    /**
     * Factory method that creates IDBFReader
     *
     * @param file DBF file
     * @return IDBFReader implementation
     * @throws IOException if IO errors occur
     */
    public static IDBFReader createDBFReader(File file) throws IOException {
        return createDBFReader(new FileInputStream(file));
    }

    /**
     * Factory method that creates IDBFReader
     *
     * @param path path to DBF file
     * @return IDBFReader implementation
     * @throws IOException if IO errors occur
     */
    public static IDBFReader createDBFReader(String path) throws IOException {
        return createDBFReader(new File(path));
    }
}
