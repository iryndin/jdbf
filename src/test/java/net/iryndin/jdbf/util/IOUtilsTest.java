package net.iryndin.jdbf.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A test class for IOUtils.
 */
public class IOUtilsTest {
    private InputStream in;

    @Before
    public void setUp() {
        byte[] bytes = new byte[10];
        Arrays.fill(bytes, (byte) 15);
        this.in = new ByteArrayInputStream(bytes);
    }

    @Test
    public void readFullyAVoidArray() throws Exception {
        byte[] bs = new byte[0];
        Assert.assertEquals(0, IOUtils.readFully(in, bs));

        byte[] ret = new byte[0];
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFullyASmallArray() throws Exception {
        byte[] bs = new byte[5];
        Assert.assertEquals(5, IOUtils.readFully(in, bs));

        byte[] ret = new byte[5];
        Arrays.fill(ret, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFullyAFullSizeArray() throws Exception {
        byte[] bs = new byte[10];
        Assert.assertEquals(10, IOUtils.readFully(in, bs));

        byte[] ret = new byte[10];
        Arrays.fill(ret, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFullyABigArray() throws Exception {
        byte[] bs = new byte[100];
        Assert.assertEquals(10, IOUtils.readFully(in, bs));

        byte[] ret = new byte[100];
        Arrays.fill(ret, 0, 10, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFullyWithOffsetBigArray() throws Exception {
        byte[] bs = new byte[100];
        Assert.assertEquals(10, IOUtils.readFully(in, bs, 5, 10));

        byte[] ret = new byte[100];
        Arrays.fill(ret, 5, 15, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }
}