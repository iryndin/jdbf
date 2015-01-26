package net.iryndin.jdbf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class IOUtils {


    public static byte[] urlToBytes(String url) throws IOException {
        return toBytes(new URL(url).openConnection().getInputStream());
    }

    public static byte[] toBytes(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 1024);
        byte[] buf = new byte[64 * 1024];
        int n;
        while ((n = input.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        return baos.toByteArray();
    }
}
