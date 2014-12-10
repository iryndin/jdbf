package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.api.IDBFHeader;
import net.iryndin.jdbf.api.IDBFMetadata;

import java.util.*;

/**
 *
 */
public class DBFMetadataImpl implements IDBFMetadata {
    private final IDBFHeader header;
    private final List<IDBFField> fields;
    private final Map<String, IDBFField> fieldMap;

    public DBFMetadataImpl(IDBFHeader header, List<IDBFField> fields) {
        this.header = header;
        this.fields = Collections.unmodifiableList(fields);
        {
            Map<String, IDBFField> map = new LinkedHashMap<>();

            for (IDBFField f : fields) {
                map.put(f.getName(), f);
            }

            this.fieldMap = Collections.unmodifiableMap(map);
        }
    }

    @Override
    public IDBFHeader getHeader() {
        return header;
    }

    @Override
    public Collection<IDBFField> getFields() {
        return fields;
    }

    @Override
    public IDBFField getField(String name) {
        return fieldMap.get(name);
    }
}
