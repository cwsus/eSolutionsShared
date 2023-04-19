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
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.processors.dto
 * File: ServiceMessage.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Date;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.CoreServicesConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class ServiceMessage implements Serializable
{
    private Date expiryDate = null;
    private Date submitDate = null;
    private boolean isAlert = false;
    private String messageId = null;
    private boolean isActive = false;
    private String messageText = null;
    private boolean doesExpire = false;
    private String messageTitle = null;
    private boolean isNewMessage = false;
    private UserAccount messageAuthor = null;

    private static final long serialVersionUID = 5693111856955648085L;
    private static final String CNAME = ServiceMessage.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setMessageId(final String value)
    {
        final String methodName = ServiceMessage.CNAME + "#setMessageId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.messageId = value;
    }

    public final void setIsNewMessage(final boolean value)
    {
        final String methodName = ServiceMessage.CNAME + "#setIsNewMessage(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.isNewMessage = value;
    }

    public final void setMessageTitle(final String value)
    {
        final String methodName = ServiceMessage.CNAME + "#setMessageTitle(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.messageTitle = value;
    }

    public final void setMessageText(final String value)
    {
        final String methodName = ServiceMessage.CNAME + "#setMessageText(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.messageText = value;
    }

    public final void setMessageAuthor(final UserAccount value)
    {
        final String methodName = ServiceMessage.CNAME + "#setMessageAuthor(final UserAccount value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.messageAuthor = value;
    }

    public final void setSubmitDate(final Date value)
    {
        final String methodName = ServiceMessage.CNAME + "#setSubmitDate(final Long value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.submitDate = value;
    }

    public final void setIsActive(final boolean value)
    {
        final String methodName = ServiceMessage.CNAME + "#setIsActive(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.isActive = value;
    }

    public final void setDoesExpire(final boolean value)
    {
        final String methodName = ServiceMessage.CNAME + "#setDoesExpire(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.doesExpire = value;
    }

    public final void setExpiryDate(final Date value)
    {
        final String methodName = ServiceMessage.CNAME + "#setExpiryDate(final Long value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.expiryDate = value;
    }

    public final void setIsAlert(final boolean value)
    {
        final String methodName = ServiceMessage.CNAME + "#setIsAlert(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.isAlert = value;
    }

    public final String getMessageId()
    {
        final String methodName = ServiceMessage.CNAME + "#getMessageId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.messageId);
        }

        return this.messageId;
    }

    public final boolean getIsNewMessage()
    {
        final String methodName = ServiceMessage.CNAME + "#getIsNewMessage()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isNewMessage);
        }

        return this.isNewMessage;
    }

    public final String getMessageTitle()
    {
        final String methodName = ServiceMessage.CNAME + "#getMessageTitle()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.messageTitle);
        }

        return this.messageTitle;
    }

    public final String getMessageText()
    {
        final String methodName = ServiceMessage.CNAME + "#getMessageText()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.messageText);
        }

        return this.messageText;
    }

    public final UserAccount getMessageAuthor()
    {
        final String methodName = ServiceMessage.CNAME + "#getMessageAuthor()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.messageAuthor);
        }

        return this.messageAuthor;
    }

    public final Date getSubmitDate()
    {
        final String methodName = ServiceMessage.CNAME + "#getSubmitDate()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.submitDate);
        }

        return this.submitDate;
    }

    public final boolean getIsActive()
    {
        final String methodName = ServiceMessage.CNAME + "#getIsActive()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isActive);
        }

        return this.isActive;
    }

    public final boolean getDoesExpire()
    {
        final String methodName = ServiceMessage.CNAME + "#getDoesExpire()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.doesExpire);
        }

        return this.doesExpire;
    }

    public final Date getExpiryDate()
    {
        final String methodName = ServiceMessage.CNAME + "#getExpiryDate()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.expiryDate);
        }

        return this.expiryDate;
    }

    public final boolean getIsAlert()
    {
        final String methodName = ServiceMessage.CNAME + "#getIsAlert()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isAlert);
        }

        return this.isAlert;
    }

    public final boolean isAlert()
    {
        final String methodName = ServiceMessage.CNAME + "#isAlert()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isAlert);
        }

        return this.isAlert;
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
