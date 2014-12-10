package net.iryndin.jdbf.api;

import net.iryndin.jdbf.core.DbfFileTypeEnum;

import java.util.Date;

/**
 *
 */
public interface IDBFHeader {
    DbfFileTypeEnum getType();
    Date getUpdateDate();
    int getRecordsQty();
    int getFullHeaderLength();
    int getOneRecordLength();
    byte getUncompletedTxFlag();
    byte getEcnryptionFlag();
}
