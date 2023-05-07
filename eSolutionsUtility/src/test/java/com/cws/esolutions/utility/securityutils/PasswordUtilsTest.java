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
 * File: PasswordUtilsTest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;
/**
 * @author khuntly
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PasswordUtilsTest
{
    @Test public void testTwoWayEncryption()
    {
    	// U1Y5RkFIdzZ3VXZBdW9DRHlyM0syZz09OmUvR2szWDJvdHVoWmhqZTZPNU9MTVFWeXkrWUpETTRwOVA1WWplRnpaWU09
    	
        final String plainText = "ANIBbuKHiGkyGANLOjawFZ9cZGXuCVRd";
        final String salt = "fc50b7f9765e846f14d8ce9818d710c5dd25b643b6fa02863f1567e483aa399d704788cdb6db31c7a67ae6f02c2080f72214d8cdf7af4c2732b11040b2a84eff";

        try
        {
        	String encr = PasswordUtils.encryptText(plainText.toCharArray(), salt, "PBKDF2WithHmacSHA512", 600000, 256, "UTF-8");

        	System.out.println(encr);
        }
        catch (final Exception sx)
        {
            Assertions.fail(sx.getMessage());
        }
    }
}