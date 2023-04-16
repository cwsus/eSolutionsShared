/*
 *
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
package com.cws.esolutions.core.config.xml;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.config.xml
 * File: ProxyConfig.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.cws.esolutions.core.CoreServicesConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
@XmlType(name = "proxy-config")
@XmlAccessorType(XmlAccessType.NONE)
public final class ProxyConfig
{
    private int proxyServerPort = 0;
    private String proxyUserId = null;
    private String proxyPwdSalt = null;
    private String proxyPassword = null;
    private String proxyAuthType = null;
    private String proxyServerName = null;
    private String proxyAuthDomain = null;
    private String proxyServerRealm = null;
    private boolean proxyAuthRequired = false;
    private boolean proxyServiceRequired = false;

    private static final String CNAME = ProxyConfig.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setProxyServerPort(final int value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyServerPort(final int value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyServerPort = value;
    }

    public final void setProxyUserId(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyUserId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyUserId = value;
    }

    public final void setProxyPwdSalt(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyPwdSalt(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyPwdSalt = value;
    }

    public final void setProxyPassword(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyPassword(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyPassword = value;
    }

    public final void setProxyAuthType(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyAuthType(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyAuthType = value;
    }

    public final void setProxyServerName(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyServerName(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyServerName = value;
    }

    public final void setProxyAuthDomain(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyAuthDomain(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyAuthDomain = value;
    }

    public final void setProxyServerRealm(final String value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyServerRealm(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyServerRealm = value;
    }

    public final void setProxyAuthRequired(final boolean value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyAuthRequired(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyAuthRequired = value;
    }

    public final void setProxyServiceRequired(final boolean value)
    {
        final String methodName = ProxyConfig.CNAME + "#setProxyServiceRequired(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.proxyServiceRequired = value;
    }

    @XmlElement(name = "proxyServerPort")
    public final int getProxyServerPort()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyServerPort()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyServerPort);
        }

        return this.proxyServerPort;
    }

    @XmlElement(name = "proxyUserId")
    public final String getProxyUserId()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyUserId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyUserId);
        }

        return this.proxyUserId;
    }

    @XmlElement(name = "proxyPwdSalt")
    public final String getProxyPwdSalt()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyPwdSalt()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyPwdSalt);
        }

        return this.proxyPwdSalt;
    }

    @XmlElement(name = "proxyPassword")
    public final String getProxyPassword()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyPassword()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyPassword);
        }

        return this.proxyPassword;
    }

    @XmlElement(name = "proxyAuthType")
    public final String getProxyAuthType()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyAuthType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyAuthType);
        }

        return this.proxyAuthType;
    }

    @XmlElement(name = "proxyServerName")
    public final String getProxyServerName()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyServerName()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyServerName);
        }

        return this.proxyServerName;
    }

    @XmlElement(name = "proxyAuthDomain")
    public final String getProxyAuthDomain()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyAuthDomain()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyAuthDomain);
        }

        return this.proxyAuthDomain;
    }

    @XmlElement(name = "proxyServerRealm")
    public final String getProxyServerRealm()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyServerRealm()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyServerRealm);
        }

        return this.proxyServerRealm;
    }

    @XmlElement(name = "proxyAuthRequired")
    public final boolean isProxyAuthRequired()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyAuthRequired()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyAuthRequired);
        }

        return this.proxyAuthRequired;
    }

    @XmlElement(name = "proxyServiceRequired")
    public final boolean isProxyServiceRequired()
    {
        final String methodName = ProxyConfig.CNAME + "#getProxyServiceRequired()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.proxyServiceRequired);
        }

        return this.proxyServiceRequired;
    }

    @Override
    public final String toString()
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
