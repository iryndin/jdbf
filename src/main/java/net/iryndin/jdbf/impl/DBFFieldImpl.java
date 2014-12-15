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
    private final int offset;

    public DBFFieldImpl(String name, DbfFieldTypeEnum type, int length, int numberOfDecimalPlaces, int offset) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
        this.offset = offset;
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

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "DBFFieldImpl{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", length=" + length +
                ", numberOfDecimalPlaces=" + numberOfDecimalPlaces +
                ", offset=" + offset +
                '}';
    }
}
