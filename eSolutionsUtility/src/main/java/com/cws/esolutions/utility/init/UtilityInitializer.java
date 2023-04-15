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
import com.cws.esolutions.utility.UtilityBean;
import com.cws.esolutions.utility.coreutils.DAOInitializer;
import com.cws.esolutions.utility.exception.UtilityException;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public class UtilityInitializer
{
    private static final UtilityBean bean = UtilityBean.getInstance();

    /**
     * Initializes the security service in a standalone mode - used for applications outside of a container or when
     * run as a standalone jar.
     *
     * @param configFile - The security configuration file to utilize
     * @param logConfig - The logging configuration file to utilize
     * @param startConnections - Configure, load and start repository connections
     * @throws SecurityServiceException @{link com.cws.esolutions.security.exception.SecurityServiceException}
     * if an exception occurs during initialization
     */
    public static void initializeService(final boolean startConnections) throws UtilityException
    {
        try
        {
            if (startConnections)
            {
                DAOInitializer.configureAndCreateAuthConnection(bean);
                DAOInitializer.configureAndCreateAuditConnection(bean);
            }
        }
        catch (final SecurityException sx)
        {
            sx.printStackTrace();
            throw new UtilityException(sx.getMessage(), sx);
        }
    }

    /**
     * Shuts down the running security service process.
     */
    public static void shutdown()
    {
    }
}