package net.iryndin.jdbf;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface DbfRecord {
	List<DbfField> getFields();
	Object getValue(String fieldName);
	DbfField getDbfField(String fieldName);
	Object getString(String fieldName);
	Date getDate(String fieldName);
	BigDecimal getNumber(String fieldName);
	Boolean getBoolean(String fieldName);
	String getStringRep(String fieldName);
}
