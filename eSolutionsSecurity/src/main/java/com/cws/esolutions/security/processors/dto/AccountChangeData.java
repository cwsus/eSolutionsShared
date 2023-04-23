/*
 * Copyright (c) 2009 - 2020 CaspersBox Web Services
 * 
 * All rights reserved. These materials are confidential and
 * proprietary to CaspersBox Web Services N.A and no part of
 * these materials should be reproduced, published in any form
 * by any means, electronic or mechanical, including photocopy
 * or any information storage or retrieval system not should
 * the materials be disclosed to third parties without the
 * express written authorization of CaspersBox Web Services, N.A.
 */
package com.cws.esolutions.security.processors.dto;
/*
 * Project: eSolutions_java_source
 * Package: com.cws.esolutions.web.dto
 * File: UserChangeRequest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly           11/23/2008 22:39:20             Created.
 */
import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.security.SecurityServicesConstants;
import com.cws.esolutions.security.processors.enums.ResetRequestType;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public final class AccountChangeData implements Serializable
{
	private int count = 0;
    private String guid = null;
    private String username = null;
    private String telNumber = null;
    private boolean isReset = false;
    private String emailAddr = null;
    private String pagerNumber = null;
    private char[] newPassword = null;
    private char[] secAnswerOne = null;
    private char[] secAnswerTwo = null;
    private String secQuestionOne = null;
    private String secQuestionTwo = null;
    private char[] currentPassword = null;
    private char[] confirmPassword = null;
    private ResetRequestType resetType = null;
    
    private static final long serialVersionUID = -7617490638200945132L;
    private static final String CNAME = AccountChangeData.class.getName();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setGuid(final String value)
    {
        final String methodName = AccountChangeData.CNAME + "#setGuid(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.guid = value;
    }

    public final void setIsReset(final boolean value)
    {
        final String methodName = AccountChangeData.CNAME + "#setIsReset(final boolean value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.isReset = value;
    }

    public final void setResetType(final ResetRequestType value)
    {
        final String methodName = AccountChangeData.CNAME + "#setResetType(final ResetRequestType value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.resetType = value;
    }

    public final void setUsername(final String value)
    {
    	this.username = value;
    }

    public final void setCurrentPassword(final char[] value)
    {
        this.currentPassword = value;
    }

    public final void setNewPassword(final char[] value)
    {
        this.newPassword = value;
    }

    public final void setConfirmPassword(final char[] value)
    {
        this.confirmPassword = value;
    }

    public final void setEmailAddr(final String value)
    {
        final String methodName = AccountChangeData.CNAME + "#setEmailAddr(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.emailAddr = value;
    }

    public final void setSecAnswerOne(final char[] value)
    {
        this.secAnswerOne = value;
    }

    public final void setSecAnswerTwo(final char[] value)
    {
        this.secAnswerTwo = value;
    }

    public final void setSecQuestionOne(final String value)
    {
        this.secQuestionOne = value;
    }

    public final void setSecQuestionTwo(final String value)
    {
        this.secQuestionTwo = value;
    }

    public final void setTelNumber(final String value)
    {
        final String methodName = AccountChangeData.CNAME + "#setTelNumber(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.telNumber = value;
    }

    public final void setPagerNumber(final String value)
    {
        final String methodName = AccountChangeData.CNAME + "#setPagerNumber(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.pagerNumber = value;
    }

    public final void setCount(final int value)
    {
        final String methodName = AccountChangeData.CNAME + "#setCount(final int value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.count = value;
    }

    public final boolean getIsReset()
    {
        final String methodName = AccountChangeData.CNAME + "#getIsReset()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isReset);
        }

        return this.isReset;
    }

    public final boolean isReset()
    {
        final String methodName = AccountChangeData.CNAME + "#isReset()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.isReset);
        }

        return this.isReset;
    }

    public final String getGuid()
    {
        final String methodName = AccountChangeData.CNAME + "#getGuid()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.guid);
        }

        return this.guid;
    }

    public final ResetRequestType getResetType()
    {
        final String methodName = AccountChangeData.CNAME + "#getResetType()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.resetType);
        }

        return this.resetType;
    }

    public final String getUsername()
    {
        final String methodName = AccountChangeData.CNAME + "#getUsername()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.username);
        }

        return this.username;
    }

    public final char[] getCurrentPassword()
    {
        return this.currentPassword;
    }

    public final char[] getNewPassword()
    {
        return this.newPassword;
    }

    public final char[] getConfirmPassword()
    {
        return this.confirmPassword;
    }

    public final String getEmailAddr()
    {
        final String methodName = AccountChangeData.CNAME + "#getEmailAddr()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.emailAddr);
        }

        return this.emailAddr;
    }

    public final char[] getSecAnswerOne()
    {
        return this.secAnswerOne;
    }

    public final char[] getSecAnswerTwo()
    {
        return this.secAnswerTwo;
    }

    public final String getSecQuestionOne()
    {
        return this.secQuestionOne;
    }

    public final String getSecQuestionTwo()
    {
        return this.secQuestionTwo;
    }

    public final String getTelNumber()
    {
        final String methodName = AccountChangeData.CNAME + "#getTelNumber()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.telNumber);
        }

        return this.telNumber;
    }

    public final String getPagerNumber()
    {
        final String methodName = AccountChangeData.CNAME + "#getPagerNumber()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.pagerNumber);
        }

        return this.pagerNumber;
    }

    public final int getCount()
    {
        final String methodName = AccountChangeData.CNAME + "#getCount()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.count);
        }

        return this.count;
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
                    (!(field.getName().equals("newPassword"))) &&
                    (!(field.getName().equals("secAnswerOne"))) &&
                    (!(field.getName().equals("secAnswerTwo"))) &&
                    (!(field.getName().equals("secQuestionOne"))) &&
                    (!(field.getName().equals("secQuestionTwo"))) &&
                    (!(field.getName().equals("currentPassword"))) &&
                    (!(field.getName().equals("confirmPassword"))))
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
