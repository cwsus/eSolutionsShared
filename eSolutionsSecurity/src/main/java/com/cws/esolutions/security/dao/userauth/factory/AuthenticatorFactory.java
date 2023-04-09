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
package com.cws.esolutions.security.dao.userauth.factory;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.dao.userauth.factory
 * File: UserSecurityInformationDAOFactory.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.security.SecurityServiceConstants;
import com.cws.esolutions.security.dao.userauth.interfaces.Authenticator;
/**
 * Interface for the Application Data DAO layer. Allows access
 * into the asset management database to obtain, modify and remove
 * application information.
 *
 * @author cws-khuntly
 * @version 1.0
 */
public class AuthenticatorFactory
{
    private static Authenticator authenticator = null;

    private static final String CNAME = AuthenticatorFactory.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServiceConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();
    private static final Logger ERROR_RECORDER = LogManager.getLogger(SecurityServiceConstants.ERROR_LOGGER + CNAME);

    /**
     * Static method to provide a new or existing instance of a
     * {@link com.cws.esolutions.security.dao.userauth.interfaces.Authenticator} singleton
     *
     * @param className - The fully qualified class name to return
     * @return an instance of a {@link com.cws.esolutions.security.dao.userauth.interfaces.Authenticator} singleton
     */
    public static final Authenticator getAuthenticator(final String className)
    {
        final String methodName = CNAME + "#getAuthenticator(final String className)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", className);
        }

        if (authenticator == null)
        {
            try
            {
            	authenticator = (Authenticator) Class.forName(className).getDeclaredConstructor().newInstance();

                if (DEBUG)
                {
                    DEBUGGER.debug("Authenticator: {}", authenticator);
                }
            }
            catch (final InstantiationException ix)
            {
                ERROR_RECORDER.error(ix.getMessage(), ix);
            }
            catch (final IllegalAccessException iax)
            {
                ERROR_RECORDER.error(iax.getMessage(), iax);
            }
            catch (final ClassNotFoundException cnx)
            {
                ERROR_RECORDER.error(cnx.getMessage(), cnx);
            }
            catch (final IllegalArgumentException iax)
            {
                ERROR_RECORDER.error(iax.getMessage(), iax);
            }
            catch (final SecurityException sx)
            {
                ERROR_RECORDER.error(sx.getMessage(), sx);
            }
            catch (InvocationTargetException itx)
            {
            	ERROR_RECORDER.error(itx.getMessage(), itx);
			}
            catch (NoSuchMethodException nsmx)
            {
            	ERROR_RECORDER.error(nsmx.getMessage(), nsmx);
			}
        }

        return authenticator;
    }
}
