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
 * File: AccountChangeResponse.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.SecurityServiceConstants;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class AccountChangeResponse implements Serializable
{
	private String secret = null;
    private UserAccount userAccount = null;
    private List<String> questionList = null;
    private ByteArrayOutputStream qrCode = null;
    private SecurityRequestStatus requestStatus = null;

    private static final long serialVersionUID = 2748842176593775221L;
    private static final String CNAME = AccountChangeResponse.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServiceConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setRequestStatus(final SecurityRequestStatus value)
    {
        final String methodName = AccountChangeResponse.CNAME + "#setRequestStatus(final SecurityRequestStatus value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.requestStatus = value;
    }

    public final void setUserAccount(final UserAccount value)
    {
        final String methodName = AccountChangeResponse.CNAME + "#setUserAccount(final UserAccount value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.userAccount = value;
    }

    public final void setQuestionList(final List<String> value)
    {
        final String methodName = AccountChangeResponse.CNAME + "#setQuestionList(final List<String> value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.questionList = value;
    }

    public final void setQrCode(final ByteArrayOutputStream value)
    {
        final String methodName = AccountChangeResponse.CNAME + "#setQrCode(final ByteArrayOutputStream value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.qrCode = value;
    }

    public final void setSecret(final String value)
    {
        final String methodName = AccountChangeResponse.CNAME + "#setSecret(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.secret = value;
    }

    public final SecurityRequestStatus getRequestStatus()
    {
        final String methodName = AccountChangeResponse.CNAME + "#getMgmtType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.requestStatus);
        }

        return this.requestStatus;
    }

    public final UserAccount getUserAccount()
    {
        final String methodName = AccountChangeResponse.CNAME + "#getUserAccount()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.userAccount);
        }

        return this.userAccount;
    }

    public final List<String> getQuestionList()
    {
        final String methodName = AccountChangeResponse.CNAME + "#getQuestionList()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.questionList);
        }

        return this.questionList;
    }

    public final ByteArrayOutputStream getQrCode()
    {
        final String methodName = AccountChangeResponse.CNAME + "#getQrCode()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.qrCode);
        }

        return this.qrCode;
    }

    public final String getSecret()
    {
        final String methodName = AccountChangeResponse.CNAME + "#getSecret()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.secret);
        }

        return this.secret;
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
