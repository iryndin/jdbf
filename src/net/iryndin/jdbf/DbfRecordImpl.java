package net.iryndin.jdbf;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbfRecordImpl implements DbfRecord {

	private Map<String, FieldValue> fieldMap = new LinkedHashMap<String, FieldValue>();
	private final List<DbfField> fields;
	
	public DbfRecordImpl(List<DbfField> fields) {
		this.fields = fields;
	}
	
	class FieldValue {
		private DbfField fieldData;
		private String stringValue;
		private Date dateValue;
		private BigDecimal numberValue;
		private Boolean booleanValue;
		
		public FieldValue(DbfField fieldData) {
			this.fieldData = fieldData;
		}
		public DbfField getFieldData() {
			return fieldData;
		}
		public void setFieldData(DbfField fieldData) {
			this.fieldData = fieldData;
		}
		public String getStringValue() {
			return stringValue;
		}
		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}
		public Date getDateValue() {
			return dateValue;
		}
		public void setDateValue(Date dateValue) {
			this.dateValue = dateValue;
		}
		public BigDecimal getNumberValue() {
			return numberValue;
		}
		public void setNumberValue(BigDecimal numberValue) {
			this.numberValue = numberValue;
		}
		public Boolean getBooleanValue() {
			return booleanValue;
		}
		public void setBooleanValue(Boolean booleanValue) {
			this.booleanValue = booleanValue;
		}
		
	}
	
	@Override
	public Object getValue(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null && fv.getFieldData() != null) {
			switch (fv.getFieldData().getType()) {
			case Character:
				return fv.getStringValue(); 
			case Date:
				return fv.getDateValue(); 
			case Numeric:
				return fv.getNumberValue();
			case Logical:
				return fv.getBooleanValue();
			default:
				return null;					
			}
		} else {
			return null;
		}
	}

	@Override
	public DbfField getDbfField(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null) {
			return fv.getFieldData();
		} else {
			return null;
		}
	}

	@Override
	public Object getString(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null) {
			return fv.getStringValue();
		} else {
			return null;
		}
	}

	@Override
	public Date getDate(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null) {
			return fv.getDateValue();
		} else {
			return null;
		}
	}

	@Override
	public BigDecimal getNumber(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null) {
			return fv.getNumberValue();
		} else {
			return null;
		}
	}	
	
	@Override
	public Boolean getBoolean(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null) {
			return fv.getBooleanValue();
		} else {
			return null;
		}
	}
	
	private FieldValue getFieldValue(String fieldName) {
		return fieldMap.get(fieldName);
	}

	@Override
	public List<DbfField> getFields() {
		return fields;
	}

	public void setStringValue(DbfField field, String v) {
		FieldValue fv = new FieldValue(field);
		fv.setStringValue(v);
		fieldMap.put(field.getName(), fv);		
	}

	public void setNumberValue(DbfField field, BigDecimal v) {
		FieldValue fv = new FieldValue(field);
		fv.setNumberValue(v);
		fieldMap.put(field.getName(), fv);		
	}

	public void setDateValue(DbfField field, Date v) {
		FieldValue fv = new FieldValue(field);
		fv.setDateValue(v);
		fieldMap.put(field.getName(), fv);	
	}
	
	public void setBooleanValue(DbfField field, Boolean v) {
		FieldValue fv = new FieldValue(field);
		fv.setBooleanValue(v);
		fieldMap.put(field.getName(), fv);	
	}
	
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for (DbfField f : getFields()) {
			String name = f.getName();
			Object val = this.getValue(name);
			sb.append(name).append("=").append(val).append(" | ");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getStringValue();
	}

	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public String getStringRep(String fieldName) {
		FieldValue fv = getFieldValue(fieldName);
		if (fv != null && fv.getFieldData() != null) {
			switch (fv.getFieldData().getType()) {
			case Character:
				return fv.getStringValue(); 
			case Date:
				return (fv.getDateValue() != null ? dateformat.format(fv.getDateValue()) : null);
			case Numeric:
				return (fv.getNumberValue() != null ? fv.getNumberValue().toPlainString() : null);
			case Logical:
				return (fv.getBooleanValue() != null ? (fv.getBooleanValue().booleanValue() ? "T" : "F"): null) ;
			default:
				throw new RuntimeException("unknown type");					
			}
		} else {
			return null;
		}
	}	
}
