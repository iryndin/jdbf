package net.iryndin.jdbf.api;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 *
 */
public interface IDBFReader extends Closeable, Iterable<IDBFRecord>{
    Charset getCharset();
    void setCharset(Charset charset);
    IDBFMetadata getMetadata();
}
