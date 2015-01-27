package net.iryndin.jdbf.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by iryndin on 10.12.14.
 */
public interface IDBFRecord {
    Charset getCharset();
    void setCharset(Charset charset);
    int getRecordNumber();
    boolean isDeleted();
    Collection<IDBFField> getFields();
    IDBFField getField(String name);
    byte[] getBytes(String name) throws IOException;
    String getString(String name);
    String getString(String name, Charset charset);
    Date getDate(String name);
    Double getDouble(String name);
    Integer getInt(String name);
    BigDecimal getBigDecimal(String name);
    Boolean getBoolean(String name);
    Map<String, Object> toMap() throws IOException;
}
