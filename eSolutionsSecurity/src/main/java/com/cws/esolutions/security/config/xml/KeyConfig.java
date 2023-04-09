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
 * File: KeyConfig.java
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

import com.cws.esolutions.security.SecurityServiceConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
@XmlType(name = "key-config")
@XmlAccessorType(XmlAccessType.NONE)
public final class KeyConfig
{
    private int keySize = 1024;
    private String keyManager = null;
    private String keyAlgorithm = null;
    private String keyDirectory = null;
    private String signatureAlgorithm = null;
    private String publicKeyAttribute = null;

    private static final String CNAME = KeyConfig.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServiceConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setKeyManager(final String value)
    {
        final String methodName = KeyConfig.CNAME + "#setKeyManager(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.keyManager = value;
    }

    public final void setKeyDirectory(final String value)
    {
        final String methodName = KeyConfig.CNAME + "#setKeyDirectory(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.keyDirectory = value;
    }

    public final void setKeyAlgorithm(final String value)
    {
        final String methodName = KeyConfig.CNAME + "#setKeyAlgorithm(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.keyAlgorithm = value;
    }

    public final void setKeySize(final int value)
    {
        final String methodName = KeyConfig.CNAME + "#setKeySize(final int value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.keySize = value;
    }

    public final void setPublicKeyAttribute(final String value)
    {
        final String methodName = KeyConfig.CNAME + "#setPublicKeyAttribute(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.publicKeyAttribute = value;
    }

    public final void setSignatureAlgorithm(final String value)
    {
        final String methodName = KeyConfig.CNAME + "#setSignatureAlgorithm(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.signatureAlgorithm = value;
    }

    @XmlElement(name = "keyManager")
    public final String getKeyManager()
    {
        final String methodName = KeyConfig.CNAME + "#getKeyManager()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.keyManager);
        }

        return this.keyManager;
    }

    @XmlElement(name = "keyDirectory")
    public final String getKeyDirectory()
    {
        final String methodName = KeyConfig.CNAME + "#getKeyDirectory()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.keyDirectory);
        }

        return this.keyDirectory;
    }

    @XmlElement(name = "keyAlgorithm")
    public final String getKeyAlgorithm()
    {
        final String methodName = KeyConfig.CNAME + "#getKeyAlgorithm()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.keyAlgorithm);
        }

        return this.keyAlgorithm;
    }

    @XmlElement(name = "keySize")
    public final int getKeySize()
    {
        final String methodName = KeyConfig.CNAME + "#getKeySize()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.keySize);
        }

        return this.keySize;
    }

    @XmlElement(name = "publicKeyAttribute")
    public final String getPublicKeyAttribute()
    {
        final String methodName = KeyConfig.CNAME + "#getPublicKeyAttribute()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.publicKeyAttribute);
        }

        return this.publicKeyAttribute;
    }

    @XmlElement(name = "signatureAlgorithm")
    public final String getSignatureAlgorithm()
    {
        final String methodName = KeyConfig.CNAME + "#getSignatureAlgorithm()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.signatureAlgorithm);
        }

        return this.signatureAlgorithm;
    }

    @Override
    public final String toString()
    {
    	StringBuilder sBuilder = new StringBuilder()
            .append("[" + this.getClass().getName() + "]" + SecurityServiceConstants.LINE_BREAK + "{" + SecurityServiceConstants.LINE_BREAK);

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
                        sBuilder.append("\t" + field.getName() + " --> " + field.get(this) + SecurityServiceConstants.LINE_BREAK);
                    }
                }
                catch (final IllegalAccessException iax) {}
            }
        }

        sBuilder.append('}');

        return sBuilder.toString();
    }
}
