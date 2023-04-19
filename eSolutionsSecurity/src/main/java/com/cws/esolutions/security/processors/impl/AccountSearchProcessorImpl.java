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
package com.cws.esolutions.security.processors.impl;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.processors.impl
 * File: AccountChangeProcessorImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
import com.cws.esolutions.security.processors.dto.AccountSearchRequest;
import com.cws.esolutions.security.processors.dto.AccountSearchResponse;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditEntry;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditRequest;
import com.cws.esolutions.security.processors.exception.AccountSearchException;
import com.cws.esolutions.security.processors.interfaces.IAccountSearchProcessor;
import com.cws.esolutions.security.dao.usermgmt.exception.UserManagementException;
import com.cws.esolutions.utility.securityutils.processors.exception.AuditServiceException;
/**
 * @see com.cws.esolutions.security.processors.interfaces.IAccountChangeProcessor
 */
public class AccountSearchProcessorImpl implements IAccountSearchProcessor
{
    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor#findUserAccount(com.cws.esolutions.security.processors.dto.AccountResetRequest)
     */
    public AccountSearchResponse findUserAccount(final AccountSearchRequest request) throws AccountSearchException
    {
        final String methodName = AccountSearchProcessorImpl.CNAME + "#findUserAccount(final AccountSearchRequest request) throws AccountSearchException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        List<UserAccount> responseData = null;
        AccountSearchResponse response = new AccountSearchResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", userAccount);
        }

        try
        {
            List<String[]> userList = userManager.findUsers(request.getSearchTerms());

            if (DEBUG)
            {
                DEBUGGER.debug("List<String[]>: {}", userList);
            }

            if ((Objects.isNull(userList)) || (userList.size() == 0))
            {
                response.setRequestStatus(SecurityRequestStatus.FAILURE);

                return response;
            }

            if (userList.size() == 1)
            {
            	String userGuid = userList.get(0)[0];

            	if (DEBUG)
            	{
            		DEBUGGER.debug("userGuid: {}", userGuid);
            	}

            	List<Object> accountData = userManager.loadUserAccount(userGuid);

            	if (DEBUG)
            	{
            		DEBUGGER.debug("accountData: {}", accountData);
            	}

                UserAccount foundAccount = new UserAccount();
            	foundAccount.setGuid((String) accountData.get(1)); // CN
            	foundAccount.setUsername((String) accountData.get(0)); // UID
	            foundAccount.setDisplayName((String) accountData.get(11)); // DISPLAYNAME
	            foundAccount.setEmailAddr((String) accountData.get(13)); // EMAIL

            	if (DEBUG)
            	{
            		DEBUGGER.debug("UserAccount: {}", foundAccount);
            	}

            	response.setUserAccount(foundAccount);
            	response.setRequestStatus(SecurityRequestStatus.SUCCESS);
            }
            else
            {
	            responseData = new ArrayList<UserAccount>();
	
	            for (String[] data : userList)
	            {
	            	if (DEBUG)
	            	{
	            		DEBUGGER.debug("data: {}", (Object) data);
	            	}
	
	            	UserAccount foundAccount = new UserAccount();
	
	            	if (data.length > 1)
	            	{
	                	foundAccount.setGuid(data[0]);
	                	foundAccount.setUsername(data[1]);
	            	}
	            	else
	            	{
	                	foundAccount.setGuid(data[0]);
	            	}
	
	                if (DEBUG)
	                {
	                    DEBUGGER.debug("foundAccount: {}", foundAccount);
	                }
	
	                responseData.add(foundAccount);
	            }
	
	            response.setRequestStatus(SecurityRequestStatus.SUCCESS);
	            response.setUserList(responseData);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountSearchException(umx.getMessage(), umx);
        }

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#searchAccounts(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountSearchResponse searchAccounts(final AccountSearchRequest request) throws AccountSearchException
    {
        final String methodName = AccountSearchProcessorImpl.CNAME + "#searchAccounts(final AccountSearchRequest request) throws AccountSearchException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        List<UserAccount> userAccounts = new ArrayList<UserAccount>();
        AccountSearchResponse response = new AccountSearchResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", userAccount);
        }

        try
        {
            List<String[]> userList = userManager.findUsers(request.getSearchTerms());

	        if (DEBUG)
	        {
	        	DEBUGGER.debug("userList: {}", userList);
	        }

            if (!(Objects.isNull(userList)) && (userList.size() != 0))
            {
                for (Object[] userData : userList)
                {
                	if (DEBUG)
                	{
                		DEBUGGER.debug("userData: {}", userData);
                	}

                	if (StringUtils.equals(userAccount.getGuid(), (String) userData[0]))
                	{
                		continue;
                	}

            		UserAccount userInfo = new UserAccount();
            		userInfo.setGuid((String) userData[0]);
            		userInfo.setUsername((String) userData[1]);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("UserAccount: {}", userInfo);
            		}

            		userAccounts.add(userInfo);

                    if (DEBUG)
                    {
                    	DEBUGGER.debug("userAccounts: {}", userAccounts);
                    }
                }

                if (userAccounts.size() == 0)
                {
                	response.setRequestStatus(SecurityRequestStatus.FAILURE);
                }
                else
                {
                	response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                    response.setUserList(userAccounts);
                }
            }
            else
            {
            	response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountSearchException(umx.getMessage(), umx);
        }
        finally
        {
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.SEARCHACCOUNTS);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(userAccount.getSessionId());
                    auditEntry.setUserGuid(userAccount.getGuid());
                    auditEntry.setUserName(userAccount.getUsername());
                    auditEntry.setUserRole(userAccount.getUserRole().toString());
                    auditEntry.setAuthorized(Boolean.TRUE);
                    auditEntry.setApplicationId(request.getApplicationId());
                    auditEntry.setApplicationName(request.getApplicationName());
    
                    if (DEBUG)
                    {
                        DEBUGGER.debug("AuditEntry: {}", auditEntry);
                    }
    
                    List<String> auditHostInfo = new ArrayList<String>(
                    		Arrays.asList(
                    				reqInfo.getHostAddress(),
                    				reqInfo.getHostName()));

                    if (DEBUG)
                    {
                    	DEBUGGER.debug("List<String>: {}", auditHostInfo);
                    }

                    AuditRequest auditRequest = new AuditRequest();
                    auditRequest.setAuditEntry(auditEntry);
                    auditRequest.setHostInfo(auditHostInfo);
    
                    if (DEBUG)
                    {
                        DEBUGGER.debug("AuditRequest: {}", auditRequest);
                    }

                    auditor.auditRequest(auditRequest);
                }
                catch (final AuditServiceException asx)
                {
                    ERROR_RECORDER.error(asx.getMessage(), asx);
                }
            }
        }

        return response;
    }
}
