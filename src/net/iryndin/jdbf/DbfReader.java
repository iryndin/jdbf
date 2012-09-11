package net.iryndin.jdbf;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public interface DbfReader {
	List<? extends DbfRecord> read(File file) throws Exception;
	List<? extends DbfRecord> read(File file, Charset stringsCharset) throws Exception;
}
