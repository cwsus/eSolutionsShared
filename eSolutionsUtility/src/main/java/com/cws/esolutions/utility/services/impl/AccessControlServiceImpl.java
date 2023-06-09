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
package com.cws.esolutions.utility.services.impl;
import java.sql.SQLException;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.services.impl
 * File: AccessControlServiceImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.utility.services.dto.AccessControlServiceRequest;
import com.cws.esolutions.utility.services.dto.AccessControlServiceResponse;
import com.cws.esolutions.utility.services.interfaces.IAccessControlService;
import com.cws.esolutions.utility.services.exception.AccessControlServiceException;
/**
 * @see com.cws.esolutions.security.services.interfaces.IAccessControlService
 */
public class AccessControlServiceImpl implements IAccessControlService
{
    private static final String CNAME = AccessControlServiceImpl.class.getName();

    /**
     * @see com.cws.esolutions.security.services.interfaces.IAccessControlService#isUserAuthorized(AccessControlServiceRequest) throws AccessControlServiceException
     */
    public AccessControlServiceResponse isUserAuthorized(final AccessControlServiceRequest request) throws AccessControlServiceException
    {
        final String methodName = AccessControlServiceImpl.CNAME + "#isUserAuthorized(final AccessControlServiceRequest request) throws AccessControlServiceException";

        AccessControlServiceResponse response = new AccessControlServiceResponse();

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccessControlServiceRequest: {}", request);
        }

        boolean isAuthorized = false;
        List<Object> accountData = request.getUserAccount();

        if (DEBUG)
        {
        	DEBUGGER.debug("UserAccount: {}", accountData);
        }

        try
        {
	        if (StringUtils.equals((String) accountData.get(1), "SITE_ADMIN"))
	        {
	        	response.setIsUserAuthorized(Boolean.TRUE);
	        }
	        else
	        {
	        	boolean isEnabled = dao.isGroupEnabled(request.getServiceGuid());

	        	if (DEBUG)
	        	{
	        		DEBUGGER.debug("isEnabled: {}", isEnabled);
	        	}

	        	if (isEnabled)
	        	{
	        		String[] userGroups = dao.getUserGroups((String) accountData.get(0)).split(",");

	        		if (DEBUG)
	        		{
	        			DEBUGGER.debug("List<String>: userGroups: {}", (Object) userGroups);
	        		}

	        		for (String group : userGroups)
	        		{
	        			if (DEBUG)
	        			{
	        				DEBUGGER.debug("String: group: {}", group);
	        			}

	        			isAuthorized = StringUtils.equals(request.getServiceGuid(), group.trim());

	        			if (DEBUG)
	        			{
	        				DEBUGGER.debug("isAuthorized: {}", isAuthorized);
	        			}

	        			if (isAuthorized)
	        			{
	        				response.setIsUserAuthorized(isAuthorized);

	        				break;
	        			}
	        		}
	        	}
	        	else
	        	{
	        		response.setIsUserAuthorized(Boolean.FALSE);
	        	}
	        }
        }
        catch (SQLException sqx)
        {
        	ERROR_RECORDER.error(sqx.getMessage(), sqx);
        }
        
        return response;
    }
}
