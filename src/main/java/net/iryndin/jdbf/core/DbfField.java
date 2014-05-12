package net.iryndin.jdbf.core;


public class DbfField {
	
	private String name;
	private DbfFieldTypeEnum type;
	private int length;	
	private int numberOfDecimalPlaces;
	private int offset;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DbfFieldTypeEnum getType() {
		return type;
	}
	public void setType(DbfFieldTypeEnum type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getNumberOfDecimalPlaces() {
		return numberOfDecimalPlaces;
	}
	public void setNumberOfDecimalPlaces(int numberOfDecimalPlaces) {
		this.numberOfDecimalPlaces = numberOfDecimalPlaces;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	@Override
	public String toString() {
		return "DbfField [\n  name=" + name + ", \n  type=" + type
				+ ", \n  length=" + length + ", \n  numberOfDecimalPlaces="
				+ numberOfDecimalPlaces + ", \n  offset=" + offset + "\n]";
	}	
	
	public String getStringRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(",");
		sb.append(type.getType()).append(",");
		sb.append(length).append(",");
		sb.append(numberOfDecimalPlaces);
		return sb.toString();
	}
	
	public static DbfField fromStringRepresentation(String s) {
		String[] a = s.split(",");
		
		DbfField f = new DbfField();
		f.setName(a[0]);
		f.setType(DbfFieldTypeEnum.fromChar(a[1].charAt(0)));
		f.setLength(Integer.parseInt(a[2]));
		f.setNumberOfDecimalPlaces(Integer.parseInt(a[3]));
		return f;
	}
	
	/*
	
	public DbfField(byte[] fieldBytes) {
		if (fieldBytes.length < FIELD_RECORD_LENGTH) {
			throw new IllegalArgumentException("fieldBytes length is less than " + FIELD_RECORD_LENGTH);
		} else {
			this.fieldBytes = new byte[FIELD_RECORD_LENGTH];
			System.arraycopy(fieldBytes, 0, this.fieldBytes, 0, FIELD_RECORD_LENGTH);
			init();
		}
	}	
	
	
	  //Look at
	  //http://www.autopark.ru/ASBProgrammerGuide/DBFSTRUC.HTM	 
	@SuppressWarnings("deprecation")
	private void init() {
		int i = 0;
		for (i=0; i<11 && fieldBytes[i]>0; i++);
		this.name = new String(fieldBytes, 0, i);
		this.type = DbfFieldTypeEnum.fromChar((char)fieldBytes[11]);
		this.length = fieldBytes[16];
		if (this.length < 0) {
			this.length = 256+this.length;
		}
		this.numberOfDecimalPlaces = fieldBytes[17];
	}

	@Override
	public String toString() {
		return "DbfField [\n  name=" + name + ", \n  type=" + type
				+ ", \n  length=" + length
				+ ", \n  numberOfDecimalPlaces=" + numberOfDecimalPlaces
				+ "\n]";
	}

	public byte[] getFieldBytes() {
		return fieldBytes;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(DbfFieldTypeEnum type) {
		this.type = type;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public DbfFieldTypeEnum getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public int getNumberOfDecimalPlaces() {
		return numberOfDecimalPlaces;
	}
	
	public void setNumberOfDecimalPlaces(int numberOfDecimalPlaces) {
		this.numberOfDecimalPlaces = numberOfDecimalPlaces;
	}
	
	public String toStringRep() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(",");
		sb.append(type.getType()).append(",");
		sb.append(length).append(",");
		sb.append(numberOfDecimalPlaces);
		return sb.toString();
	}
	
	public static DbfField fromStringRep(String s) {
		String[] a = s.split(",");
		
		DbfField f = new DbfField();
		f.setName(a[0]);
		f.setType(DbfFieldTypeEnum.fromChar(a[1].charAt(0)));
		f.setLength(Integer.parseInt(a[2]));
		f.setNumberOfDecimalPlaces(Integer.parseInt(a[3]));
		return f;
	}
	*/	
}
