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
package com.cws.esolutions.core.processors.impl;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.processors.impl
 * File: ServiceMessagingProcessorImplTest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.assertj.core.api.Assertions;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.listeners.CoreServicesInitializer;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.security.processors.enums.LoginStatus;
import com.cws.esolutions.security.listeners.SecurityServiceInitializer;
import com.cws.esolutions.core.processors.dto.ServiceMessagingRequest;
import com.cws.esolutions.core.processors.exception.MessagingServiceException;
import com.cws.esolutions.core.processors.interfaces.IServiceMessagingProcessor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceMessagingProcessorImplTest
{
    private static UserAccount userAccount = new UserAccount();
    private static RequestHostInfo hostInfo = new RequestHostInfo();

    private static final IServiceMessagingProcessor processor = new ServiceMessagingProcessorImpl();

    @BeforeAll public void setUp() throws Exception
    {
        hostInfo.setHostAddress("junit");
        hostInfo.setHostName("junit");

        userAccount.setStatus(LoginStatus.SUCCESS);
        userAccount.setGuid("f42fb0ba-4d1e-1126-986f-800cd2650000");
        userAccount.setUsername("khuntly");

        try
        {
            SecurityServiceInitializer.initializeService("SecurityService/config/ServiceConfig.xml", "SecurityService/logging/logging.xml", false);
            CoreServicesInitializer.initializeService("eSolutionsCore/config/ServiceConfig.xml", "eSolutionsCore/logging/logging.xml", true, true);
        }
        catch (final Exception ex)
        {
            Assertions.fail(ex.getMessage());

            System.exit(-1);
        }
    }

    @Test public void testAddNewMessage()
    {
        ServiceMessagingRequest request = new ServiceMessagingRequest();
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            processor.addNewMessage(request);
        }
        catch (final MessagingServiceException msx)
        {
            Assertions.fail(msx.getMessage());
        }
    }

    @Test public void testUpdateExistingMessage()
    {
        ServiceMessagingRequest request = new ServiceMessagingRequest();
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            processor.updateExistingMessage(request);
        }
        catch (final MessagingServiceException msx)
        {
            Assertions.fail(msx.getMessage());
        }
    }

    @Test public void testShowMessages()
    {
        ServiceMessagingRequest request = new ServiceMessagingRequest();
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            processor.showMessage(request);
        }
        catch (final MessagingServiceException msx)
        {
            Assertions.fail(msx.getMessage());
        }
    }

    @Test public void testShowAlertMessages()
    {
        ServiceMessagingRequest request = new ServiceMessagingRequest();
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
        processor.showAlertMessages(request);
        }
        catch (final MessagingServiceException msx)
        {
            Assertions.fail(msx.getMessage());
        }
    }

    @Test public void testShowMessage()
    {
        ServiceMessagingRequest request = new ServiceMessagingRequest();
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            processor.showMessage(request);
        }
        catch (final MessagingServiceException msx)
        {
            Assertions.fail(msx.getMessage());
        }
    }

    @AfterAll public void tearDown() throws Exception
    {
        SecurityServiceInitializer.shutdown();
        CoreServicesInitializer.shutdown();
    }
}
