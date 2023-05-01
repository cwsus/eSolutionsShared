/*
 * Copyright (c) 2009 - 2020 CaspersBox Web Services
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.esolutions.utility.securityutils;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.utils
 * File: PasswordUtils.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.spec.KeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidKeyException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.RandomStringUtils;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.codec.digest.DigestUtils;
import java.security.InvalidAlgorithmParameterException;
/**
 * Performs password related functions, such as string encryption
 * and (where necessary) decryption, base64 decode/encode.
 *
 * @author cws-khuntly
 * @version 1.0
 */
public final class PasswordUtils
{
	/**
     * Provides an encryption method for given values
     *
     * @param value - The plain text data to encrypt
     * @param salt - The salt value to utilize for the request
     * @param secretInstance - The cryptographic instance to use for the SecretKeyFactory
     * @param iterations - The number of times to loop through the keyspec
     * @param keyBits - The size of the key, in bits
     * @param encoding - The text encoding
     * @return The encrypted string in a reversible format
     * @throws SecurityException {@link java.lang.SecurityException} if an exception occurs during processing
     */
    public static final String encryptText(final char[] value, final String salt, final String secretInstance, final int iterations, final int keyBits, final String encoding) throws SecurityException
    {
        String response = null;

        try
        {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(secretInstance);
            KeySpec keySpec = new PBEKeySpec(value, salt.getBytes(encoding), iterations, keyBits);
            byte[] hashed = keyFactory.generateSecret(keySpec).getEncoded();

            response = DigestUtils.sha512Hex(hashed);
        }
        catch (final NoSuchAlgorithmException nsx)
        {
            throw new SecurityException(nsx.getMessage(), nsx);
        }
        catch (final InvalidKeySpecException iksx)
        {
            throw new SecurityException(iksx.getMessage(), iksx);
        }
        catch (UnsupportedEncodingException uex)
        {
        	throw new SecurityException(uex.getMessage(), uex);
		}

        return response;
    }

    public static final String returnGeneratedSalt(final String generator, final int length)
    {
        String newSalt = null;

		try
		{
			SecureRandom sRandom = SecureRandom.getInstance(generator);
        	byte[] salt = new byte[length];
        	sRandom.nextBytes(salt);

        	newSalt = DigestUtils.sha512Hex(salt);
		}
		catch (NoSuchAlgorithmException nsax)
		{
			newSalt = RandomStringUtils.randomAlphanumeric(length);
		}

		return newSalt;
    }
}