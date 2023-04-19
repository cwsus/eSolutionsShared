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
package com.cws.esolutions.security.processors.dto;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.processors.dto
 * File: AccountChangeRequest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.SecurityServicesConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class AccountSearchRequest implements Serializable
{
	private String serviceId = null;
	private boolean isReset = false;
	private String searchTerms = null;
    private String applicationId = null;
    private UserAccount requestor = null;
    private String applicationName = null;
    private UserAccount userAccount = null;
    private RequestHostInfo hostInfo = null;

    private static final long serialVersionUID = 2698640275755300472L;
    private static final String CNAME = AccountSearchRequest.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setServiceId(final String value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setServiceId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.serviceId = value;
    }

    public final void setHostInfo(final RequestHostInfo value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setHostInfo(final RequestHostInfo value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.hostInfo = value;
    }

    public final void setUserAccount(final UserAccount value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setUserAccount(final UserAccount value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userAccount = value;
    }

    public final void setApplicationName(final String value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setApplicationName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.applicationName = value;
    }

    public final void setApplicationId(final String value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setApplicationId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.applicationId = value;
    }

    public final void setSearchTerms(final String value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setSearchTerms(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.searchTerms = value;
    }

    public final void setIsReset(final boolean value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setIsReset(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.isReset = value;
    }

    public final void setRequestor(final UserAccount value)
    {
        final String methodName = AccountSearchRequest.CNAME + "#setRequestor(final UserAccount value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.requestor = value;
    }

    public final String getServiceId()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getServiceId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.serviceId);
        }

        return this.serviceId;
    }

    public final RequestHostInfo getHostInfo()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getHostInfo()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.hostInfo);
        }

        return this.hostInfo;
    }

    public final UserAccount getUserAccount()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getUserAccount()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userAccount);
        }

        return this.userAccount;
    }

    public final String getApplicationName()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getApplicationName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.applicationName);
        }

        return this.applicationName;
    }

    public final String getApplicationId()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getApplicationId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.applicationId);
        }

        return this.applicationId;
    }

    public final String getSearchTerms()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getSearchTerms()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.searchTerms);
        }

        return this.searchTerms;
    }

    public final boolean getIsReset()
    {
        final String methodName = AccountSearchRequest.CNAME + "#getIsReset()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isReset);
        }

        return this.isReset;
    }

	public final UserAccount getRequestor()
	{
	    final String methodName = AccountSearchRequest.CNAME + "#getRequestor()";
	
	    if (DEBUG)
	    {
	        DEBUGGER.debug(methodName);
	        DEBUGGER.debug("Value: {}", this.requestor);
	    }
	
	    return this.requestor;
	}

    @Override
    public final String toString()
    {
        StringBuilder sBuilder = new StringBuilder()
            .append("[" + this.getClass().getName() + "]" + SecurityServicesConstants.LINE_BREAK + "{" + SecurityServicesConstants.LINE_BREAK);

        for (Field field : this.getClass().getDeclaredFields())
        {
            if (!(field.getName().equals("methodName")) &&
                    (!(field.getName().equals("CNAME"))) &&
                    (!(field.getName().equals("DEBUGGER"))) &&
                    (!(field.getName().equals("DEBUG"))) &&
                    (!(field.getName().equals("ERROR_RECORDER"))) &&
                    (!(field.getName().equals("serialVersionUID"))))
            {
                try
                {
                    if (field.get(this) != null)
                    {
                        sBuilder.append("\t" + field.getName() + " --> " + field.get(this) + SecurityServicesConstants.LINE_BREAK);
                    }
                }
                catch (final IllegalAccessException iax) {}
            }
        }

        sBuilder.append('}');

        return sBuilder.toString();
    }
}
