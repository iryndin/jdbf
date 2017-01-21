package net.iryndin.jdbf.core;

public enum DbfFileTypeEnum {
	FoxBASE1(0x02,"FoxBASE"),
	FoxBASEPlus1(0x03,"FoxBASE+/Dbase III plus, no memo"),
	VisualFoxPro1(0x30, "Visual FoxPro"),
	VisualFoxPro2(0x31,"Visual FoxPro, autoincrement enabled"),
	dBASEIV1(0x43,"dBASE IV SQL table files, no memo"),
	dBASEIV2(0x63,"dBASE IV SQL system files, no memo"),
	FoxBASEPlus2(0x83,"FoxBASE+/dBASE III PLUS, with memo"),
	dBASEIV3(0x8B,"dBASE IV with memo"),
	dBASEIV4(0xCB,"dBASE IV SQL table files, with memo"),
	FoxPro2x(0xF5,"FoxPro 2.x (or earlier) with memo"),
	FoxBASE2(0xFB,"FoxBASE"),
	dBASEVII1(0x44,"dBASE VII SQL table files, no memo"),
	dBASEVII2(0x64,"dBASE VII SQL system files, no memo"),
	dBASEIVII3(0x8D,"dBASE VII with memo"),
	dBASEIVII4(0xCD,"dBASE VII SQL table files, with memo"),;
	
	final int type;
	final String description;
	
	DbfFileTypeEnum(int type, String description) {
		this.type= type;
		this.description = description;
	}
	
	public static DbfFileTypeEnum fromInt(byte bType) {
        int iType = 0xFF & bType;
		for (DbfFileTypeEnum e : DbfFileTypeEnum.values()) {
			if (e.type == iType) {
				return e;
			}
		}
		return null;
	}
	
	public byte toByte() {
		return (byte)this.type; 
	}
}
