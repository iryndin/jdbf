package net.iryndin.jdbf;

import java.util.Date;

public class DbfHeader {
	public static final int HEADER_LENGTH = 16;
	private byte[] headerBytes;
	
	private DbfFileTypeEnum type;
	private Date updateDate;
	private int recordsQty;
	private int fullHeaderLength;
	private int oneRecordLength;
	private byte uncompletedTxFlag;
	private byte ecnryptionFlag;
	
	public DbfHeader() {}
	
	public DbfHeader(byte[] headerBytes) {
		if (headerBytes.length < HEADER_LENGTH) {
			throw new IllegalArgumentException("headerBytes length is less than " + HEADER_LENGTH);
		} else {
			this.headerBytes = new byte[HEADER_LENGTH];
			System.arraycopy(headerBytes, 0, this.headerBytes, 0, HEADER_LENGTH);
			init();
		}
	}	
	
	/**
	 * Look at
	 * http://www.autopark.ru/ASBProgrammerGuide/DBFSTRUC.HTM
	 */
	@SuppressWarnings("deprecation")
	private void init() {
		this.type = DbfFileTypeEnum.fromInt(headerBytes[0]);
		this.updateDate = new Date(headerBytes[1]+2000-1900,headerBytes[2]-1,headerBytes[3]);
		this.recordsQty = BitUtils.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]);
		this.fullHeaderLength  = BitUtils.makeInt(headerBytes[8], headerBytes[9]);
		this.oneRecordLength   = BitUtils.makeInt(headerBytes[10], headerBytes[11]);
		this.uncompletedTxFlag = headerBytes[14];
		this.ecnryptionFlag    = headerBytes[15];
	}
	
	public static int getHeaderLength() {
		return HEADER_LENGTH;
	}

	public byte[] getHeaderBytes() {
		return headerBytes;
	}
	
	public void setType(DbfFileTypeEnum type) {
		this.type = type;
	}

	public DbfFileTypeEnum getType() {
		return type;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public int getRecordsQty() {
		return recordsQty;
	}

	public int getFullHeaderLength() {
		return fullHeaderLength;
	}

	public int getOneRecordLength() {
		return oneRecordLength;
	}

	public byte getUncompletedTxFlag() {
		return uncompletedTxFlag;
	}

	public byte getEcnryptionFlag() {
		return ecnryptionFlag;
	}
	
	public void setHeaderBytes(byte[] headerBytes) {
		this.headerBytes = headerBytes;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setRecordsQty(int recordsQty) {
		this.recordsQty = recordsQty;
	}

	public void setFullHeaderLength(int fullHeaderLength) {
		this.fullHeaderLength = fullHeaderLength;
	}

	public void setOneRecordLength(int oneRecordLength) {
		this.oneRecordLength = oneRecordLength;
	}

	public void setUncompletedTxFlag(byte uncompletedTxFlag) {
		this.uncompletedTxFlag = uncompletedTxFlag;
	}

	public void setEcnryptionFlag(byte ecnryptionFlag) {
		this.ecnryptionFlag = ecnryptionFlag;
	}

	@Override
	public String toString() {
		return "DbfHeader [\n  type=" + type + ", \n  updateDate=" + updateDate
				+ ", \n  recordsQty=" + recordsQty + ", \n  fullHeaderLength="
				+ fullHeaderLength + ", \n  oneRecordLength=" + oneRecordLength
				+ ", \n  uncompletedTxFlag=" + uncompletedTxFlag
				+ ", \n  ecnryptionFlag=" + ecnryptionFlag + "\n]";
	}
	
	public byte[] toByteArray() {
		if (headerBytes == null) {
			headerBytes = new byte[HEADER_LENGTH];
			
			// type
			//DbfFileTypeEnum type = DbfFileTypeEnum.FoxBASEPlus1;
			headerBytes[0] = type.toByte();
			
			// date
			if (this.updateDate == null) {
				this.updateDate = new Date();
			}
			headerBytes[1] = (byte)(this.updateDate.getYear()-100);
			headerBytes[2] = (byte)(this.updateDate.getMonth()+1);
			headerBytes[3] = (byte)(this.updateDate.getDay());
			
			byte[] b = BitUtils.makeByte4(recordsQty);
			headerBytes[4] = b[0];
			headerBytes[5] = b[1];
			headerBytes[6] = b[2];
			headerBytes[7] = b[3];
			
			b = BitUtils.makeByte2(fullHeaderLength);
			headerBytes[8] = b[0];
			headerBytes[9] = b[1];
			
			b = BitUtils.makeByte2(oneRecordLength);
			headerBytes[10] = b[0];
			headerBytes[11] = b[1];
			
			headerBytes[12]=0;
			headerBytes[13]=0;
			
			headerBytes[14] = this.uncompletedTxFlag;
			headerBytes[15] = this.ecnryptionFlag;			
		}
		return headerBytes;			
	}
}
