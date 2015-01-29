package net.iryndin.jdbf;

import net.iryndin.jdbf.api.IDBFReader;
import net.iryndin.jdbf.api.IDBFRecord;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;


public class MiscDbfTest {

    @Test
    public void streetDbf() throws IOException {
        File file = Paths.get(KladrFilesTest.PATH, "TM_WORLD_BORDERS_SIMPL-0.3.dbf").toFile();
        if (!file.exists()) {
            System.out.println("Going to download TM_WORLD_BORDERS_SIMPL-0.3.dbf from http://trac.mapfish.org ");
            byte[] bytes = IOUtils.urlToBytes("http://trac.mapfish.org/trac/mapfish/export/1548/sandbox/camptocamp/MapCat/data/default_datastore/TM_WORLD_BORDERS_SIMPL-0.3.dbf");
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(bytes);
            }
            System.out.println("Download TM_WORLD_BORDERS_SIMPL-0.3.dbf from http://trac.mapfish.org COMPLETED");
        } else {
            System.out.println("Found TM_WORLD_BORDERS_SIMPL-0.3.dbf");
        }
        try (IDBFReader reader = JDBF.createDBFReader(file)) {
            System.out.println(reader.getMetadata());
            System.out.println("=================================================");
            //reader.setCharset(Charset.forName("cp866"));
            for (IDBFRecord rec : reader) {
                System.out.println(rec.getRecordNumber() + ": " + rec.getString("FIPS")+ ", " + rec.getString("ISO2")+ ", " + rec.getString("NAME")+", " + rec.getBigDecimal("LON")+", " + rec.getBigDecimal("LAT"));
            }
        }
    }
}
