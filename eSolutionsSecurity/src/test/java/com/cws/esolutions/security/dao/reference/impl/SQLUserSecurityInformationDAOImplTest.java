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
package com.cws.esolutions.security.dao.reference.impl;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.dao.reference.impl
 * File: SQLUserSecurityInformationDAOImplTest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.assertj.core.api.Assertions;
import org.apache.commons.lang3.RandomStringUtils;

import com.cws.esolutions.security.processors.enums.SaltType;
import com.cws.esolutions.security.dao.reference.interfaces.IUserSecurityInformationDAO;
import com.cws.esolutions.security.init.SecurityServicesInitializer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SQLUserSecurityInformationDAOImplTest
{
    private static final String GUID = "99aaefc1-8a2a-4877-bed5-20b73d971e56";
    private static final String resetId = RandomStringUtils.randomAlphanumeric(64);

    private static final IUserSecurityInformationDAO dao = new SQLUserSecurityInformationDAOImpl();

    @BeforeAll public void setUp()
    {
        try
        {
            SecurityServicesInitializer.initializeService("SecurityService/config/ServiceConfig.xml", "SecurityService/logging/logging.xml", true);
        }
        catch (final Exception e)
        {
            Assertions.fail(e.getMessage());
            System.exit(1);
        }
    }

    @Test public void addOrUpdateUserSalt()
    {
    	String saltValue = RandomStringUtils.randomAlphanumeric(64);

        try
        {
			Assertions.assertThat(dao.addOrUpdateUserSalt(SQLUserSecurityInformationDAOImplTest.GUID, saltValue, SaltType.LOGON.name())).isTrue();
		}
        catch (final SQLException sqx)
        {
			Assertions.fail(sqx.getMessage());
		}
    }

    @Test public void getUserSalt()
    {
        try
        {
            Assertions.assertThat(dao.getUserSalt(SQLUserSecurityInformationDAOImplTest.GUID, SaltType.LOGON.name())).isNotEmpty();
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void insertResetData()
    {
        try
        {
        	Assertions.assertThat(dao.insertResetData(SQLUserSecurityInformationDAOImplTest.GUID, resetId)).isTrue();
        }
        catch (final SQLException sqx)
        {
        	Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void getResetData()
    {
        try
        {
        	Assertions.assertThat(dao.getResetData(resetId)).isNotEmpty();
        }
        catch (final SQLException sqx)
        {
        	Assertions.fail(sqx.getMessage());
        }
    }

    @AfterAll public void tearDown()
    {
        SecurityServicesInitializer.shutdown();
    }
}
