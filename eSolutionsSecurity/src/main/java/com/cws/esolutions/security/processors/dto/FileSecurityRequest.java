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
 * File: FileSecurityRequest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.io.File;
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
public class FileSecurityRequest implements Serializable
{
    private File signedFile = null;
    private File requestFile = null;
    private File unsignedFile = null;
    private File decryptedFile = null;
    private File encryptedFile = null;
    private String applicationId = null;
    private String applicationName = null;
    private UserAccount userAccount = null;
    private RequestHostInfo hostInfo = null;
    private AuthenticationData userSecurity = null;

    private static final long serialVersionUID = 5458528497721677390L;
    private static final String CNAME = FileSecurityRequest.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setHostInfo(final RequestHostInfo value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setBaseDN(final RequestHostInfo value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.hostInfo = value;
    }

    public final void setUserAccount(final UserAccount value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setLoginType(final UserAccount value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userAccount = value;
    }

    public final void setUserSecurity(final AuthenticationData value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setLoginType(final AuthenticationData value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userSecurity = value;
    }

    public final void setApplicationName(final String value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setApplicationName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.applicationName = value;
    }

    public final void setApplicationId(final String value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setApplicationId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.applicationId = value;
    }

    public final void setRequestFile(final File value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setRequestFile(final File value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.requestFile = value;
    }

    public final void setUnsignedFile(final File value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setUnsignedFile(final File value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.unsignedFile = value;
    }

    public final void setSignedFile(final File value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setSignedFile(final File value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.signedFile = value;
    }

    public final void setEncryptedFile(final File value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setEncryptedFile(final File value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.encryptedFile = value;
    }

    public final void setDecryptedFile(final File value)
    {
        final String methodName = FileSecurityRequest.CNAME + "#setDecryptedFile(final File value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.decryptedFile = value;
    }

    public final RequestHostInfo getHostInfo()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getHostInfo()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.hostInfo);
        }

        return this.hostInfo;
    }

    public final UserAccount getUserAccount()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getLoginType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userAccount);
        }

        return this.userAccount;
    }

    public final AuthenticationData getUserSecurity()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getUserSecurity()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userSecurity);
        }

        return this.userSecurity;
    }

    public final String getApplicationName()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getApplicationName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.applicationName);
        }

        return this.applicationName;
    }

    public final String getApplicationId()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getApplicationId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.applicationId);
        }

        return this.applicationId;
    }

    public final File getRequestFile()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getRequestFile()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.requestFile);
        }

        return this.requestFile;
    }

    public final File getUnsignedFile()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getUnsignedFile()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.unsignedFile);
        }

        return this.unsignedFile;
    }

    public final File getSignedFile()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getSignedFile()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.signedFile);
        }

        return this.signedFile;
    }

    public final File getEncryptedFile()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getEncryptedFile()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.encryptedFile);
        }

        return this.encryptedFile;
    }

    public final File getDecryptedFile()
    {
        final String methodName = FileSecurityRequest.CNAME + "#getDecryptedFile()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.decryptedFile);
        }

        return this.decryptedFile;
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
