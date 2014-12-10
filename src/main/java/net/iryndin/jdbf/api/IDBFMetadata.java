package net.iryndin.jdbf.api;

import java.util.Collection;

/**
 *
 */
public interface IDBFMetadata {
    IDBFHeader getHeader();
    Collection<IDBFField> getFields();
    IDBFField getField(String name);
}
