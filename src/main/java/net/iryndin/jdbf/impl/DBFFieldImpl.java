package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFField;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;

/**
 *
 */
public class DBFFieldImpl implements IDBFField {

    private final String name;
    private final DbfFieldTypeEnum type;
    private final int length;
    private final int numberOfDecimalPlaces;

    public DBFFieldImpl(String name, DbfFieldTypeEnum type, int length, int numberOfDecimalPlaces) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return type;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return numberOfDecimalPlaces;
    }
}
