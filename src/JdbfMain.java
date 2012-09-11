import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.iryndin.jdbf.DbfField;
import net.iryndin.jdbf.DbfFieldTypeEnum;
import net.iryndin.jdbf.DbfHeader;
import net.iryndin.jdbf.DbfReader;
import net.iryndin.jdbf.DbfReaderImpl;
import net.iryndin.jdbf.DbfRecord;
import net.iryndin.jdbf.DbfWriter;
import net.iryndin.jdbf.DbfWriterImpl;


public class JdbfMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//method1(args);
		method2(args);
	}
	
	public static void method2(String[] args) throws Exception {
		DbfReader r = new DbfReaderImpl();
		File file = new File("data/215451/gds_im.dbf");
		//File file = new File("data/215451/tir_im.dbf");
		List<? extends DbfRecord> list = r.read(file, Charset.forName("Cp866"));
		DbfRecord record = null;
		for (DbfRecord rec : list) {
			record = rec;
			System.out.println(rec);
		}
		List<DbfField> fields = record.getFields();
		String s = getStringRep(fields);
		System.out.println(s);
		
		File file2 = new File("1.dbf");
		FileOutputStream out = new FileOutputStream(file2);
		DbfWriter writer = new DbfWriterImpl();
		writer.write(out, list, Charset.forName("Cp866"));
		out.close();
	}
	
	private static String getStringRep(List<DbfField> fields) {
		StringBuilder sb = new StringBuilder(1024*8);
		int i = fields.size();
		for (DbfField f : fields) {
			sb.append(f.toStringRep());
			i--;
			if (i>0) {
				sb.append("|");
			}
		}
		return sb.toString();
	}

	public static void method1(String[] args) throws Exception {
		//Date d = new Date();
		//System.out.println(d.getYear()+1900);
		
		//File file = new File("data/215451/gds_im.dbf");
		File file = new File("data/215451/tir_im.dbf");
		FileInputStream fin = new FileInputStream(file);
		byte[] fileContent = new byte[(int)file.length()];
		fin.read(fileContent);
		fin.close();
		
		
		ByteArrayInputStream in = new ByteArrayInputStream(fileContent);
		
		byte[] b = new byte[16];
		// read 16 byts
		in.read(b);
		// create header
		DbfHeader header = new DbfHeader(b);
		// read next 16 bytes
		in.read(b);
		// read fields
		byte[] fieldBytes = new byte[32];
		List<DbfField> fields = new ArrayList<DbfField>(128); 
		while(true) {
			in.read(fieldBytes);
			in.mark(1);
			int terminator = in.read();
			if (terminator == 0x0D) {
				break;
			} else {
				in.reset();
			}
			DbfField field = new DbfField(fieldBytes);
			fields.add(field);
		}
		
		System.out.println("Total fields: " + fields.size());

		
		Set<DbfFieldTypeEnum> fieldTypes = new HashSet<DbfFieldTypeEnum>(); 
		int bytes = 0;
		for (DbfField fld : fields) {
			System.out.println(fld);
			bytes += fld.getLength();
			
			fieldTypes.add(fld.getType());
		}
		
		System.out.println("Record lengthL: " + bytes);
		System.out.println(header);
		System.out.println(fieldTypes);
		
		for (int i=0; i<header.getRecordsQty(); i++) {
			int recordMark = in.read();
			readOneRecord(in,fields);
			//break;
		}
		
	}

	private static void readOneRecord(ByteArrayInputStream in,
			List<DbfField> fields) throws Exception {
		for (DbfField f : fields) {
			readFieldValue(f,in);
		}
		
	}

	private static void readFieldValue(DbfField f, ByteArrayInputStream in) throws Exception {
		switch(f.getType()) {
		case Character:
			readCharactes(f.getLength(), in);
			break;
		case Numeric:
			readNumeric(f.getLength(), in);
			break;
		case Date:
			readDate(f.getLength(), in);
			break;	
		}
		
	}

	private static void readDate(int length, ByteArrayInputStream in) throws IOException {
		byte[] b = new byte[length];
		in.read(b);
		String s = new String(b);
		s = s.trim();
		if (s.length()==0) {
			s=null;
		}
		System.out.println(s);
	}

	private static void readNumeric(int length, ByteArrayInputStream in) throws IOException {
		byte[] b = new byte[length];
		in.read(b);
		//int i=0;
		//for (i=0; i<length && i != 0x20; i++);
		String s = new String(b);
		s = s.trim();
		if (s.length()==0) {
			s=null;
		}
		System.out.println(s);
	}

	private static void readCharactes(int length, ByteArrayInputStream in) throws IOException {
		byte[] b = new byte[length];
		in.read(b);
		int i=0;
		//for (i=0; i<length && i != 0x20; i++);
		//String s = new String(b, 0, i, Charset.forName("Cp866"));
		String s = new String(b, Charset.forName("Cp866"));
		s = s.trim();
		if (s.length() == 0) {
			s = null;
		}
		
		System.out.println(s);		
	}

}
