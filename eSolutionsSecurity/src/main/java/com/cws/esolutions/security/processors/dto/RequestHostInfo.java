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
 * Package: com.cws.esolutions.security.audit.dto
 * File: RequestHostInfo.java
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

import com.cws.esolutions.utility.UtilityConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class RequestHostInfo implements Serializable
{
	private String hostName = null;
    private String hostAddress = null;

    private static final long serialVersionUID = -8780316582324981901L;
    private static final String CNAME = RequestHostInfo.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(UtilityConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setHostName(final String value)
    {
        final String methodName = RequestHostInfo.CNAME + "#setHostName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug(value);
        }

        this.hostName = value;
    }

    public final void setHostAddress(final String value)
    {
        final String methodName = RequestHostInfo.CNAME + "#setHostAddress(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug(value);
        }

        this.hostAddress = value;
    }

    public final String getHostName()
    {
        final String methodName = RequestHostInfo.CNAME + "#getHostName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug(this.hostName);
        }

        return this.hostName;
    }

    public final String getHostAddress()
    {
        final String methodName = RequestHostInfo.CNAME + "#getHostAddress()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug(this.hostAddress);
        }

        return this.hostAddress;
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
