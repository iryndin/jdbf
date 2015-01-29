package net.iryndin.jdbf.util;

import java.nio.charset.Charset;

public class CharsetHelper {

    /**
     * See
     * http://support.microsoft.com/kb/129631/en-us
     * See also:
     * https://msdn.microsoft.com/en-us/library/aa975386(v=vs.71).aspx
     * byte 29 (0x1D) is code page mark
     * See
     * https://msdn.microsoft.com/en-us/library/aa975345(v=vs.71).aspx
     * @param b
     * @return
     */
    public static Charset getCharsetByByte(int b) {
        switch (b) {
            case 0x01: return Charset.forName("437"); // U.S. MS-DOS
            case 0x02: return Charset.forName("850"); // International MS-DOS
            case 0x03: return Charset.forName("windows-1252"); // Windows ANSI
            case 0x04: return Charset.forName("10000"); // Standard Macintosh
            case 0x64: return Charset.forName("852"); // Eastern European MS-DOS
            case 0x65: return Charset.forName("866"); // Russian MS-DOS
            case 0x66: return Charset.forName("865"); // Nordic MS-DOS
            case 0x67: return Charset.forName("861"); // Icelandic MS-DOS
            case 0x68: return Charset.forName("895"); // Kamenicky (Czech) MS-DOS
            case 0x69: return Charset.forName("620"); // Mazovia (Polish) MS-DOS
            case 0x6A: return Charset.forName("737"); // Greek MS-DOS (437G)
            case 0x6B: return Charset.forName("857"); // Turkish MS-DOS
            case 0x78: return Charset.forName("950"); // Chinese (Hong Kong SAR, Taiwan) Windows
            case 0x79: return Charset.forName("949"); // Korean Windows
            case 0x7A: return Charset.forName("936"); // Chinese (PRC, Singapore) Windows
            case 0x7B: return Charset.forName("932"); // Japanese Windows
            case 0x7C: return Charset.forName("874"); // Thai Windows
            case 0x7D: return Charset.forName("1255"); // Hebrew Windows
            case 0x7E: return Charset.forName("1256"); // Arabic Windows
            case 0x96: return Charset.forName("10007"); // Russian Macintosh
            case 0x97: return Charset.forName("10029"); // Macintosh EE
            case 0x98: return Charset.forName("10006"); // Greek Macintosh
            case 0xC8: return Charset.forName("1250"); // Eastern European Windows
            case 0xC9: return Charset.forName("1251"); // Russian Windows
            case 0xCA: return Charset.forName("1254"); // Turkish Windows
            case 0xCB: return Charset.forName("1253"); // Greek Windows
        }
        return null;
    }
}
