package net.iryndin.jdbf.reader;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.util.DbfMetadataUtils;

public class DbfReader {
	
	private RandomAccessFile raf;
	private DbfMetadata metadata;
	private byte[] oneRecordBuffer;
	
	public DbfReader(File file) throws IOException {
		this.raf = new RandomAccessFile(file, "r");
		readMetadata();
	}
	
	public DbfMetadata getMetadata() {
		return metadata;
	}

	private void readMetadata() throws IOException {
		metadata = new DbfMetadata();
		readHeader();
		DbfMetadataUtils.readFields(metadata, raf);
		oneRecordBuffer = new byte[metadata.getOneRecordLength()];
	}

	private void readHeader() throws IOException {
		// 1. Allocate buffer
		byte[] bytes = new byte[16];
		// 2. Read 16 bytes
		raf.readFully(bytes);
		// 3. Fill header fields
		DbfMetadataUtils.fillHeaderFields(metadata,bytes);		
		// 4. Read next 16 bytes (for most DBF types these are reserved bytes)
		raf.readFully(bytes);
	}

	public void close() throws IOException {
		raf.close();
	}
	
	public DbfRecord read() throws IOException {
		try {
			raf.readFully(oneRecordBuffer);
			return createDbfRecord();
		} catch (EOFException eofe) {
			return null;
		}
	}

	private DbfRecord createDbfRecord() {
		return new DbfRecord(oneRecordBuffer, metadata);
	}
}
