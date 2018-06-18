package net.iryndin.jdbf.reader;

import net.iryndin.jdbf.core.MemoFileHeader;
import net.iryndin.jdbf.core.MemoRecord;
import net.iryndin.jdbf.util.BitUtils;
import net.iryndin.jdbf.util.IOUtils;
import net.iryndin.jdbf.util.JdbfUtils;

import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * Reader of memo files (tested of *.FPT files - Visual FoxPro)
 * See links:
 *
 * Visual FoxPro file formats:
 * http://msdn.microsoft.com/en-us/library/aa977077(v=vs.71).aspx
 *
 * DBase file formats:
 * http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm
 *
 */
public class MemoReader implements Closeable {
    private MemoFileHeader memoHeader;
    private MappedByteBuffer memoByteBuffer;

    public MemoReader(File memoFile) throws IOException {
        this(new FileInputStream(memoFile));
    }

    public MemoReader(FileInputStream inputStream) throws IOException {
        FileChannel channel = inputStream.getChannel();
        this.memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        readMetadata();
    }

    private void readMetadata() throws IOException {
        byte[] headerBytes = new byte[JdbfUtils.MEMO_HEADER_LENGTH];
        try {
            this.memoByteBuffer.get(headerBytes);
        } catch (BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbf file", e);
        }

        this.memoHeader = MemoFileHeader.create(headerBytes);
    }

    @Override
    public void close() throws IOException {
        this.memoByteBuffer = null;
    }

    public MemoFileHeader getMemoHeader() {
        return memoHeader;
    }

    public MemoRecord read(int offsetInBlocks) throws IOException {
        int start = memoHeader.getBlockSize()*offsetInBlocks;
        try {

            byte[] recordHeader = new byte[JdbfUtils.RECORD_HEADER_LENGTH];
            this.memoByteBuffer.position(start);
            this.memoByteBuffer.get(recordHeader);
            int memoRecordLength = BitUtils.makeInt(recordHeader[7], recordHeader[6], recordHeader[5], recordHeader[4]);

            byte[] recordBody = new byte[memoRecordLength];
            this.memoByteBuffer.get(recordBody);
            return new MemoRecord(recordHeader, recordBody, memoHeader.getBlockSize(), offsetInBlocks);
        } catch (BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbf file", e);
        }
    }
}