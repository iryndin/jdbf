package net.iryndin.jdbf.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import net.iryndin.jdbf.core.DbfField;
import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.util.BitUtils;
import net.iryndin.jdbf.util.DbfMetadataUtils;
import net.iryndin.jdbf.util.JdbfUtils;

public class DbfWriter {
	private OutputStream out;
	private DbfMetadata metadata;
	private Charset stringCharset = Charset.defaultCharset();
	private byte[] recordBuffer;
 
	public DbfWriter(DbfMetadata metadata,OutputStream out) throws IOException {
		this.out = out;
		this.metadata = metadata;
		recordBuffer = new byte[metadata.getOneRecordLength()];
		writeHeaderAndFields();
	}
	
	private void writeHeaderAndFields() throws IOException {
		writeHeader();
		writeFields();
	}

	private void writeHeader() throws IOException {
		byte[] bytes = DbfMetadataUtils.toByteArrayHeader(metadata);
		// assert(bytes.length == 16);
		out.write(bytes);
		BitUtils.memset(bytes, 0);
		out.write(bytes);		
	}
	
	private void writeFields() throws IOException {
		byte[] bytes = new byte[JdbfUtils.FIELD_RECORD_LENGTH];
		for (DbfField f : metadata.getFields()) {
			DbfMetadataUtils.writeDbfField(f, bytes);
			out.write(bytes);
		}
		out.write(JdbfUtils.HEADER_TERMINATOR);
	}
	
	public void write(Map<String, Object> map) throws IOException {
		BitUtils.memset(recordBuffer, JdbfUtils.EMPTY);
		for (DbfField f : metadata.getFields()) {
			Object o = map.get(f.getName());
			writeIntoRecordBuffer(f, o);			
		}
		out.write(recordBuffer);
	}

	private void writeIntoRecordBuffer(DbfField f, Object o) {
		if (o == null) {
			return;
		}
		// TODO: for all methods add length checkings
		// TODO: for all methods add type checkings
		switch (f.getType()) {
		case Character:
			writeString(f, (String)o);
			break;
		case Date:
			writeDate(f, (Date)o);
			break;
		case Logical:
			writeBoolean(f, (Boolean)o);
			break;
		case Numeric:
			writeBigDecimal(f, (BigDecimal)o);
			break;
		case Float:
			if (o instanceof Double)
				writeDouble(f, (Double)o);
			else
				writeFloat(f, (Float)o);
			break;

		// FOXPRO
		case DateTime:
			writeDateTime(f, (Date)o);
			break;
		case Double: // Behaves like dBASE 7 double but uses different column type identifier
			writeDouble7(f, (Double)o);
		/*case Integer: // exactly like dBASE 7
			writeInteger(f, (Integer)o);
			break;*/

		// dBASE 7
		case Timestamp:
			writeTimestamp(f, (Date)o);
			break;
		case Double7:
			writeDouble7(f, (Double)o);
			break;
		case Integer:
			writeInteger(f, (Integer)o);
			break;

		default:
			throw new UnsupportedOperationException("Unknown or unsupported field type " + f.getType().name() + " for " + f.getName());
		}
	}
	
	private void writeBigDecimal(DbfField f, BigDecimal value) {
		String s = value.toPlainString();
		byte[] bytes = s.getBytes();
		if (bytes.length > f.getLength()) {
			byte[] newBytes = new byte[f.getLength()];
			System.arraycopy(bytes, 0, newBytes, 0, f.getLength());
			bytes = newBytes;
		}
		System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
	}

	private void writeBoolean(DbfField f, Boolean value) {
		String s = value.booleanValue() ? "T" : "F";
		byte[] bytes = s.getBytes();
		System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);		
	}

	private void writeDate(DbfField f, Date value) {
		byte[] bytes = JdbfUtils.writeDate(value);
		// TODO: check that bytes.length = f.getLength();
		System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
	}

	private void writeString(DbfField f, String value) {
		byte[] bytes = value.getBytes(stringCharset);
		if (bytes.length > f.getLength()) {
			byte[] newBytes = new byte[f.getLength()];
			System.arraycopy(bytes, 0, newBytes, 0, f.getLength());
			bytes = newBytes;
		}
		System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
	}

	private void writeFloat(DbfField f, Float value) {
		writeDouble(f, value.doubleValue());
	}

	private void writeDouble(DbfField f, Double value) {
		String str = String.format("% 20.18f", value); // Whitespace pad; 20 min length; 18 max precision
		if (str.length() > 20) { // Trim to 20 places, if longer
			str = str.substring(0, 20);
		}
		writeString(f, str);
	}

	private void writeTimestamp(DbfField f, Date d) {
		byte[] bytes = JdbfUtils.writeJulianDate(d);
		System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
	}

	// TODO: Appears to be 64 bit epoch timestamp, but there was no reliable source for that
	private void writeDateTime(DbfField f, Date d) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(d.getTime());
		System.arraycopy(bb.array(), 0, recordBuffer, f.getOffset(), bb.capacity());
	}

	private void writeDouble7(DbfField f, Double d) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putDouble(d);
		System.arraycopy(bb.array(), 0, recordBuffer, f.getOffset(), bb.capacity());
	}

	private void writeInteger(DbfField f, Integer i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		System.arraycopy(bb.array(), 0, recordBuffer, f.getOffset(), bb.capacity());
	}

	public void close() throws IOException {
		this.out.flush();
		this.out.close();
	}
	
	public void setStringCharset(String charsetName) {
		setStringCharset(Charset.forName(charsetName));
	}

	public void setStringCharset(Charset stringCharset) {
		this.stringCharset = stringCharset;		
	}
}
