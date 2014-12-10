package net.iryndin.jdbf.api;

import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Created by iryndin on 10.12.14.
 */
public interface IDBFRecord {
    Charset getCharset();
    void setCharset(Charset charset);
    int getRecordNumber();
    boolean isDeleted();
    Collection<IDBFField> getFields();
}
