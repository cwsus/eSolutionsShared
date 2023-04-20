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
 * File: SecurityConfigurationData.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.StringUtils;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;

import com.cws.esolutions.security.SecurityServicesConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.NONE)
public final class SecurityConfigurationData
{
    private SystemConfig systemConfig = null;
    private SecurityConfig securityConfig = null;
    private ResourceConfig resourceConfig = null;

    private static final String CNAME = SecurityConfigurationData.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setSystemConfig(final SystemConfig value)
    {
        final String methodName = SecurityConfigurationData.CNAME + "#setSystemConfig(final SystemConfig value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }
        
        this.systemConfig = value;
    }

    public final void setSecurityConfig(final SecurityConfig value)
    {
        final String methodName = SecurityConfigurationData.CNAME + "#setSecurityConfig(final SecurityConfig value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.securityConfig = value;
    }

    public final void setResourceConfig(final ResourceConfig value)
    {
        final String methodName = SecurityConfigurationData.CNAME + "#setResourceConfig(final ResourceConfig value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.resourceConfig = value;
    }

    @XmlElement(name = "system-config")
    public final SystemConfig getSystemConfig()
    {
        final String methodName = SecurityConfigurationData.CNAME + "#getSystemConfig()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.systemConfig);
        }

        return this.systemConfig;
    }

    @XmlElement(name = "security-config")
    public final SecurityConfig getSecurityConfig()
    {
        final String methodName = SecurityConfigurationData.CNAME + "#getSecurityConfig()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.securityConfig);
        }

        return this.securityConfig;
    }

    @XmlElement(name = "resource-config")
    public final ResourceConfig getResourceConfig()
    {
        final String methodName = SecurityConfigurationData.CNAME + "#getResourceConfig()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.resourceConfig);
        }

        return this.resourceConfig;
    }

    public static final String expandEnvVars(final String value)
    {
        final String methodName = SecurityConfigurationData.CNAME + "#expandEnvVars(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        String returnValue = null;

        if (!(StringUtils.contains(value, "$")))
        {
            return null;
        }

        final Properties sysProps = System.getProperties();
        final Map<String, String> envMap = System.getenv();
        final String text = StringUtils.replaceEachRepeatedly(value.split("=")[1].trim(), new String[] {"${", "}" }, new String[] { "", "" }).trim();

        if (DEBUG)
        {
            DEBUGGER.debug("Properties sysProps: {}", sysProps);
            DEBUGGER.debug("Map<String, String> envMap: {}", envMap);
            DEBUGGER.debug("String text: {}", text);
        }

        for (Entry<Object, Object> property : sysProps.entrySet())
        {
            if (DEBUG)
            {
                DEBUGGER.debug("Entry<Object, Object> property: {}", property);
            }

            String key = (String) property.getKey();

            if (DEBUG)
            {
                DEBUGGER.debug("String key: {}", key);
            }

            if (StringUtils.equals(key.trim(), text))
            {
                returnValue = sysProps.getProperty(key.trim());

                break;
            }
        }

        for (Entry<String, String> entry : envMap.entrySet())
        {
            if (DEBUG)
            {
                DEBUGGER.debug("Entry<String, String> entry: {}", entry);
            }

            String key = entry.getKey();

            if (DEBUG)
            {
                DEBUGGER.debug("String key: {}", key);
            }

            if (StringUtils.equals(key.trim(), text))
            {
                returnValue = entry.getValue();

                break;
            }
        }

        if (DEBUG)
        {
            DEBUGGER.debug("String returnValue: {}", returnValue);
        }

        return returnValue;
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
