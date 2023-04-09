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
package com.cws.esolutions.utility.services.exception;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.access.control.exception
 * File: AccessControlServiceException.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import com.cws.esolutions.utility.exception.UtilityException;
/**
 * @see com.cws.esolutions.security.exception.SecurityServiceException
 */
public class AccessControlServiceException extends UtilityException
{
    private static final long serialVersionUID = 4987391236304708183L;

	/**
     * @param message - The thrown exception message
     * @see com.cws.esolutions.security.exception.SecurityServiceException#SecurityServiceException(java.lang.String)
     */
    public AccessControlServiceException(final String message)
    {
        super(message);
    }

    /**
     * @param throwable - The thrown exception
     * @see com.cws.esolutions.security.exception.SecurityServiceException#SecurityServiceException(java.lang.Throwable)
     */
    public AccessControlServiceException(final Throwable throwable)
    {
        super(throwable);
    }

    /**
     * @param message - The thrown exception message
     * @param throwable - The thrown exception 
     * @see com.cws.esolutions.security.exception.SecurityServiceException#SecurityServiceException(java.lang.String, java.lang.Throwable)
     */
    public AccessControlServiceException(final String message, final Throwable throwable)
    {
        super(message, throwable);
    }
}
