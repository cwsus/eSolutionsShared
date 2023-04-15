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
package com.cws.esolutions.utility;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security
 * File: SecurityServiceBean.java
 *
 * History
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 * Kevin Huntly         12/05/2008 13:36:09             Added method to process change requests
 */
import java.util.Map;
import java.io.Serializable;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class UtilityBean implements Serializable
{
    private Object authDataSource = null;
    private Object auditDataSource = null;
    private Map<String, DataSource> dataSources = null;

    private static UtilityBean instance = null;

    private static final long serialVersionUID = -2801382473833987974L;
    private static final String CNAME = UtilityBean.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(UtilityConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public static final UtilityBean getInstance()
    {
        final String method = CNAME + "#getInstance()";

        if (DEBUG)
        {
            DEBUGGER.debug(method);
            DEBUGGER.debug("instance: {}", UtilityBean.instance);
        }

        if (UtilityBean.instance == null)
        {
            UtilityBean.instance = new UtilityBean();
        }

        return UtilityBean.instance;
    }

    public final void setAuthDataSource(final Object value)
    {
        final String methodName = UtilityBean.CNAME + "#setAuthDataSource(final Object value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.authDataSource = value;
    }

    public final void setAuditDataSource(final Object value)
    {
        final String methodName = UtilityBean.CNAME + "#setAuditDataSource(final Object value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.auditDataSource = value;
    }

    public final void setDataSources(final Map<String, DataSource> value)
    {
        final String methodName = UtilityBean.CNAME + "#setDataSources(final Map<String, DataSource> value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.dataSources = value;
    }

    public final Object getAuthDataSource()
    {
        final String methodName = UtilityBean.CNAME + "#getAuthDataSource()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.authDataSource);
        }

        return this.authDataSource;
    }

    public final Object getAuditDataSource()
    {
        final String methodName = UtilityBean.CNAME + "#getAuditDataSource()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.auditDataSource);
        }

        return this.auditDataSource;
    }

    public final Map<String, DataSource> getDataSources()
    {
        final String methodName = UtilityBean.CNAME + "#getDataSources()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.dataSources);
        }

        return this.dataSources;
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
                    (!(field.getName().equals("instance"))) &&
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
                catch (final IllegalAccessException iax) { }
            }
        }

        sBuilder.append('}');

        return sBuilder.toString();
    }
}