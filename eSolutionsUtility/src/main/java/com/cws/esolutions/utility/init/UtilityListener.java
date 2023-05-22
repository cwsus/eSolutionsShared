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
package com.cws.esolutions.utility.init;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.listeners
 * File: SecurityServiceInitializer.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly           11/23/2008 22:39:20             Created.
 */
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.cws.esolutions.utility.UtilityBean;
import com.cws.esolutions.utility.coreutils.DAOInitializer;
import com.cws.esolutions.utility.exception.UtilityException;
/**
 * @see jakarta.servlet.ServletContextListener
 */
@WebListener
public class UtilityListener implements ServletContextListener
{
    private static final UtilityBean bean = UtilityBean.getInstance();

    /**
     * @see jakarta.servlet.ServletContextListener#contextInitialized(jakarta.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent sContextEvent)
    {
        try
        {
            DAOInitializer.configureAndCreateAuthConnection(bean);
            DAOInitializer.configureAndCreateAuditConnection(bean);
        }
        catch (final UtilityException ux)
        {
        	System.err.println(ux.getMessage());
		}
    }

    /**
     * @see jakarta.servlet.ServletContextListener#contextDestroyed(jakarta.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent sContextEvent)
    {
    }
}