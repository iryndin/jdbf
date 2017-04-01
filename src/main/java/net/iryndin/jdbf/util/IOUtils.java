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

    /**
     * @param in the input stream
     * @param bs a byte array to fill from the input stream
     * @return 0 if b.length == 0 or the number or bytes really read, between 0 and b.length.
     * If b.length != 0, a return value lower than b.length means that the end of the stream was reached.
     * @throws IOException
     */
    public static int readFully(InputStream in, byte[] bs) throws IOException {
        return IOUtils.readFully(in, bs, 0, bs.length);
    }

    /**
     * @param in the input stream
     * @param bs a byte array to fill from the input stream
     * @param offset first byte to write in bs
     * @param len number of bytes to write in bs
     * @return 0 if b.length == 0 or the number or bytes really read, bewteen 0 and b.length.
     * If b.length != 0, a return value lower than b.length means that the end of the stream was reached.
     * @throws IOException
     */
    public static int readFully(InputStream in, byte[] bs, int offset, int len) throws IOException {
        int byteCount = in.read(bs, offset, len);
        if (byteCount <= -1) // length == 0 or EOF
            return 0;

        while (byteCount < len) {
            int ret = in.read(bs, offset + byteCount, len - byteCount);
            if (ret == -1)
                break;
            byteCount += ret;
        }
        return byteCount;
    }
}
