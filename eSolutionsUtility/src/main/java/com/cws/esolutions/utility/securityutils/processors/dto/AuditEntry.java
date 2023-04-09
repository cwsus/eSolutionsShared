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
package com.cws.esolutions.utility.securityutils.processors.dto;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.processors.dto
 * File: AuditEntry.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import java.util.Date;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.utility.UtilityConstants;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class AuditEntry implements Serializable
{
	private Date auditDate = null;
	private String userName = null;
	private String userGuid = null;
	private String userRole = null;
	private String sessionId = null;
    private Boolean authorized = false;
    private AuditType auditType = null;
    private String applicationId = null;
    private List<String> hostInfo = null;
    private String applicationName = null;
    private List<String> accountInfo = null;

    private static final String CNAME = AuditEntry.class.getName();
    private static final long serialVersionUID = 6162424573063066481L;

    private static final Logger DEBUGGER = LogManager.getLogger(UtilityConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setUserName(final String value)
    {
        final String methodName = AuditEntry.CNAME + "#setUserAccount(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userName = value;
    }

    public final void setUserGuid(final String value)
    {
        final String methodName = AuditEntry.CNAME + "#setUserGuid(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userGuid = value;
    }

    public final void setUserRole(final String value)
    {
        final String methodName = AuditEntry.CNAME + "#setUserRole(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userRole = value;
    }

    public final void setSessionId(final String value)
    {
        final String methodName = AuditEntry.CNAME + "#setSessionId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.sessionId = value;
    }

    public final void setApplicationName(final String value)
    {
        final String methodName = AuditEntry.CNAME + "#setApplicationName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.applicationName = value;
    }

    public final void setApplicationId(final String value)
    {
        final String methodName = AuditEntry.CNAME + "#setApplicationId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.applicationId = value;
    }

    public final void setAuditDate(final Date value)
    {
        final String methodName = AuditEntry.CNAME + "#setAuditDate(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.auditDate = value;
    }

    public final void setAuditType(final AuditType value)
    {
        final String methodName = AuditEntry.CNAME + "#setAuditType(final AuditType value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.auditType = value;
    }

    public final void setAuthorized(final Boolean value)
    {
        final String methodName = AuditEntry.CNAME + "#setAuthorized(final Boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.authorized = value;
    }

    public final void setHostInfo(final List<String> value)
    {
        final String methodName = AuditEntry.CNAME + "#setHostInfo(final List<String[]> value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.hostInfo = value;
    }

    public final void setAccountInfo(final List<String> value)
    {
        final String methodName = AuditEntry.CNAME + "#setAccountInfo(final List<String[]> value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.accountInfo = value;
    }

    public final String getApplicationName()
    {
        final String methodName = AuditEntry.CNAME + "#getApplicationName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.applicationName);
        }

        return this.applicationName;
    }

    public final String getApplicationId()
    {
        final String methodName = AuditEntry.CNAME + "#getApplicationId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.applicationId);
        }

        return this.applicationId;
    }

    public final Date getAuditDate()
    {
        final String methodName = AuditEntry.CNAME + "#getAuditDate()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.auditDate);
        }

        return this.auditDate;
    }

    public final AuditType getAuditType()
    {
        final String methodName = AuditEntry.CNAME + "#getAuditType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.auditType);
        }

        return this.auditType;
    }

    public final Boolean getAuthorized()
    {
        final String methodName = AuditEntry.CNAME + "#getAuthorized()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.authorized);
        }

        return this.authorized;
    }

    public final String getUserName()
    {
        final String methodName = AuditEntry.CNAME + "#getUserAccount()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userName);
        }

        return this.userName;
    }

    public final String getUserGuid()
    {
        final String methodName = AuditEntry.CNAME + "#getUserGuid()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userGuid);
        }

        return this.userGuid;
    }

    public final String getUserRole()
    {
        final String methodName = AuditEntry.CNAME + "#getUserRole()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userRole);
        }

        return this.userRole;
    }

    public final String getSessionId()
    {
        final String methodName = AuditEntry.CNAME + "#getSessionId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.sessionId);
        }

        return this.sessionId;
    }

    public final List<String> getHostInfo()
    {
        final String methodName = AuditEntry.CNAME + "#getHostInfo()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.hostInfo);
        }

        return this.hostInfo;
    }

    public final List<String> getAccountInfo()
    {
        final String methodName = AuditEntry.CNAME + "#getAccountInfo()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.accountInfo);
        }

        return this.accountInfo;
    }

    @Override
    public final String toString()
    {
        StringBuilder sBuilder = new StringBuilder()
                .append("[" + this.getClass().getName() + "]" + UtilityConstants.LINE_BREAK + "{" + UtilityConstants.LINE_BREAK);

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
                        sBuilder.append("\t" + field.getName() + " --> " + field.get(this) + UtilityConstants.LINE_BREAK);
                    }
                }
                catch (final IllegalAccessException iax) {}
            }
        }

        sBuilder.append('}');

        return sBuilder.toString();
    }
}
