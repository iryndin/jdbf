package net.iryndin.jdbf.core;

public enum MemoRecordTypeEnum {
    IMAGE(0x0),
    TEXT(0x1);

    final int type;

    MemoRecordTypeEnum(int type) {
        this.type= type;
    }

    public static MemoRecordTypeEnum fromInt(int type) {
        for (MemoRecordTypeEnum e : MemoRecordTypeEnum.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return null;
    }
}
