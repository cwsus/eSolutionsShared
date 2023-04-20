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
 * File: AccountControlProcessorImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;
import java.util.Objects;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.cws.esolutions.security.dto.UserGroup;
import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.enums.SecurityUserRole;
import com.cws.esolutions.security.processors.enums.SaltType;
import com.cws.esolutions.utility.securityutils.PasswordUtils;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.security.processors.dto.AuthenticationData;
import com.cws.esolutions.security.processors.dto.AccountControlRequest;
import com.cws.esolutions.security.processors.dto.AccountControlResponse;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditEntry;
import com.cws.esolutions.utility.services.dto.AccessControlServiceRequest;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
import com.cws.esolutions.utility.services.dto.AccessControlServiceResponse;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditRequest;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditResponse;
import com.cws.esolutions.security.processors.exception.AccountControlException;
import com.cws.esolutions.security.dao.usermgmt.exception.UserManagementException;
import com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor;
import com.cws.esolutions.utility.services.exception.AccessControlServiceException;
import com.cws.esolutions.utility.securityutils.processors.exception.AuditServiceException;
/**
 * @see com.cws.esolutions.security.processors.interfaces.IFileSecurityProcessor
 */
public class AccountControlProcessorImpl implements IAccountControlProcessor
{
    private static final String CNAME = AccountControlProcessorImpl.class.getName();

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#createNewUser(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse createNewUser(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#createNewUser(final CreateUserRequest createReq) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();
        final AuthenticationData userSecurity = request.getUserSecurity();

        if (DEBUG)
        {
            DEBUGGER.debug("Requestor: {}", reqAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("AuthenticationData: {}", userSecurity);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            String userGuid = UUID.randomUUID().toString();

            if (DEBUG)
            {
                DEBUGGER.debug("New user UUID: {}", userGuid);
            }

            for (int x = 0; x < 10; x++)
            {
                if (x == 10)
                {
                    throw new AccountControlException("Failed to generate a unique user identifier");
                }

                try
                {
                    userManager.validateUserAccount(userAccount.getUsername(), userGuid);

                    break;
                }
                catch (final UserManagementException umx)
                {
                    ERROR_RECORDER.error(umx.getMessage(), umx);

                    if (!(StringUtils.contains(umx.getMessage(), "UUID")))
                    {
                        response.setRequestStatus(SecurityRequestStatus.FAILURE);

                        return response;
                    }

                    userGuid = UUID.randomUUID().toString();

                    if (DEBUG)
                    {
                        DEBUGGER.debug("Value: {}", userGuid);
                    }

                    x++;

                    continue;
                }
            }

            char[] newPassword = PasswordUtils.returnGeneratedSalt(secConfig.getRandomGenerator(), secConfig.getPasswordMinLength()).toCharArray();
            String newSalt = PasswordUtils.returnGeneratedSalt(secConfig.getRandomGenerator(), secConfig.getIterations());
            String encNewPassword = PasswordUtils.encryptText(newPassword, newSalt,
                    secConfig.getSecretKeyAlgorithm(), secConfig.getIterations(), secConfig.getKeyLength(),
                    sysConfig.getEncoding());

            if ((StringUtils.isBlank(newSalt) || (StringUtils.isBlank(encNewPassword))))
            {
            	throw new AccountControlException("Failed to generate new user logon information.");
            }

            List<String> accountData = new ArrayList<String>(
                Arrays.asList(
                		userGuid, // commonName
                		userAccount.getUsername(), // uid
                		encNewPassword,
                		userAccount.getUserRole().toString(), // cwsrole
                		userAccount.getSurname(), // surname
                		userAccount.getGivenName(), // gvenName
                		newSalt,
                		SaltType.LOGON.toString(),
                		userAccount.getEmailAddr(), // email
                		userAccount.getTelephoneNumber(), // telnum
                		userAccount.getPagerNumber() // pagernum
                		));

            if (DEBUG)
            {
                DEBUGGER.debug("accountData: {}", accountData);
            }

            boolean isUserCreated = userManager.addUserAccount(accountData);

            if (DEBUG)
            {
                DEBUGGER.debug("isUserCreated: {}", isUserCreated);
            }

            if (isUserCreated)
            {
            	UserAccount resAccount = new UserAccount();
            	resAccount.setGuid(userGuid);
            	resAccount.setUsername(userAccount.getUsername());
            	resAccount.setUserRole(userAccount.getUserRole());
            	resAccount.setSurname(userAccount.getSurname());
            	resAccount.setGivenName(userAccount.getGivenName());
            	resAccount.setEmailAddr(userAccount.getEmailAddr());

            	if (DEBUG)
            	{
            		DEBUGGER.debug("UserAccount: {}", resAccount);
            	}

            	response.setUserAccount(resAccount);
                response.setRequestStatus(SecurityRequestStatus.SUCCESS);
            }
            else
            {
                // failed to add the user to the repository
                ERROR_RECORDER.error("Failed to add user to the userAccount repository");

                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final AccountControlException acx)
        {
            ERROR_RECORDER.error(acx.getMessage(), acx);

            throw new AccountControlException(acx.getMessage(), acx);
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountControlException(umx.getMessage(), umx);
        }
        catch (final SecurityException sx)
        {
            ERROR_RECORDER.error(sx.getMessage(), sx);

            throw new AccountControlException(sx.getMessage(), sx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.CREATEUSER);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#removeUserAccount(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse removeUserAccount(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#removeUserAccount(final AccountControlRequest) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount reqUser: {}", reqAccount);
            DEBUGGER.debug("UserAccount userAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            // delete userAccount
            boolean isComplete = userManager.removeUserAccount(userAccount.getGuid());

            if (DEBUG)
            {
                DEBUGGER.debug("isComplete: {}", isComplete);
            }

            if (isComplete)
            {
                response.setRequestStatus(SecurityRequestStatus.SUCCESS);
            }
            else
            {
                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountControlException(umx.getMessage(), umx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.DELETEUSER);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#modifyUserSuspension(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse modifyUserSuspension(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#modifyUserSuspension(final AccountControlRequest) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();
        
        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount reqUser: {}", reqAccount);
            DEBUGGER.debug("UserAccount userAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            // we will only have a guid here - so we need to load the user
            List<Object> userData = userManager.loadUserAccount(userAccount.getGuid());

            if (DEBUG)
            {
                DEBUGGER.debug("List<Object>: {}", userData);
            }

            if ((userData != null) && (userData.size() != 0))
            {
                boolean isComplete = userManager.modifyUserSuspension(userAccount.getGuid(), userAccount.isSuspended());

                if (DEBUG)
                {
                    DEBUGGER.debug("isComplete: {}", isComplete);
                }

                if (isComplete)
                {
                    response.setUserAccount(userAccount);
                    response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                }
                else
                {
                    response.setRequestStatus(SecurityRequestStatus.FAILURE);
                }
            }
            else
            {
                ERROR_RECORDER.error("Failed to locate user for given GUID");

                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountControlException(umx.getMessage(), umx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.SUSPENDUSER);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#modifyUserRole(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse modifyUserRole(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#modifyUserRole(final AccountControlRequest) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount reqUser: {}", reqAccount);
            DEBUGGER.debug("UserAccount userAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            // we will only have a guid here - so we need to load the user
            List<Object> userData = userManager.loadUserAccount(userAccount.getGuid());

            if (DEBUG)
            {
                DEBUGGER.debug("List<Object>: {}", userData);
            }

            if ((userData != null) && (userData.size() != 0))
            {
                boolean isComplete = userManager.modifyUserRole(userAccount.getGuid(), userAccount.getUserRole().toString());

                if (DEBUG)
                {
                    DEBUGGER.debug("isComplete: {}", isComplete);
                }

                if (isComplete)
                {
                    List<Object> resData = userManager.loadUserAccount((String) userData.get(0));

                    if (DEBUG)
                    {
                        DEBUGGER.debug("List<Object>: {}", resData);
                    }

                    if ((resData != null) && (!(resData.isEmpty())))
                    {
                    	List<UserGroup> userGroups = new ArrayList<UserGroup>();

                    	for (String groupId : (String[]) userData.get(13).toString().split(","))
                    	{
                    		if (DEBUG)
                    		{
                    			DEBUGGER.debug("String: groupId: {}", groupId);
                    		}

                    		UserGroup group = new UserGroup();
                    		group.setGuid(groupId);

                    		if (DEBUG)
                    		{
                    			DEBUGGER.debug("UserGroup: group: {}", group);
                    		}

                    		userGroups.add(group);

                    		if (DEBUG)
                    		{
                    			DEBUGGER.debug("List<UserGroup>: userGroups: {}", userGroups);
                    		}
                    	}

                		if (DEBUG)
                		{
                			DEBUGGER.debug("List<UserGroup>: userGroups: {}", userGroups);
                		}

                        UserAccount resAccount = new UserAccount();
                        resAccount.setUsername((String) userData.get(0));
                        resAccount.setGuid((String) userData.get(1));
                        resAccount.setUserRole(SecurityUserRole.valueOf((String) userData.get(2)));
                        resAccount.setFailedCount(((Objects.isNull(userData.get(9))) ? 0 : (Integer) userData.get(3)));
                        resAccount.setLastLogin(((Objects.isNull(userData.get(4))) ? new Date(System.currentTimeMillis()) : new Date(((Timestamp) userData.get(4)).getTime()))); // fix
                        resAccount.setSurname((String) userData.get(5));
                        resAccount.setGivenName((String) userData.get(6));
                        resAccount.setExpiryDate(((Objects.isNull(userData.get(7))) ? new Date(System.currentTimeMillis()) : new Date(((Timestamp) userData.get(7)).getTime()))); // fix
                        resAccount.setSuspended(((Objects.isNull(userData.get(8))) ? Boolean.FALSE : (Boolean) userData.get(8)));
                        resAccount.setDisplayName((String) userData.get(11));
                        resAccount.setEmailAddr((String) userData.get(14));
                        resAccount.setTelephoneNumber((String) userData.get(15));
                        resAccount.setPagerNumber((String) userData.get(16));
                        resAccount.setUserGroups(userGroups);

                        if (DEBUG)
                        {
                            DEBUGGER.debug("UserAccount: {}", resAccount);
                        }

                        response.setUserAccount(resAccount);
                    }
                    else
                    {
                        // if we have an issue re-loading the userAccount
                        // just put the existing one in. its something.
                        response.setUserAccount(userAccount);
                    }

                    response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                }
                else
                {
                    response.setRequestStatus(SecurityRequestStatus.FAILURE);
                }
            }
            else
            {
                ERROR_RECORDER.error("Failed to locate user for given GUID");

                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountControlException(umx.getMessage(), umx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.CHANGEROLE);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#modifyUserPassword(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse modifyUserPassword(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#modifyUserPassword(final AccountControlRequest request) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final Calendar calendar = Calendar.getInstance();
        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        calendar.add(Calendar.DATE, secConfig.getPasswordExpiration());

        if (DEBUG)
        {
            DEBUGGER.debug("Calendar: {}", calendar);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("UserAccount: {}", userAccount);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            // this is a reset request, so we need to do a few things
            // 1, we need to generate a unique id that we can email off
            // to the user, that we can then look up to confirm
            // then once we have that we can actually do the reset
            // first, change the existing password
            // 128 character values - its possible that the reset is
            // coming as a result of a possible compromise
            char[] newPassword = RandomStringUtils.random(secConfig.getPasswordMaxLength()).toCharArray();
            String newSalt = PasswordUtils.returnGeneratedSalt(secConfig.getRandomGenerator(), secConfig.getSaltLength());
            String tmpPassword = PasswordUtils.encryptText(newPassword, newSalt,
                    secConfig.getSecretKeyAlgorithm(),
                    secConfig.getIterations(), secConfig.getKeyLength(),
                    //secConfig.getEncryptionAlgorithm(), secConfig.getEncryptionInstance(),
                    sysConfig.getEncoding());

            if ((StringUtils.isBlank(newSalt)) || (StringUtils.isBlank(tmpPassword)))
            {
            	throw new AccountControlException("Failed to generate security information for the request.");
            }

            // update the authentication datastore with the new password
            // we never show the user the password, we're only doing this
            // to prevent unauthorized access (or further unauthorized access)
            // we get a return code back but we aren't going to use it really
            boolean isComplete = userSec.modifyUserPassword(userAccount.getGuid(), userAccount.getUsername(), tmpPassword, true);

            if (DEBUG)
            {
                DEBUGGER.debug("isComplete: {}", isComplete);
            }

            // now generate a temporary id to stuff into the database
            // this will effectively replace the current salt value
            String resetId = RandomStringUtils.randomAlphanumeric(secConfig.getResetIdLength());

            if (StringUtils.isNotEmpty(resetId))
            {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, secConfig.getResetTimeout());

                if (DEBUG)
                {
                	DEBUGGER.debug("Calendar: {}", cal);
                }

                isComplete = userSec.insertResetData(userAccount.getGuid(), resetId, cal.toInstant());

                if (DEBUG)
                {
                    DEBUGGER.debug("isComplete: {}", isComplete);
                }

                if (isComplete)
                {
                	response.setUserAccount(userAccount);
                    response.setResetId(resetId);
                    response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                }
                else
                {
                    ERROR_RECORDER.error("Unable to insert password identifier into database. Cannot continue.");

                    response.setRequestStatus(SecurityRequestStatus.FAILURE);
                }
            }
            else
            {
                ERROR_RECORDER.error("Unable to generate a unique identifier. Cannot continue.");

                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final SecurityException sx)
        {
            ERROR_RECORDER.error(sx.getMessage(), sx);

            throw new AccountControlException(sx.getMessage(), sx);
        }
        catch (SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AccountControlException(sqx.getMessage(), sqx);
		}
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.RESETPASS);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#modifyUserLockout(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse modifyUserLockout(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#modifyUserLockout(final AccountControlRequest) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount reqUser: {}", reqAccount);
            DEBUGGER.debug("UserAccount userAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            // we will only have a guid here - so we need to load the user
            List<Object> userData = userManager.loadUserAccount(userAccount.getGuid());

            if (DEBUG)
            {
                DEBUGGER.debug("List<Object>: {}", userData);
            }

            if ((userData != null) && (userData.size() != 0))
            {
                boolean isComplete = userManager.modifyUserLock((String) userData.get(1), ((userAccount.getFailedCount() >= 3) ? true : false), userAccount.getFailedCount());

                if (DEBUG)
                {
                    DEBUGGER.debug("isComplete: {}", isComplete);
                }

                if (isComplete)
                {
                    response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                }
                else
                {
                	response.setRequestStatus(SecurityRequestStatus.FAILURE);
                }
            }
            else
            {
                ERROR_RECORDER.error("Failed to locate user for given GUID");

                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountControlException(umx.getMessage(), umx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.MODIFYLOCKOUT);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#loadUserAccount(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse loadUserAccount(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#loadUserAccount(final AccountControlRequest request) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", userAccount);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            List<Object> userData = userManager.loadUserAccount(userAccount.getGuid());

            if (DEBUG)
            {
                DEBUGGER.debug("List<Object>: {}", userData);
            }

            if ((userData != null) && (!(userData.isEmpty())))
            {
            	UserAccount loadedAccount = new UserAccount();
            	loadedAccount.setUsername((String) userData.get(0)); // T1.UID,
            	loadedAccount.setGuid((String) userData.get(1)); // T1.CN,
            	loadedAccount.setUserRole(SecurityUserRole.valueOf((String) userData.get(2))); // T1.CWSROLE,
            	loadedAccount.setFailedCount((Integer) userData.get(3)); // T1.CWSFAILEDPWDCOUNT,
            	loadedAccount.setLastLogin((Date) userData.get(4)); // T1.CWSLASTLOGIN,
            	loadedAccount.setSurname((String) userData.get(5)); // T1.SN,
            	loadedAccount.setGivenName((String) userData.get(6)); // T1.GIVENNAME,
            	loadedAccount.setExpiryDate((Date) userData.get(7)); // T1.CWSEXPIRYDATE,
            	loadedAccount.setSuspended((Boolean) userData.get(8)); // T1.CWSISSUSPENDED,
            	loadedAccount.setDisplayName((String) userData.get(11)); // T1.DISPLAYNAME,
            	loadedAccount.setAcceptedTerms((Boolean) userData.get(12)); // T1.CWSISTCACCEPTED,
            	loadedAccount.setEmailAddr((String) userData.get(13)); // T2.EMAIL

                if (DEBUG)
                {
                    DEBUGGER.debug("UserAccount: {}", loadedAccount);
                }

                response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                response.setUserAccount(loadedAccount);
            }
            else
            {
                response.setRequestStatus(SecurityRequestStatus.FAILURE);
            }

            if (DEBUG)
            {
                DEBUGGER.debug("AccountControlResponse: {}", response);
            }
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountControlException(umx.getMessage(), umx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.LOADACCOUNT);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor#listUserAccounts(com.cws.esolutions.security.processors.dto.AccountControlRequest)
     */
    public AccountControlResponse listUserAccounts(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#listUserAccounts(final AccountControlRequest request) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountControlRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getRequestor();
        final UserAccount userAccount = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", userAccount);
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            List<Object[]> userList = userManager.listUserAccounts();

            if (DEBUG)
            {
                DEBUGGER.debug("userList: {}", userList);
            }

            if ((userList != null) && (userList.size() != 0))
            {
                List<UserAccount> userAccounts = new ArrayList<UserAccount>();

                for (Object[] userData : userList)
                {
                    if (!(StringUtils.equals(reqAccount.getGuid(), (String) userData[0])))
                    {
                        UserAccount userInfo = new UserAccount();
                        userInfo.setGuid((String) userData[0]);
                        userInfo.setUsername((String) userData[1]);
                        userInfo.setUserRole(SecurityUserRole.valueOf((String) userData[2]));
                        userInfo.setLastLogin(new Date(((Timestamp) userData[3]).getTime()));

                        if (DEBUG)
                        {
                            DEBUGGER.debug("UserAccount: {}", userInfo);
                        }

                        userAccounts.add(userInfo);
                    }
                }

                if (DEBUG)
                {
                    DEBUGGER.debug("userAccounts: {}", userAccounts);
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

            throw new AccountControlException(umx.getMessage(), umx);
        }
        finally
        {
            // audit
            if (secConfig.getPerformAudit())
            {
                // audit if a valid account. if not valid we cant audit much,
                // but we should try anyway. not sure how thats going to work
                try
                {
                    AuditEntry auditEntry = new AuditEntry();
                    auditEntry.setAuditType(AuditType.LISTUSERS);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAuditProcessor#getAuditEntries(com.cws.esolutions.security.processors.dto.AuditRequest)
     */
    public AccountControlResponse getAuditEntries(final AccountControlRequest request) throws AccountControlException
    {
        final String methodName = AccountControlProcessorImpl.CNAME + "#getAuditEntries(final AccountControlRequest request) throws AccountControlException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AuditRequest: {}", request);
        }

        AccountControlResponse response = new AccountControlResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount userAccount = request.getUserAccount();
        final UserAccount reqAccount = request.getRequestor();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("UserAccount: {}", userAccount);
        }

        if (!(secConfig.getPerformAudit()))
        {
        	throw new AccountControlException("Audit services are not enabled");
        }

        try
        {
        	try
        	{
	            // this will require admin and service authorization
	            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
	            accessRequest.setServiceGuid(request.getServiceId());
	            accessRequest.setUserAccount(
	            		new ArrayList<Object>(
	            				Arrays.asList(
	            						reqAccount.getGuid(),
	            						reqAccount.getUserRole().toString(),
	            						reqAccount.getUserGroups())));
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceRequest: {}", accessRequest);
	            }
	
	            AccessControlServiceResponse accessResponse = accessControl.isUserAuthorized(accessRequest);
	
	            if (DEBUG)
	            {
	                DEBUGGER.debug("AccessControlServiceResponse accessResponse: {}", accessResponse);
	            }
	
	            if (!(accessResponse.getIsUserAuthorized()))
	            {
	                // unauthorized
	                response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

	                return response;
	            }
            }
        	catch (AccessControlServiceException acsx)
        	{
        		ERROR_RECORDER.error(acsx.getMessage(), acsx);

        		response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

        		return response;
			}
        	finally
        	{
                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.CREATEUSER);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(reqAccount.getSessionId());
                        auditEntry.setUserGuid(reqAccount.getGuid());
                        auditEntry.setUserName(reqAccount.getUsername());
                        auditEntry.setUserRole(reqAccount.getUserRole().toString());
                        auditEntry.setAuthorized(Boolean.FALSE);
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

            AuditEntry auditEntry = new AuditEntry();
            auditEntry.setUserGuid(reqAccount.getGuid());

            if (DEBUG)
            {
            	DEBUGGER.debug("AuditEntry: {}", auditEntry);
            }

            AuditRequest auditRequest = new AuditRequest();
            auditRequest.setAuditEntry(auditEntry);

            if (DEBUG)
            {
            	DEBUGGER.debug("AuditEntry: {}", auditEntry);
            }

            AuditResponse auditResponse = auditor.getAuditEntries(auditRequest);

            if (DEBUG)
            {
                DEBUGGER.debug("AuditResponse: {}", auditResponse);
            }

            switch (auditResponse.getRequestStatus())
            {
				case FAILURE:
					response.setRequestStatus(SecurityRequestStatus.FAILURE);

					break;
				case SUCCESS:
					response.setEntryCount(auditResponse.getEntryCount());
					response.setAuditList(auditResponse.getAuditList());
					response.setRequestStatus(SecurityRequestStatus.SUCCESS);

					break;
				case UNAUTHORIZED:
					response.setRequestStatus(SecurityRequestStatus.UNAUTHORIZED);

					break;
				default:
					response.setRequestStatus(SecurityRequestStatus.FAILURE);

					break;
            }
        }
        catch (final AuditServiceException asx)
        {
            ERROR_RECORDER.error(asx.getMessage(), asx);

            throw new AccountControlException(asx.getMessage(), asx);
        }
        finally
        {
        	if (secConfig.getPerformAudit())
        	{
	            // audit
	            try
	            {
	                AuditEntry auditEntry = new AuditEntry();
	                auditEntry.setAuditType(AuditType.SHOWAUDIT);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(reqAccount.getSessionId());
                    auditEntry.setUserGuid(reqAccount.getGuid());
                    auditEntry.setUserName(reqAccount.getUsername());
                    auditEntry.setUserRole(reqAccount.getUserRole().toString());
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
