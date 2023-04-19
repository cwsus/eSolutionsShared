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
 * File: AuthenticationProcessorImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.security.dto.UserGroup;
import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.enums.SecurityUserRole;
import com.cws.esolutions.security.processors.enums.SaltType;
import com.cws.esolutions.utility.securityutils.PasswordUtils;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
import com.cws.esolutions.security.processors.enums.LoginStatus;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.security.processors.dto.AuthenticationData;
import com.cws.esolutions.security.exception.SecurityServiceException;
import com.cws.esolutions.security.processors.dto.AuthenticationRequest;
import com.cws.esolutions.security.processors.dto.AuthenticationResponse;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditEntry;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditRequest;
import com.cws.esolutions.security.processors.exception.AuthenticationException;
import com.cws.esolutions.security.processors.interfaces.IAuthenticationProcessor;
import com.cws.esolutions.utility.securityutils.processors.exception.AuditServiceException;
/**
 * @see com.cws.esolutions.security.processors.interfaces.IAuthenticationProcessor
 */
public class AuthenticationProcessorImpl implements IAuthenticationProcessor
{
    private static final String CNAME = AuthenticationProcessorImpl.class.getName();

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAuthenticationProcessor#processAgentLogon(com.cws.esolutions.security.processors.dto.AuthenticationRequest)
     */
    public AuthenticationResponse processAgentLogon(final AuthenticationRequest request) throws AuthenticationException
    {
        final String methodName = AuthenticationProcessorImpl.CNAME + "#processAgentLogon(final AuthenticationRequest request) throws AuthenticationException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AuthenticationRequest: {}", request);
        }

        String userId = null;
        String userGuid = null;
        UserAccount userAccount = null;
        AuthenticationResponse response = new AuthenticationResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount authUser = request.getUserAccount();
        final AuthenticationData authSec = request.getUserSecurity();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", authUser);
        }

        try
        {
        	List<String> userInfo = userManager.getUserByUsername(authUser.getUsername());

            if (DEBUG)
            {
                DEBUGGER.debug("userInfo: {}", userInfo);
            }

            if (Objects.isNull(userInfo))
            {
            	throw new AuthenticationException("Unable to locate an account for the given information. Cannot continue");
            }

            userId = userInfo.get(1);
            userGuid = userInfo.get(0);

            if (DEBUG)
            {
            	DEBUGGER.debug("userId: {}", userId);
            	DEBUGGER.debug("userGuid: {}", userGuid);
            }

            String userSalt = userSec.getUserSalt(userGuid, SaltType.LOGON.name());

            if (DEBUG)
            {
            	DEBUGGER.debug("userSalt: {}", userSalt);
            }

            if (StringUtils.isBlank(userSalt))
            {
                throw new AuthenticationException("Unable to obtain configured user security information. Cannot continue");
            }

            String returnedPassword = PasswordUtils.encryptText(authSec.getPassword(), userSalt,
                    secConfig.getSecretKeyAlgorithm(),
                    secConfig.getIterations(), secConfig.getKeyLength(),
                    sysConfig.getEncoding());

            if (DEBUG)
            {
            	DEBUGGER.debug("returnedPassword: {}", returnedPassword);
            }

            boolean isAuthenticated = authenticator.performLogon(userGuid, userId, returnedPassword);

            if (DEBUG)
            {
            	DEBUGGER.debug("isAuthenticated: {}", isAuthenticated);
            }

            if (!(isAuthenticated))
            {
            	response.setRequestStatus(SecurityRequestStatus.FAILURE);

            	return response;
            }

            // load the user account here
            List<Object> userObject = userManager.loadUserAccount(userGuid);

            if (DEBUG)
            {
            	DEBUGGER.debug("authObject: {}", userObject);
            }

            if (Objects.isNull(userObject))
            {
            	response.setRequestStatus(SecurityRequestStatus.FAILURE);

            	return response;
            }
            else
            {
            	userAccount = new UserAccount();

            	if ((Integer) userObject.get(3) >= secConfig.getMaxAttempts())
            	{
            		userAccount.setStatus(LoginStatus.LOCKOUT);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("userAccount: {}", userAccount);
            		}

            		response.setUserAccount(userAccount);
            		response.setRequestStatus(SecurityRequestStatus.FAILURE);
	            }
	            else if ((Boolean) userObject.get(8))
	            {
            		userAccount.setStatus(LoginStatus.SUSPENDED);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("userAccount: {}", userAccount);
            		}

            		response.setUserAccount(userAccount);
	            	response.setRequestStatus(SecurityRequestStatus.FAILURE);
	            }
	            else
	            {
	            	// generate auth token
	            	String tokenValue = (String) userObject.get(1);
	            	String tokenSalt = PasswordUtils.returnGeneratedSalt(secConfig.getRandomGenerator(), secConfig.getSaltLength()); // salt value for auth token
	            	String authToken = PasswordUtils.encryptText(tokenValue.toCharArray(), tokenSalt,
	                        secConfig.getSecretKeyAlgorithm(), secConfig.getIterations(), secConfig.getKeyLength(),
	                        sysConfig.getEncoding());

	            	if (DEBUG)
	            	{
	            		DEBUGGER.debug("tokenValue: {}", tokenValue);
	            		DEBUGGER.debug("tokenSalt: {}", tokenSalt);
	            		DEBUGGER.debug("authToken: {}", authToken);
	            	}

	            	boolean isLoggedIn = authenticator.performSuccessfulLogin(userId, userGuid, authToken);
	            	boolean isAuthTokenInserted = userSec.addOrUpdateUserSalt((String) userObject.get(1), authToken, SaltType.AUTHTOKEN.toString());

	            	if (DEBUG)
	            	{
	            		DEBUGGER.debug("isLoggedIn: {}", isLoggedIn);
	            		DEBUGGER.debug("isAuthTokenInserted: {}", isAuthTokenInserted);
	            	}

	            	if ((!(isLoggedIn)) || (!(isAuthTokenInserted)))
	            	{
	            		throw new AuthenticationException("The authentication process failed. Please review logs.");
	            	}

	            	List<UserGroup> userGroups = null;

	            	if (!(Objects.isNull(userObject.get(13))))
	            	{
		            	String[] groupList = userManager.loadUserGroups((String) userObject.get(1)).split(",");

		            	if (DEBUG)
		            	{
		            		DEBUGGER.debug("List<String[]: groupList: {}", (Object) groupList);
		            	}

		            	userGroups = new ArrayList<UserGroup>();

		            	if (!(Objects.isNull(userGroups)));
		            	{
			            	for (String str : groupList)
			            	{
			            		if (DEBUG)
			            		{
			            			DEBUGGER.debug("String[]: str: {}", (Object) str);
			            		}

			            		UserGroup userGroup = new UserGroup();
			            		userGroup.setGuid(str);

			            		if (DEBUG)
			            		{
			            			DEBUGGER.debug("UserGroup: userGroup: {}", userGroup);
			            		}

			            		userGroups.add(userGroup);
			            	}
		            	}
	            	}

	            	userAccount.setUsername((String) userObject.get(0));
                    userAccount.setGuid((String) userObject.get(1));
                    userAccount.setUserRole(SecurityUserRole.valueOf((String) userObject.get(2)));
                    userAccount.setFailedCount(((Objects.isNull(userObject.get(9))) ? 0 : (Integer) userObject.get(3)));
                    userAccount.setLastLogin(((Objects.isNull(userObject.get(4))) ? new Date(System.currentTimeMillis()) : new Date(((Timestamp) userObject.get(4)).getTime()))); // fix
                    userAccount.setSurname((String) userObject.get(5));
                    userAccount.setGivenName((String) userObject.get(6));
                    userAccount.setExpiryDate(((Objects.isNull(userObject.get(7))) ? new Date(System.currentTimeMillis()) : new Date(((Timestamp) userObject.get(7)).getTime()))); // fix
                    userAccount.setSuspended(((Objects.isNull(userObject.get(8))) ? Boolean.FALSE : (Boolean) userObject.get(8)));
                    userAccount.setDisplayName((String) userObject.get(11));
                    userAccount.setAcceptedTerms((Boolean) userObject.get(12));
                    userAccount.setEmailAddr((String) userObject.get(14));
                    userAccount.setTelephoneNumber((String) userObject.get(15));
                    userAccount.setPagerNumber((String) userObject.get(16));
                    userAccount.setUserGroups(userGroups);
                    userAccount.setAuthToken(authToken);

		            if (DEBUG)
		            {
		                DEBUGGER.debug("UserAccount: {}", userAccount);
		            }
		
		            // have a user account, run with it
		            if ((userAccount.getExpiryDate().before(new Date(System.currentTimeMillis())) || (userAccount.getExpiryDate().equals(new Date(System.currentTimeMillis())))))
		            {
		                userAccount.setStatus(LoginStatus.EXPIRED);
		
		                response.setRequestStatus(SecurityRequestStatus.SUCCESS);
		                response.setUserAccount(userAccount);
		            }
		            else
		            {
		                userAccount.setLastLogin(new Date(System.currentTimeMillis()));
		                userAccount.setStatus(LoginStatus.SUCCESS);
		
		                response.setRequestStatus(SecurityRequestStatus.SUCCESS);
		                response.setUserAccount(userAccount);
		            }

		            if (DEBUG)
		            {
		                DEBUGGER.debug("UserAccount: {}", userAccount);
		                DEBUGGER.debug("AuthenticationResponse: {}", response);
		            }
	            }
            }
        }
        catch (final SecurityServiceException ssx)
        {
            ERROR_RECORDER.error(ssx.getMessage(), ssx);

            throw new AuthenticationException(ssx.getMessage(), ssx);
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AuthenticationException(sqx.getMessage(), sqx);
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
	                auditEntry.setAuditType(AuditType.LOGON);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(authSec.getSessionId());
                    auditEntry.setUserGuid(userGuid);
                    auditEntry.setUserName(userId);
                    auditEntry.setUserRole(SecurityUserRole.NONE.name());
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

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAuthenticationProcessor#processAgentLogoff(com.cws.esolutions.security.processors.dto.AuthenticationRequest)
     */
    public AuthenticationResponse processAgentLogoff(final AuthenticationRequest request) throws AuthenticationException
    {
        final String methodName = AuthenticationProcessorImpl.CNAME + "#processAgentLogoff(final AuthenticationRequest request) throws AuthenticationException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AuthenticationRequest: {}", request);
        }

        AuthenticationResponse response = new AuthenticationResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount authUser = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", authUser);
        }

        try
        {
        	String tokenSalt = userSec.getUserSalt(authUser.getGuid(), SaltType.AUTHTOKEN.name());

        	if (DEBUG)
        	{
        		DEBUGGER.debug("String: tokenSalt: {}", tokenSalt);
        	}

        	authenticator.performLogoff(authUser.getGuid(), authUser.getUsername(), tokenSalt, authUser.getAuthToken());

        	response.setRequestStatus(SecurityRequestStatus.SUCCESS);

        	if (DEBUG)
        	{
        		DEBUGGER.debug("AuthenticationResponse: {}", response);
        	}
        }
        catch (final SecurityServiceException ssx)
        {
            ERROR_RECORDER.error(ssx.getMessage(), ssx);

            throw new AuthenticationException(ssx.getMessage(), ssx);
        }
        catch (SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AuthenticationException(sqx.getMessage(), sqx);
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
	                auditEntry.setAuditType(AuditType.LOGOFF);
                    auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                    auditEntry.setSessionId(authUser.getSessionId());
                    auditEntry.setUserGuid(authUser.getGuid());
                    auditEntry.setUserName(authUser.getUsername());
                    auditEntry.setUserRole(authUser.getUserRole().toString());
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

        return response;
    }
}
