/*
 * The MIT License Copyright (c) 2014 Krayushkin Konstantin (jangokvk@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.jango.j0util;

import java.security.MessageDigest;

/**
 * Utility class for simplifying {@link java.security.MessageDigest} usage for hashing
 * ({@link #ALGORITHMS}).
 * <br /><br/>
 *
 * http://www.sha1-online.com/
 */
public class SecurityUtil {

    /**
     * Supported hash algorithms in JDK 1.7 are: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512.
     * <br/><br />
     *
     * Seems MD2 already is not supported (at least in Android 4.2)<br />
     * http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
     *
     * @see java.security.MessageDigest
     */
    public static final String[] ALGORITHMS = { "MD2", "MD5", "SHA-1",
            "SHA-256", "SHA-384", "SHA-512" };

    /**
     * Encodes a string with the specified algorithm and returns the result as a string, not a
     * byte array, as {@link java.security.MessageDigest} does.
     *
     * @param text text to encode
     * @param alg  name of algorithm
     * @return hex-string of the encoded text
     *
     * @see java.security.MessageDigest
     * @see #ALGORITHMS
     */
    public static String hash(String text, String alg) {
        try {
            final MessageDigest md = MessageDigest.getInstance(alg);
            md.reset();
            md.update(text.getBytes("iso-8859-1"));

            return convertToHex(md.digest());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method looks through the algorithms in {@link #ALGORITHMS} and chooses supported and
     * the strongest one.
     *
     * You could use this method if the algorithm actually doesn't matter for you - you
     * just want to encode some string. Be aware, that different algorithms produce
     * hashes with different lengths, so if resulting string length is important for you, you
     * should better use {@link #hash(String, String)}.
     *
     * @param text text to encode
     * @return hex-string of the encoded text
     *
     * @see #hash(String, String)
     */
    public static String smartHash(String text) {
        for (int i = ALGORITHMS.length - 1; i >= 0; i--) {
            final String hash = hash(text, ALGORITHMS[i]);
            if (hash != null) return hash;
        }

        return null;
    }

    public static String md5(String text) {
        return hash(text, "MD5");
    }

    public static String sha1(String text) {
        return hash(text, "SHA-1");
    }

    public static String sha512(String text) {
        return hash(text, "SHA-512");
    }

    private static String convertToHex(byte[] hash) {
        final StringBuilder buf = new StringBuilder();
        for (byte i : hash) {
            final String h = Integer.toHexString(0xFF & i);
            if (h.length() == 0) buf.append("00");
            else if (h.length() == 1) {
                buf.append("0");
                buf.append(h);
            } else buf.append(h);
        }

        return buf.toString();
    }

}
