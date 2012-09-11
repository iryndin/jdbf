package net.iryndin.jdbf;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public class DbfWriterImpl implements DbfWriter {

	private Charset stringsCharset;
	
	@Override
	public void write(OutputStream out, List<? extends DbfRecord> records)
			throws Exception {
		write(out, records, Charset.defaultCharset());
	}

	@Override
	public void write(OutputStream out, List<? extends DbfRecord> records,
			Charset stringsCharset) throws Exception {
		this.stringsCharset = stringsCharset;
		DbfRecord rec = records.get(0);
		writeHeader(out, records.size(), rec);
		writeFields(out, rec);
		writeData(out, records);
	}

	private void writeData(OutputStream out, List<? extends DbfRecord> records) throws IOException {
		for (DbfRecord rec : records) {
			writeOneRecord(out, rec);
		}		
	}

	private void writeOneRecord(OutputStream out, DbfRecord rec) throws IOException {
		List<DbfField> fields = rec.getFields();
		out.write(0x20);
		for (DbfField f : fields) {
			int length = f.getLength();
			byte[] b = new byte[length];
			BitUtils.memset(b, 0x20);
			String s = rec.getStringRep(f.getName());
			if (s != null) {
				byte[] sbytes = s.getBytes(stringsCharset);
				System.arraycopy(sbytes, 0, b, 0, sbytes.length);
			}
			out.write(b);
		}
	}

	private void writeFields(OutputStream out, DbfRecord rec) throws IOException {
		byte[] fieldBytes = new byte[32];
		for (DbfField field : rec.getFields()) {
			System.out.println(field);
			BitUtils.memset(fieldBytes, 0);
			byte[] nameBytes = field.getName().getBytes();
			System.arraycopy(nameBytes, 0, fieldBytes, 0, nameBytes.length);
			fieldBytes[11] = field.getType().toByte();
			int length = field.getLength();
			fieldBytes[16] = (byte)(length & 0xff);
			fieldBytes[17] = (byte)(field.getNumberOfDecimalPlaces() & 0xff);
			out.write(fieldBytes);
		}	
		// write terminator
		out.write(0xD);
	}

	private void writeHeader(OutputStream out, int recordsQty, DbfRecord rec) throws IOException {
		DbfHeader header = new DbfHeader();
		header.setType(DbfFileTypeEnum.FoxBASEPlus1);
		header.setRecordsQty(recordsQty);
		int fullHeaderLength = calculateFullHeaderLength(rec.getFields());
		header.setFullHeaderLength(fullHeaderLength);
		int oneRecordLength = calculateOneRecordLength(rec.getFields());
		header.setOneRecordLength(oneRecordLength);
		
		byte[] bytes = header.toByteArray();
		out.write(bytes);
		BitUtils.memset(bytes,0);
		out.write(bytes);
	}

	private int calculateOneRecordLength(List<DbfField> fields) {
		int result = 0;
		for (DbfField field : fields) {
			result += field.getLength();
		}			
		result++;
		return result;
	}

	private int calculateFullHeaderLength(List<DbfField> fields) {
		int result = 32;
		result += 32*fields.size();
		result++;
		return result;
	}
}
