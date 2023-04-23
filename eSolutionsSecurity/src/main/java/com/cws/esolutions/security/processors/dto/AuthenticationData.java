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
 * File: AuthenticationData.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.io.Serializable;
/**
 * @author cws-khuntly
 * @version 1.0
 * @see java.io.Serializable
 */
public class AuthenticationData implements Serializable
{
	private String username = null;
    private String userSalt = null;
    private char[] password = null;
    private String resetKey = null;
    private String sessionId = null;
    private boolean isReset = false;
    private char[] newPassword = null;
    private char[] currentPassword = null;
    private char[] confirmPassword = null;

    private static final long serialVersionUID = 1920284352649895644L;

    public final void setUsername(final String value)
    {
        this.username = value;
    }

    public final void setUserSalt(final String value)
    {
        this.userSalt = value;
    }

    public final void setPassword(final char[] value)
    {
        this.password = value;
    }

    public final void setNewPassword(final char[] value)
    {
        this.newPassword = value;
    }

    public final void setSessionId(final String value)
    {
        this.sessionId = value;
    }

    public final void setResetKey(final String value)
    {
        this.resetKey = value;
    }

    public final void setIsReset(final boolean value)
    {
        this.isReset = value;
    }

    public final void setCurrentPassword(final char[] value)
    {
        this.currentPassword = value;
    }

    public final void setConfirmPassword(final char[] value)
    {
        this.confirmPassword = value;
    }

    public final String getUsername()
    {
        return this.username;
    }

    public final String getUserSalt()
    {
        return this.userSalt;
    }

    public final char[] getPassword()
    {
        return this.password;
    }

    public final char[] getNewPassword()
    {
        return this.newPassword;
    }

    public final String getSessionId()
    {
        return this.sessionId;
    }

    public final String getResetKey()
    {
        return this.resetKey;
    }

    public final boolean getIsReset()
    {
        return this.isReset;
    }

    public final boolean isReset()
    {
        return this.isReset;
    }

    public final char[] getCurrentPassword()
    {
        return this.currentPassword;
    }

    public final char[] getConfirmPassword()
    {
        return this.confirmPassword;
    }
}
