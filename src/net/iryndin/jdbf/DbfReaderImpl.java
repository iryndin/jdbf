package net.iryndin.jdbf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbfReaderImpl implements DbfReader {

	private Charset stringCharset;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public List<? extends DbfRecord> read(File file) throws Exception {
		return read(file, Charset.defaultCharset());
				
	}
	
	@Override
	public List<? extends DbfRecord> read(File file, Charset stringCharset) throws Exception {
		this.stringCharset = stringCharset;
		byte[] fileContent = readFileContent(file);
		return parseFileContent(fileContent);
	}

	private List<? extends DbfRecord> parseFileContent(byte[] fileContent) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(fileContent);
		DbfHeader header = readHeader(in);
		System.out.println(header);
		List<DbfField> fields = readFields(in);
		//System.out.println(fields);
		List<DbfRecordImpl> records = readRecords(in,header,fields);
		return records;
	}

	private List<DbfRecordImpl> readRecords(InputStream in,DbfHeader header, List<DbfField> fields) throws Exception {
		int qty = header.getRecordsQty();
		List<DbfRecordImpl> records = new ArrayList<DbfRecordImpl>(qty); 
		for (int i=0; i<qty; i++) {
			DbfRecordImpl rec = readOneRecord(in, fields);
			records.add(rec);
		}
		return records;
	}

	private DbfRecordImpl readOneRecord(InputStream in, List<DbfField> fields) throws Exception {
		DbfRecordImpl record = new DbfRecordImpl(fields);
		// read one byte
		int recordMark = in.read();
		// read record fields
		for (DbfField f : fields) {
			//System.out.println("read field: " + f);
			switch(f.getType()) {
			case Character:
				{
					String s = readCharactes(f.getLength(), in);
					record.setStringValue(f, s);
					break;
				}
			case Numeric:
				{
					BigDecimal n = readNumeric(f.getLength(), in);
					record.setNumberValue(f, n);
					break;
				}
			case Date:
				{
					Date d = readDate(f.getLength(), in);
					record.setDateValue(f, d);
					break;
				}
			case Logical:
				{
					Boolean l = readLogical(f.getLength(), in);
					record.setBooleanValue(f, l);
					break;					
				}
			default: 
				{
					throw new RuntimeException("unknown type:" + f.getType());
					
				}
			}
		}
		//System.out.println(record);
		return record;
	}

	private List<DbfField> readFields(InputStream in) throws Exception {
		List<DbfField> fields = new ArrayList<DbfField>(128);
		byte[] fieldBytes = new byte[32];
		int headerLength = 0;
		int fieldLength = 0;
		while(true) {
			in.read(fieldBytes);
			DbfField field = new DbfField(fieldBytes);
			fields.add(field);
			
			fieldLength += field.getLength();
			headerLength += 32;
			
			in.mark(1);
			int terminator = in.read();
			if (terminator == 0x0D) {
				break;
			} else {
				in.reset();
			}
		}
		// that's one record mark infornt of record
//		fieldLength += 1;
//		headerLength += 32;
//		headerLength += 1;
//		System.out.println("fieldLength="+fieldLength);
//		System.out.println("headerLength="+headerLength);
		return fields;		
	}

	private DbfHeader readHeader(InputStream in) throws IOException {
		byte[] b = new byte[16];
		// read 16 bytes
		in.read(b);
		// create header
		DbfHeader header = new DbfHeader(b);
		// read next 16 bytes (these are reserved bytes)
		in.read(b);
		return header;
	}

	private byte[] readFileContent(File file) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		byte[] fileContent = new byte[(int)file.length()];
		fin.read(fileContent);
		fin.close();
		return fileContent;
	}

	private Date readDate(int length, InputStream in) throws IOException, ParseException {
		String s = readString(length, in);
		if (s != null) {
			return dateFormat.parse(s);
		} else {
			return null;
		}
	}

	private BigDecimal readNumeric(int length, InputStream in) throws IOException {
		String s = readString(length, in);
		if (s != null) {
			//System.out.println("to BigDecimal: " + s);
			return new BigDecimal(s);
		} else {
			return null;
		}
	}
	
	private Boolean readLogical(int length, InputStream in) throws IOException {
		String s = readString(length, in);
		//System.out.println("Logical: " + s);
		if (s != null) {
			if (s.equalsIgnoreCase("f")) {
				return false;
			} else if (s.equalsIgnoreCase("t")) {
				return true;
			}
		} 
		return null;
	}

	private String readCharactes(int length, InputStream in) throws IOException {
		byte[] b = new byte[length];
		in.read(b);
		int i=0;
		String s = new String(b, stringCharset);
		s = s.trim();
		if (s.length() == 0) {
			s = null;
		}
		return s;		
	}
	
	private String readString(int length, InputStream in) throws IOException {
		byte[] b = new byte[length];
		in.read(b);
		String s = new String(b);
		s = s.trim();
		if (s.length()==0) {
			s=null;
		}
		return s;
	}
}
