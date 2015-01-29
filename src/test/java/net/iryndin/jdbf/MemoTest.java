package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.api.IDBFRecord;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class MemoTest {

    @Test
    public void test1() throws IOException {
        InputStream dbf = getClass().getClassLoader().getResourceAsStream("memo1/texto.dbf");
        InputStream memo = getClass().getClassLoader().getResourceAsStream("memo1/texto.fpt");

        try (IDBFReader reader = JDBF.createDBFReader(dbf, memo)) {
            System.out.println(reader.getMetadata());
            System.out.println("=================================================");
            for (IDBFRecord rec : reader) {
                System.out.println(rec.getRecordNumber() + ": " + rec.getString("TEXVER")+ ", " + Arrays.toString(rec.getBytes("TEXTEX")));
            }
        }
    }
}
