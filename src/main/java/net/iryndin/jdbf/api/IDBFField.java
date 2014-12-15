package net.iryndin.jdbf.api;

import net.iryndin.jdbf.core.DbfFieldTypeEnum;

/**
 *
 */
public interface IDBFField {
    String getName();
    DbfFieldTypeEnum getType();
    int getLength();
    int getNumberOfDecimalPlaces();
    int getOffset();
}
