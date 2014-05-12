package net.iryndin.jdbf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.iryndin.jdbf.core.DbfField;
import net.iryndin.jdbf.core.DbfFieldTypeEnum;

public class JdbfUtils {
	
	public static int EMPTY = 0x20;
	public static final int FIELD_RECORD_LENGTH = 32;
	public static final int HEADER_TERMINATOR = 0x0D;	
	
	public static ThreadLocal<SimpleDateFormat> dateFormatRef = new ThreadLocal<SimpleDateFormat>();
	
	static {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		dateFormatRef.set(f);
	}
			
	public static List<DbfField> createFieldsFromString(String fieldsString) {
		List<DbfField> list = new ArrayList<DbfField>();
		String[] a = fieldsString.split("\\|");
		for (String b : a) {
			if (b.trim().length() == 0) {
				continue;
			}
			DbfField f = createDbfFieldFromString(b);
			list.add(f);
		}
		return list;
	}
	
	public static DbfField createDbfFieldFromString(String s) {
		String[] a = s.split(",");
		
		DbfField f = new DbfField();
		f.setName(a[0]);
		f.setType(DbfFieldTypeEnum.fromChar(a[1].charAt(0)));
		f.setLength(Integer.parseInt(a[2]));
		f.setNumberOfDecimalPlaces(Integer.parseInt(a[3]));
		
		return f;
	}

	@SuppressWarnings("deprecation")
	public static Date createDate(byte b1, byte b2, byte b3) {
		int year = b1 + 2000 - 1900;
		int month = b2 - 1;
		int day = b3;
		return new Date(year,month,day);
	}
	
	public static byte[] writeDateForHeader(Date date) {
		byte[] headerBytes = {
			(byte)(date.getYear()-100),
			(byte)(date.getMonth()+1),
			(byte)(date.getDay()),
		};
		return headerBytes;
	}
	
	public static byte[] writeDate(Date date) {
		String s = dateFormatRef.get().format(date);		
		return s.getBytes();
	}
	
	public static Date parseDate(String s) throws ParseException {
		return dateFormatRef.get().parse(s);
	}
	
	public static boolean compareMaps(Map<String,Object> m1, Map<String,Object> m2) {
		if (!compareSets(m1.keySet(), m2.keySet())) {
			return false;
		}
		for (String s : m1.keySet()) {
			if (!compareObjects(m1.get(s), m2.get(s))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean compareSets(Set<String> set1, Set<String> set2) {
		if (set1.size() != set2.size()) {
			return false;
		}
		for (String s : set1) {
			if (!set2.contains(s)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean compareObjects(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			if (o2 == null) {
				return false;
			} else {
				return o1.equals(o2);
			}
		}
	}
}
