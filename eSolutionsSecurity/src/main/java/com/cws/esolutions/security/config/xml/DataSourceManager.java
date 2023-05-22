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
package com.cws.esolutions.security.config.xml;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.config.xml
 * File: DataSourceManager.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import jakarta.xml.bind.annotation.XmlType;
import org.apache.logging.log4j.LogManager;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import com.cws.esolutions.security.SecurityServicesConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
@XmlType(name = "DataSourceManager")
@XmlAccessorType(XmlAccessType.NONE)
public final class DataSourceManager
{
    private String dsName = null;
    private String driver = null;
    private String dsUser = null;
    private String dsPass = null;
    private String datasource = null;
    private int socketTimeout = 10000;
    private int connectTimeout = 10000;

    private static final String CNAME = DataSourceManager.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setDsName(final String value)
    {
        final String methodName = DataSourceManager.CNAME + "#setDsName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.dsName = value;
    }

    public final void setDataSource(final String value)
    {
        final String methodName = DataSourceManager.CNAME + "#setDataSource(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.datasource = value;
    }

    public final void setDriver(final String value)
    {
        final String methodName = DataSourceManager.CNAME + "#setDriver(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.driver = value;
    }

    public final void setDsUser(final String value)
    {
        final String methodName = DataSourceManager.CNAME + "#setDsUser(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.dsUser = value;
    }

    public final void setDsPass(final String value)
    {
        this.dsPass = value;
    }

    public final void setConnectTimeout(final int value)
    {
        final String methodName = DataSourceManager.CNAME + "#setSalt(final int value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.connectTimeout = value;
    }

    public final void setSocketTimeout(final int value)
    {
        final String methodName = DataSourceManager.CNAME + "#setSocketTimeout(final int value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.socketTimeout = value;
    }

    @XmlElement(name = "dsName")
    public final String getDsName()
    {
        final String methodName = DataSourceManager.CNAME + "#getDsName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.dsName);
        }

        return this.dsName;
    }

    @XmlElement(name = "datasource")
    public final String getDataSource()
    {
        final String methodName = DataSourceManager.CNAME + "#getDataSource()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.datasource);
        }

        return this.datasource;
    }

    @XmlElement(name = "driver")
    public final String getDriver()
    {
        final String methodName = DataSourceManager.CNAME + "#getDriver()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.driver);
        }

        return this.driver;
    }

    @XmlElement(name = "dsUser")
    public final String getDsUser()
    {
        final String methodName = DataSourceManager.CNAME + "#getDsUser()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.dsUser);
        }

        return this.dsUser;
    }

    @XmlElement(name = "dsPass")
    public final String getDsPass()
    {
        return this.dsPass;
    }

    @XmlElement(name = "connectTimeout")
    public final int getConnectTimeout()
    {
        final String methodName = DataSourceManager.CNAME + "#getConnectTimeout()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.connectTimeout);
        }

        return this.connectTimeout;
    }

    @XmlElement(name = "socketTimeout")
    public final int getSocketTimeout()
    {
        final String methodName = DataSourceManager.CNAME + "#getSocketTimeout()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.socketTimeout);
        }

        return this.socketTimeout;
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
                    (!(field.getName().equals("serialVersionUID"))) &&
                    (!(field.getName().equals("dsPass"))))
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
