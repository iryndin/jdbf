package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFHeader;
import net.iryndin.jdbf.core.DbfFileTypeEnum;

import java.util.Date;

/**
 *
 */
public class DBFHeaderImpl implements IDBFHeader {

    private final DbfFileTypeEnum type;
    private final Date updateDate;
    private final int recordsQty;
    private final int fullHeaderLength;
    private final int oneRecordLength;
    private final byte uncompletedTxFlag;
    private final byte ecnryptionFlag;

    public DBFHeaderImpl(DbfFileTypeEnum type, Date updateDate, int recordsQty, int fullHeaderLength, int oneRecordLength, byte uncompletedTxFlag, byte ecnryptionFlag) {
        this.type = type;
        this.updateDate = updateDate;
        this.recordsQty = recordsQty;
        this.fullHeaderLength = fullHeaderLength;
        this.oneRecordLength = oneRecordLength;
        this.uncompletedTxFlag = uncompletedTxFlag;
        this.ecnryptionFlag = ecnryptionFlag;
    }

    @Override
    public DbfFileTypeEnum getType() {
        return type;
    }

    @Override
    public Date getUpdateDate() {
        return updateDate;
    }

    @Override
    public int getRecordsQty() {
        return recordsQty;
    }

    @Override
    public int getFullHeaderLength() {
        return fullHeaderLength;
    }

    @Override
    public int getOneRecordLength() {
        return oneRecordLength;
    }

    @Override
    public byte getUncompletedTxFlag() {
        return uncompletedTxFlag;
    }

    @Override
    public byte getEcnryptionFlag() {
        return ecnryptionFlag;
    }

    @Override
    public String toString() {
        return "DBFHeaderImpl{" +
                "type=" + type +
                ", updateDate=" + updateDate +
                ", recordsQty=" + recordsQty +
                ", fullHeaderLength=" + fullHeaderLength +
                ", oneRecordLength=" + oneRecordLength +
                ", uncompletedTxFlag=" + uncompletedTxFlag +
                ", ecnryptionFlag=" + ecnryptionFlag +
                '}';
    }
}
