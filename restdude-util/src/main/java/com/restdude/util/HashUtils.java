/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.util;

import com.github.tkawachi.exhash.core.StacktraceHashException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

public class HashUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashUtils.class);

	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	public static String md5Hex(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex(md.digest(message.getBytes("UTF-8")));
		} catch (Exception e) {
			LOGGER.error("Failed to create hash for message \"" + message + "\"", e);
		}
		return null;
    }

    public static String buildHash(Throwable ex) {
        return buildHash(ex, SHA_256, false);
    }

    public static String buildHash(Throwable ex, String algorithm, boolean includeLineNumbers) {
        String hash;
        try {
            com.github.tkawachi.exhash.core.IStacktrace hashStacktrace = com.github.tkawachi.exhash.core.Stacktrace.getInstance(ex, includeLineNumbers);
            com.github.tkawachi.exhash.core.IStacktraceHash h = new com.github.tkawachi.exhash.core.StacktraceHash(algorithm);
            hash = h.hash(hashStacktrace);
        } catch (StacktraceHashException stacktraceHashException) {
            throw new RuntimeException("Error hashing the given exception", stacktraceHashException);
        }
        return hash;
    }


    public static String buildHash(String s) {
        return buildHash(s, SHA_256);
    }

    public static String buildHash(String s, String algorithm) {

        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            hash = md.digest(s.getBytes());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                sb.append(0);
                sb.append(hex.charAt(hex.length() - 1));
            } else {
                sb.append(hex.substring(hex.length() - 2));
            }
        }
        return sb.toString();
    }
}

