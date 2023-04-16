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
package com.cws.esolutions.core.processors.dto;
/*
 * Project: eSolutionsAgent
 * Package: com.cws.esolutions.agent.processors.dto
 * File: SystemManagerRequest.java
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

import com.cws.esolutions.core.CoreServicesConstants ;
import com.cws.esolutions.core.processors.enums.StateManagementType;
import com.cws.esolutions.core.processors.enums.ServiceOperationType;
import com.cws.esolutions.core.processors.enums.SystemManagementType;
/**
 * Interface for the Application Data DAO layer. Allows access
 * into the asset management database to obtain, modify and remove
 * application information.
 *
 * @author cws-khuntly
 * @version 1.0
 */
public class SystemManagerRequest implements Serializable
{
    private String processName = null;
    private SystemManagementType mgmtType = null;
    private ServiceOperationType serviceType = null;
    private StateManagementType stateMgmtType = null;

    private static final long serialVersionUID = 2750044850950337356L;
    private static final String CNAME = SystemManagerRequest.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants .DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setMgmtType(final SystemManagementType value)
    {
        final String methodName = SystemManagerRequest.CNAME + "#setMgmtType(final SystemManagementType value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.mgmtType = value;
    }

    public final void setServiceType(final ServiceOperationType value)
    {
        final String methodName = SystemManagerRequest.CNAME + "#setServiceType(final ServiceOperationType value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.serviceType = value;
    }

    public final void setProcessName(final String value)
    {
        final String methodName = SystemManagerRequest.CNAME + "#setProcessName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.processName = value;
    }

    public final void setStateMgmtType(final StateManagementType value)
    {
        final String methodName = SystemManagerRequest.CNAME + "#setStateMgmtType(final StateManagementType value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.stateMgmtType = value;
    }

    public final SystemManagementType getMgmtType()
    {
        final String methodName = SystemManagerRequest.CNAME + "#getMgmtType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.mgmtType);
        }

        return this.mgmtType;
    }

    public final ServiceOperationType getServiceType()
    {
        final String methodName = SystemManagerRequest.CNAME + "#getServiceType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.serviceType);
        }

        return this.serviceType;
    }

    public final String getProcessName()
    {
        final String methodName = SystemManagerRequest.CNAME + "#getProcessName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.processName);
        }

        return this.processName;
    }

    public final StateManagementType getStateMgmtType()
    {
        final String methodName = SystemManagerRequest.CNAME + "#getStateMgmtType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.stateMgmtType);
        }

        return this.stateMgmtType;
    }

    @Override
    public String toString()
    {
        StringBuilder sBuilder = new StringBuilder()
            .append("[" + this.getClass().getName() + "]" + CoreServicesConstants.LINE_BREAK + "{" + CoreServicesConstants.LINE_BREAK);

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
                        sBuilder.append("\t" + field.getName() + " --> " + field.get(this) + CoreServicesConstants.LINE_BREAK);
                    }
                }
                catch (final IllegalAccessException iax) {}
            }
        }

        sBuilder.append('}');

        return sBuilder.toString();
    }
}
