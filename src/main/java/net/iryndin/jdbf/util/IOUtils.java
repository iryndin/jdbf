package net.iryndin.jdbf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class IOUtils {

    public static byte[] toByteArray(InputStream input) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024 * 8];
            int count;
            while ((count = input.read(buffer)) > 0) {
                baos.write(buffer, 0, count);
            }

            return baos.toByteArray();
        } finally {
            input.close();
        }
    }
}
