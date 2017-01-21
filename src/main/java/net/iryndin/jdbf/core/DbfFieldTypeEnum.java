package net.iryndin.jdbf.core;

public enum DbfFieldTypeEnum {
    Character('C'),
    Currency('Y'),
    Numeric('N'),
    Float('F'),
    Date('D'),
    /**
     * @deprecated FoxPro-specific extension. Use Timestamp/@ with dBASE 7 or later
     */
    @Deprecated
    DateTime('T'),
    Timestamp('@'), // dbASE 7 julain date
    /**
     * @deprecated Binary doubles are FoxPro specific dBASE V uses B for binary MEMOs. Use Double7, Float or Numeric instead
     */
    @Deprecated
    Double('B'),
    Double7('O'), // dBASE 7 binary double (standardized in contrast to 'B'
    Integer('I'),
    Logical('L'),
    Memo('M'),
    General('G'),
    Picture('P'),
    NullFlags('0');

    final char type;

    DbfFieldTypeEnum(char type) {
        this.type = type;
    }

    public static DbfFieldTypeEnum fromChar(char type) {
        for (DbfFieldTypeEnum e : DbfFieldTypeEnum.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return null;
    }

    public byte toByte() {
        return (byte) type;
    }

    public char getType() {
        return type;
    }
}
