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
package com.cws.esolutions.core.dao.impl;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.dao.impl
 * File: java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.assertj.core.api.Assertions;

import com.cws.esolutions.security.init.SecurityServicesInitializer;
import com.cws.esolutions.core.dao.interfaces.IApplicationDataDAO;
import com.cws.esolutions.core.init.CoreServicesInitializer;
/**
 * @see com.cws.esolutions.core.dao.interfaces.IApplicationDataDAO
 * @author cws-khuntly
 * @version 1.0
 */
public class ApplicationDataDAOImplTest
{
    private String appGuid = "828f58b4-55f8-473a-9d75-17ae6cad3f6b";
    private String platformGuid = "0e78acfe-eb16-4a40-9e6a-b79a3def9b4c";
    private static final IApplicationDataDAO dao = new ApplicationDataDAOImpl();

    @BeforeAll public void setUp()
    {
        try
        {
            SecurityServicesInitializer.initializeService("SecurityService/config/ServiceConfig.xml", "SecurityService/logging/logging.xml", true);
            CoreServicesInitializer.initializeService("eSolutionsCore/config/ServiceConfig.xml", "eSolutionsCore/logging/logging.xml", true, true);
        }
        catch (final Exception ex)
        {
            Assertions.fail(ex.getMessage());

            System.exit(-1);
        }
    }

    @Test public void addNewApplication()
    {
        List<String> appData = new ArrayList<String>(
                Arrays.asList(
                        this.appGuid,
                        "eSolutions",
                        "1.0",
                        "/appvol/ATS70/eSolutions/eSolutions_web_source-1.0.war",
                        "/nas/installer/eSolutions_web_source-1.0.war",
                        "/nas/installer/runAppInstall.sh",
                        "",
                        "/appvol/ATS70/eSolutions/syslogs/",
                        this.platformGuid));
        try
        {
            dao.addApplication(appData);
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void updateApplication()
    {
        List<String> appData = new ArrayList<String>(
                Arrays.asList(
                        this.appGuid,
                        "eSolutions",
                        "2.0",
                        "/appvol/ATS70/eSolutions/eSolutions_web_source-1.0.war",
                        "/nas/installer/eSolutions_web_source-1.0.war",
                        "/nas/installer/runAppInstall.sh",
                        "",
                        "/appvol/ATS70/eSolutions/syslogs/",
                        this.platformGuid));

        try
        {
            dao.addApplication(appData);
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void listInstalledApplications()
    {
        try
        {
            Assertions.assertThat(dao.listApplications(0)).isNotEmpty();
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void getApplicationData()
    {
        try
        {
            Assertions.assertThat(dao.getApplication(this.appGuid)).isNotEmpty();
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void getApplicationsByAttribute()
    {
        try
        {
            Assertions.assertThat(dao.getApplicationsByAttribute("eSolutions", 0)).isNotEmpty();
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @Test public void deleteApplication()
    {
        try
        {
            dao.removeApplication(this.appGuid);
        }
        catch (final SQLException sqx)
        {
            Assertions.fail(sqx.getMessage());
        }
    }

    @AfterAll public void tearDown()
    {
        CoreServicesInitializer.shutdown();
    }
}
