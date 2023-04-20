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
package com.cws.esolutions.utility.coreutils;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.utils
 * File: DAOInitializer.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.utility.UtilityBean;
import com.cws.esolutions.utility.UtilityConstants;
import com.cws.esolutions.utility.exception.UtilityException;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public final class DAOInitializer
{
    private static final String CNAME = DAOInitializer.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(UtilityConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();
    private static final Logger ERROR_RECORDER = LogManager.getLogger(UtilityConstants.ERROR_LOGGER + DAOInitializer.CNAME);

    /**
     * @param properties - The <code>AuthRepo</code> object containing connection information
     * @param isContainer - A <code>boolean</code> flag indicating if this is in a container
     * @param bean - The {@link com.cws.esolutions.security.SecurityServiceBean} <code>SecurityServiceBean</code> that holds the connection
     * @throws SecurityServiceException {@link com.cws.esolutions.security.exception.SecurityServiceException}
     * if an exception occurs opening the connection
     */
    public synchronized static void configureAndCreateAuthConnection(final UtilityBean bean) throws UtilityException
    {
        String methodName = DAOInitializer.CNAME + "#configureAndCreateAuthConnection(final UtilityBean bean) throws UtilityException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("SecurityServiceBean: {}", bean);
        }

        try
        {
        	Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup(UtilityConstants.DS_CONTEXT);

            bean.setAuthDataSource(envContext.lookup("jdbc/cwssec"));
        }
        catch (final NamingException nx)
        {
        	ERROR_RECORDER.error(nx.getMessage(), nx);

            throw new UtilityException(nx.getMessage(), nx);
        }
    }

    /**
     * @param properties - The <code>AuthRepo</code> object containing connection information
     * @param isContainer - A <code>boolean</code> flag indicating if this is in a container
     * @param bean - The {@link com.cws.esolutions.security.SecurityServiceBean} <code>SecurityServiceBean</code> that holds the connection
     * @throws SecurityServiceException {@link com.cws.esolutions.security.exception.SecurityServiceException}
     * if an exception occurs opening the connection
     */
    public synchronized static void configureAndCreateAuditConnection(final UtilityBean bean) throws UtilityException
    {
        String methodName = DAOInitializer.CNAME + "#configureAndCreateAuditConnection(final SecurityServiceBean bean) throws UtilityException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("SecurityServiceBean: {}", bean);
        }

        try
        {

        	Context initContext = new InitialContext();

            bean.setAuditDataSource(initContext.lookup(UtilityConstants.DS_CONTEXT + "/jdbc/audit"));
        }
        catch (final NamingException nx)
        {
        	ERROR_RECORDER.error(nx.getMessage(), nx);

            throw new UtilityException(nx.getMessage(), nx);
        }
    }
}