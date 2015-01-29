package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.impl.DBFReaderImpl;
import net.iryndin.jdbf.impl.DBFReaderWithMemo;

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

    /**
     * Factory method that creates IDBFReader
     *
     * @param dbfStream InputStream that holds DBF file
     * @param memoStream InputStream that holds memo file
     * @return IDBFReader implementation
     * @throws IOException if IO errors occur
     */
    public static IDBFReader createDBFReader(InputStream dbfStream, InputStream memoStream) throws IOException {
        return new DBFReaderWithMemo(dbfStream, memoStream);
    }

    /**
     * Factory method that creates IDBFReader
     *
     * @param dbfFile DBF file
     * @param memoFile DBF file
     * @return IDBFReader implementation
     * @throws IOException if IO errors occur
     */
    public static IDBFReader createDBFReader(File dbfFile, File memoFile) throws IOException {
        return createDBFReader(new FileInputStream(dbfFile), new FileInputStream(memoFile));
    }

    /**
     * Factory method that creates IDBFReader
     *
     * @param dbfPath path to DBF file
     * @param memoPath path to DBF file
     * @return IDBFReader implementation
     * @throws IOException if IO errors occur
     */
    public static IDBFReader createDBFReader(String dbfPath, String memoPath) throws IOException {
        return createDBFReader(new File(dbfPath), new File(memoPath));
    }
}
