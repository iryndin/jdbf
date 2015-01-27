package net.iryndin.jdbf.impl;

import net.iryndin.jdbf.api.IDBFMemoMetadata;
import net.iryndin.jdbf.api.IDBFMemoReader;
import net.iryndin.jdbf.api.IDBFMemoRecord;
import net.iryndin.jdbf.core.MemoRecordTypeEnum;
import net.iryndin.jdbf.util.BitUtils;

import java.io.IOException;
import java.io.InputStream;

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
public class DBFMemoReaderImpl implements IDBFMemoReader {

    public static final int MEMO_RECORD_HEADER_LENGTH = 8;

    private final InputStream inputStream;
    private final IDBFMemoMetadata metadata;
    private int readBytesCount = 0;

    public DBFMemoReaderImpl(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.metadata = readHeaderAndFields();
    }

    private IDBFMemoMetadata readHeaderAndFields() throws IOException {
        DBFMemoMetadataReader metadataReader = new DBFMemoMetadataReader(inputStream);
        IDBFMemoMetadata md  = metadataReader.readMetadata();
        this.readBytesCount = metadataReader.getReadBytesCount();
        return md;
    }

    @Override
    public IDBFMemoMetadata getMetadata() {
        return metadata;
    }

    @Override
    public IDBFMemoRecord read(final int offsetInBlocks) throws IOException {
        int totalOffset = metadata.getBlockSize()*offsetInBlocks;
        readBytesCount += inputStream.skip(totalOffset - readBytesCount);
        byte[] recordHeader = new byte[MEMO_RECORD_HEADER_LENGTH];
        readBytesCount += inputStream.read(recordHeader);

        int type = BitUtils.makeInt(recordHeader[3],recordHeader[2],recordHeader[1],recordHeader[0]);
        final MemoRecordTypeEnum memoRecordType = MemoRecordTypeEnum.fromInt(type);
        final int memoRecordLength = BitUtils.makeInt(recordHeader[7], recordHeader[6], recordHeader[5], recordHeader[4]);
        final byte[] memoRecordBody = new byte[memoRecordLength];
        readBytesCount += inputStream.read(memoRecordBody);

        return new IDBFMemoRecord() {

            @Override
            public byte[] getBytes() {
                return memoRecordBody;
            }

            @Override
            public int getLength() {
                return memoRecordLength;
            }

            @Override
            public MemoRecordTypeEnum getMemoType() {
                return memoRecordType;
            }

            @Override
            public int getBlockSize() {
                return metadata.getBlockSize();
            }

            @Override
            public int getOffsetInBlocks() {
                return offsetInBlocks;
            }
        };
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
