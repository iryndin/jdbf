package net.iryndin.jdbf;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface DbfWriter {
	void write(OutputStream out, List<? extends DbfRecord> records) throws Exception;
	void write(OutputStream out, List<? extends DbfRecord> records, Charset stringsCharset) throws Exception;
}
