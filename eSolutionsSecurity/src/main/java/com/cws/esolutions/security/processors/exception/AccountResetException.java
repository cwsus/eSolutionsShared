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
package com.cws.esolutions.security.processors.exception;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.processors.exception
 * File: AccountResetException.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import com.cws.esolutions.security.exception.SecurityServiceException;
/**
 * @see com.cws.esolutions.security.exception.SecurityServiceException
 */
public class AccountResetException extends SecurityServiceException
{
    private static final long serialVersionUID = 5165671678201410191L;

    /**
     * @param message - The thrown exception message
     * @see com.cws.esolutions.security.exception.SecurityServiceException#SecurityServiceException(java.lang.String)
     */
    public AccountResetException(final String message)
    {
        super(message);
    }

    /**
     * @param throwable - The thrown exception
     * @see com.cws.esolutions.security.exception.SecurityServiceException#SecurityServiceException(java.lang.Throwable)
     */
    public AccountResetException(final Throwable throwable)
    {
        super(throwable);
    }

    /**
     * @param message - The thrown exception message
     * @param throwable - The thrown exception
     * @see com.cws.esolutions.security.exception.SecurityServiceException#SecurityServiceException(java.lang.String, java.lang.Throwable)
     */
    public AccountResetException(final String message, final Throwable throwable)
    {
        super(message, throwable);
    }
}
