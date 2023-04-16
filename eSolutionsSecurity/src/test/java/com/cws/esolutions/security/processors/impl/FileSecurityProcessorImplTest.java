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
package com.cws.esolutions.security.processors.impl;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.processors.impl
 * File: FileSecurityProcessorImplTest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.assertj.core.api.Assertions;
import org.apache.commons.io.FileUtils;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
import com.cws.esolutions.security.init.SecurityServiceInitializer;
import com.cws.esolutions.security.processors.enums.LoginStatus;
import com.cws.esolutions.security.processors.dto.FileSecurityRequest;
import com.cws.esolutions.security.processors.dto.FileSecurityResponse;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.security.processors.exception.FileSecurityException;
import com.cws.esolutions.security.processors.interfaces.IFileSecurityProcessor;

public class FileSecurityProcessorImplTest
{
    private static UserAccount userAccount = new UserAccount();
    private static RequestHostInfo hostInfo = new RequestHostInfo();

    private static final IFileSecurityProcessor processor = new FileSecurityProcessorImpl();

    @BeforeAll public void setUp()
    {
        try
        {
            hostInfo.setHostAddress("junit");
            hostInfo.setHostName("junit");

            userAccount.setStatus(LoginStatus.SUCCESS);
            userAccount.setGuid("f42fb0ba-4d1e-1126-986f-800cd2650000");
            userAccount.setUsername("khuntly");

            SecurityServiceInitializer.initializeService("SecurityService/config/ServiceConfig.xml", "SecurityService/logging/logging.xml", true);
        }
        catch (final Exception ex)
        {
            Assertions.fail(ex.getMessage());

            System.exit(-1);
        }
    }

    @Test public void signFile()
    {
        FileSecurityRequest request = new FileSecurityRequest();
        request.setHostInfo(hostInfo);
        request.setUserAccount(userAccount);
        request.setSignedFile(FileUtils.getFile("C:/Temp/myfile.sig"));
        request.setUnsignedFile(FileUtils.getFile("C:/Temp/myfile.txt"));

        try
        {
            FileSecurityResponse response = processor.signFile(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(SecurityRequestStatus.SUCCESS);
        }
        catch (final FileSecurityException fsx)
        {
            Assertions.fail(fsx.getMessage());
        }
    }

    @Test public void verifyFile()
    {
        FileSecurityRequest request = new FileSecurityRequest();
        request.setHostInfo(hostInfo);
        request.setUserAccount(userAccount);
        request.setSignedFile(FileUtils.getFile("C:/Temp/myfile.sig"));
        request.setUnsignedFile(FileUtils.getFile("C:/Temp/myfile.txt"));

        try
        {
            FileSecurityResponse response = processor.verifyFile(request);

            Assertions.assertThat(response.isSignatureValid()).isTrue();
        }
        catch (final FileSecurityException fsx)
        {
            Assertions.fail(fsx.getMessage());
        }
    }

    @Test public void encryptFile()
    {
        FileSecurityRequest request = new FileSecurityRequest();
        request.setHostInfo(hostInfo);
        request.setUserAccount(userAccount);
        request.setDecryptedFile(FileUtils.getFile("C:/Temp/myfile.txt"));
        request.setEncryptedFile(FileUtils.getFile("C:/Temp/myfile.enc"));

        try
        {
            FileSecurityResponse response = processor.encryptFile(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(SecurityRequestStatus.SUCCESS);
        }
        catch (final FileSecurityException fsx)
        {
            Assertions.fail(fsx.getMessage());
        }
    }

    @Test public void decryptFile()
    {
        FileSecurityRequest request = new FileSecurityRequest();
        request.setHostInfo(hostInfo);
        request.setUserAccount(userAccount);
        request.setDecryptedFile(FileUtils.getFile("C:/Temp/myfile.txt"));
        request.setEncryptedFile(FileUtils.getFile("C:/Temp/myfile.enc"));

        try
        {
            FileSecurityResponse response = processor.decryptFile(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(SecurityRequestStatus.SUCCESS);
        }
        catch (final FileSecurityException fsx)
        {
            Assertions.fail(fsx.getMessage());
        }
    }

    @AfterAll public void tearDown()
    {
        SecurityServiceInitializer.shutdown();
    }
}
