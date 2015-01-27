package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.api.IDBFRecord;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class KladrFilesTest {

    static final String PATH = "/var/jdbf/temp";

    @Test
    public void streetDbf() throws IOException {
        File file = Paths.get(PATH, "street.dbf").toFile();
        if (!file.exists()) {
            System.out.println("Going to download STREET.DBF from GitHub....");
            byte[] bytes = IOUtils.urlToBytes("https://github.com/chezka/kladr/blob/master/STREET.DBF?raw=true");
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(bytes);
            }
            System.out.println("Download STREET.DBF from GitHub COMPLETED");
        } else {
            System.out.println("Found STREET.DBF");
        }
        try (IDBFReader reader = JDBF.createDBFReader(file)) {
            System.out.println(reader.getMetadata());
            System.out.println("=================================================");
            reader.setCharset(Charset.forName("cp866"));
            for (IDBFRecord rec : reader) {
                System.out.println(rec.getRecordNumber() + ": " + rec.getString("NAME"));
            }
        }
    }

    @Test
    public void altnamesDbf() throws IOException {
        File file = Paths.get(PATH, "altnames.dbf").toFile();
        if (!file.exists()) {
            System.out.println("Going to download ALTNAMES.DBF from GitHub....");
            byte[] bytes = IOUtils.urlToBytes("https://github.com/chezka/kladr/blob/master/ALTNAMES.DBF?raw=true");
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(bytes);
            }
            System.out.println("Download ALTNAMES.DBF from GitHub");
        } else {
            System.out.println("Found ALTNAMES.DBF");
        }
        try (IDBFReader reader = JDBF.createDBFReader(file)) {
            System.out.println(reader.getMetadata());
            System.out.println("=================================================");
            for (IDBFRecord rec : reader) {
                System.out.println(rec.getRecordNumber()+": " + rec.getString("OLDCODE") + " --> " + rec.getString("NEWCODE")+", L: " + rec.getString("LEVEL"));
            }
        }
    }
}
