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
 * File: AccountResetProcessorImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;
import java.util.Arrays;
import java.util.Calendar;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.security.enums.SecurityUserRole;
import com.cws.esolutions.security.processors.enums.SaltType;
import com.cws.esolutions.utility.securityutils.PasswordUtils;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
import com.cws.esolutions.security.processors.enums.LoginStatus;
import com.cws.esolutions.security.processors.dto.AccountChangeData;
import com.cws.esolutions.security.processors.dto.AuthenticationData;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.security.exception.SecurityServiceException;
import com.cws.esolutions.security.processors.dto.AccountResetRequest;
import com.cws.esolutions.security.processors.dto.AccountResetResponse;
import com.cws.esolutions.security.processors.exception.AccountChangeException;
import com.cws.esolutions.security.processors.exception.AccountResetException;
import com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor;
import com.cws.esolutions.security.dao.userauth.exception.AuthenticatorException;
import com.cws.esolutions.security.dao.usermgmt.exception.UserManagementException;
/**
 * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor
 */
public class AccountResetProcessorImpl implements IAccountResetProcessor
{
    private static final String CNAME = AccountResetProcessorImpl.class.getName();

    public AccountResetResponse getSecurityQuestionList(final AccountResetRequest request) throws AccountResetException
    {
        final String methodName = AccountResetProcessorImpl.CNAME + "#getSecurityQuestionList(final AccountResetRequest request) throws AccountResetException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        AccountResetResponse response = new AccountResetResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
        	List<String> questionList = userSec.obtainSecurityQuestionList();

        	if (DEBUG)
        	{
        		DEBUGGER.debug("questionList: {}", questionList);
        	}

        	if (Objects.isNull(questionList))
        	{
        		response.setRequestStatus(SecurityRequestStatus.FAILURE);
        	}
        	else
        	{
        		response.setAvailableQuestions(questionList);
        		response.setRequestStatus(SecurityRequestStatus.SUCCESS);
        	}

        	if (DEBUG)
        	{
        		DEBUGGER.debug("AccountResetResponse: {}", response);
        	}
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AccountResetException(sqx.getMessage(), sqx);
        }

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor#isOnlineResetAvailable(com.cws.esolutions.security.processors.dto.AccountResetRequest)
     */
    public AccountResetResponse isOnlineResetAvailable(final AccountResetRequest request) throws AccountResetException
    {
        final String methodName = AccountResetProcessorImpl.CNAME + "#isOnlineResetAvailable(final AccountResetRequest request) throws AccountResetException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        AccountResetResponse response = new AccountResetResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount  = request.getUserAccount();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
        }

        try
        {
        	List<Boolean> authList = authenticator.getOlrStatus(reqAccount.getGuid(), reqAccount.getUsername());

        	if (DEBUG)
        	{
        		DEBUGGER.debug("AuthList: {}", authList);
        	}

        	if (Objects.isNull(authList))
        	{
        		response.setRequestStatus(SecurityRequestStatus.FAILURE);

        		return response;
        	}

        	UserAccount resAccount = new UserAccount();
        	resAccount.setUsername(reqAccount.getUsername());
        	resAccount.setGuid(reqAccount.getGuid());
        	resAccount.setStatus(LoginStatus.SUCCESS);

        	boolean isOlrSetup = authList.get(0); // isOlrSetup
        	boolean isOlrLocked = authList.get(1); // isOlrLocked

        	if (DEBUG)
        	{
        		DEBUGGER.debug("isOlrSetup: {}", isOlrSetup);
        		DEBUGGER.debug("isOlrLocked: {}", isOlrLocked);
        	}

        	if (isOlrSetup)
        	{
        		resAccount.setStatus(LoginStatus.OLRSETUP);
        	}

        	if (isOlrLocked)
        	{
        		resAccount.setStatus(LoginStatus.OLRLOCKED);
        	}

        	response.setUserAccount(resAccount);
        	response.setRequestStatus(SecurityRequestStatus.SUCCESS);
        }
        catch (final AuthenticatorException ax)
        {
            ERROR_RECORDER.error(ax.getMessage(), ax);

            throw new AccountResetException(ax.getMessage(), ax);
        }

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor#verifyUserSecurityConfig(com.cws.esolutions.security.processors.dto.AccountResetRequest)
     */
    public AccountResetResponse verifyUserSecurityConfig(final AccountResetRequest request) throws AccountResetException
    {
        final String methodName = AccountResetProcessorImpl.CNAME + "#verifyUserSecurityConfig(final AccountResetRequest request) throws AccountResetException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        AccountResetResponse response = new AccountResetResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount  = request.getUserAccount();
        final AccountChangeData changeData = request.getChangeData();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("AccountChangeData: {}", changeData);
        }

        try
        {
            String userSalt = userSec.getUserSalt(reqAccount.getGuid(), SaltType.RESET.name());

            if (DEBUG)
            {
            	DEBUGGER.debug("userSalt: {}", userSalt);
            }

            if (!(Objects.isNull(userSalt)))
            {
                String secAnswerOne = PasswordUtils.encryptText(changeData.getSecAnswerOne(), userSalt,
                        secConfig.getSecretKeyAlgorithm(),
                        secConfig.getIterations(), secConfig.getKeyLength(),
                        sysConfig.getEncoding());
                String secAnswerTwo = PasswordUtils.encryptText(changeData.getSecAnswerTwo(), userSalt,
                        secConfig.getSecretKeyAlgorithm(),
                        secConfig.getIterations(), secConfig.getKeyLength(),
                        sysConfig.getEncoding());

                if (DEBUG)
                {
                	DEBUGGER.debug("Value: {}", secAnswerTwo);
                	DEBUGGER.debug("Value: {}", secAnswerOne);
                }

            	HashMap<String, String> responseMap = new HashMap<String, String>();
            	responseMap.put(changeData.getSecQuestionOne(), secAnswerOne);
            	responseMap.put(changeData.getSecQuestionTwo(), secAnswerTwo);

            	if (DEBUG)
            	{
            		DEBUGGER.debug("HashMap<String, String>: {}", responseMap);
            	}

            	boolean isVerified = authenticator.verifySecurityData(reqAccount.getGuid(), reqAccount.getUsername(), responseMap);

                if (DEBUG)
                {
                    DEBUGGER.debug("boolean: {}", isVerified);
                }

                if (isVerified)
                {
                	// build a user account object here
                    // load the user account here
                    List<Object> authObject = userManager.loadUserAccount(reqAccount.getGuid());

                    if (DEBUG)
                    {
                    	DEBUGGER.debug("authObject: {}", authObject);
                    }

                    if (Objects.isNull(authObject))
                    {
                    	ERROR_RECORDER.error("Attempted to load user account but got no response data");

                    	response.setRequestStatus(SecurityRequestStatus.FAILURE);

                    	return response;
                    }

                    UserAccount resAccount = new UserAccount();
                    resAccount.setGuid((String) authObject.get(1));
                    resAccount.setUsername((String) authObject.get(0));
                    resAccount.setUserRole(SecurityUserRole.valueOf((String) authObject.get(2)));

                    if ((Integer) authObject.get(3) >= secConfig.getMaxAttempts())
                    {
                        // user locked
                    	resAccount.setStatus(LoginStatus.LOCKOUT);

                    	if (DEBUG)
                    	{
                    		DEBUGGER.debug("UserAccount: {}", resAccount);
                    	}

                    	response.setUserAccount(resAccount);
                    	response.setRequestStatus(SecurityRequestStatus.SUCCESS);

                        return response;
                    }

                    response.setUserAccount(resAccount);
                    response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                }
                else
                {
                    if (request.getCount() >= 3)
                    {
                        try
                        {
                            userManager.modifyOlrLock(reqAccount.getGuid(), true);
                        }
                        catch (final UserManagementException umx)
                        {
                            ERROR_RECORDER.error(umx.getMessage(), umx);
                        }
                    }

                    response.setCount(request.getCount() + 1);
                    response.setRequestStatus(SecurityRequestStatus.FAILURE);
                }
            }
            else
            {
                throw new AccountResetException("Unable to obtain user salt value. Cannot continue.");
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AccountResetException(sqx.getMessage(), sqx);
        }
        catch (final AuthenticatorException ax)
        {
            ERROR_RECORDER.error(ax.getMessage(), ax);

            throw new AccountResetException(ax.getMessage(), ax);
        }
        catch (UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountResetException(umx.getMessage(), umx);
		}

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor#insertResetRequest(com.cws.esolutions.security.processors.dto.AccountResetRequest)
     */
    public AccountResetResponse insertResetRequest(final AccountResetRequest request) throws AccountResetException
    {
        final String methodName = AccountResetProcessorImpl.CNAME + "#resetUserPassword(final AccountResetRequest request) throws AccountResetException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        AccountResetResponse response = new AccountResetResponse();

        final Calendar calendar = Calendar.getInstance();
        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getUserAccount();

        calendar.add(Calendar.DATE, secConfig.getPasswordExpiration());

        if (DEBUG)
        {
            DEBUGGER.debug("Calendar: {}", calendar);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
        }

        try
        {
            String resetId = RandomStringUtils.randomAlphanumeric(secConfig.getResetIdLength());

            if (StringUtils.isNotEmpty(resetId))
            {
                boolean isComplete = userSec.insertResetData(reqAccount.getGuid(), resetId);

                if (DEBUG)
                {
                    DEBUGGER.debug("isComplete: {}", isComplete);
                }

                if (isComplete)
                {
                    // load the user account for the email response
                    List<Object> userData = userManager.loadUserAccount(reqAccount.getGuid());

                    if (DEBUG)
                    {
                        DEBUGGER.debug("UserData: {}", userData);
                    }

                    if ((userData != null) && (!(userData.isEmpty())))
                    {
                        UserAccount responseAccount = new UserAccount();
                        responseAccount.setGuid((String) userData.get(1)); // UID
                        responseAccount.setUsername((String) userData.get(0)); // CN
                        responseAccount.setDisplayName((String) userData.get(11)); // DISPLAYNAME
                        responseAccount.setEmailAddr((String) userData.get(13)); // EMAIL

                        if (DEBUG)
                        {
                            DEBUGGER.debug("UserAccount: {}", responseAccount);
                        }

                        response.setResetId(resetId);
                        response.setUserAccount(responseAccount);
                        response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                    }
                    else
                    {
                        ERROR_RECORDER.error("Failed to locate user account in authentication repository. Cannot continue.");

                        response.setRequestStatus(SecurityRequestStatus.FAILURE);
                    }
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
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AccountResetException(sqx.getMessage(), sqx);
        }
        catch (final UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);

            throw new AccountResetException(umx.getMessage(), umx);
        }

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor#verifyResetRequest(com.cws.esolutions.security.processors.dto.AccountResetRequest)
     */
    public AccountResetResponse verifyResetRequest(final AccountResetRequest request) throws AccountResetException
    {
        final String methodName = AccountResetProcessorImpl.CNAME + "#verifyResetRequest(final AccountResetRequest request) throws AccountResetException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        AccountResetResponse response = new AccountResetResponse();

        final Calendar cal = Calendar.getInstance();
        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getUserAccount();
        final AuthenticationData authData = request.getUserSecurity();

        if (DEBUG)
        {
            DEBUGGER.debug("Calendar: {}", cal);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("AuthenticationData: {}", authData);
        }

        try
        {
            cal.add(Calendar.MINUTE, secConfig.getResetTimeout());

            if (DEBUG)
            {
                DEBUGGER.debug("Reset expiry: {}", cal.getTimeInMillis());
            }

            // the request id should be in here, so lets make sure it exists
            List<Object> resetData = userSec.getResetData(authData.getResetKey());

            if (DEBUG)
            {
            	DEBUGGER.debug("resetData: {}", resetData);
            }

            if (Objects.isNull(resetData))
            {
            	throw new AccountResetException("No reset information was found for the given information. Cannot continue.");
            }

            final String commonName = (String) resetData.get(0);
            final Date resetTimestamp = (Date) resetData.get(1);

            if (DEBUG)
            {
                DEBUGGER.debug("String: {}", commonName);
                DEBUGGER.debug("Date: {}", resetTimestamp);
            }

            // make sure the timestamp is appropriate
            if (resetTimestamp.after(cal.getTime()))
            {
                response.setRequestStatus(SecurityRequestStatus.FAILURE);

                return response;
            }

            // good, now we have something we can look for
            List<Object> userList = userManager.loadUserAccount(commonName);

            if (DEBUG)
            {
                DEBUGGER.debug("userList: {}", userList);
            }

            // we expect back only one
            if ((Objects.isNull(userList)) || (userList.size() == 0))
            {
                throw new AccountResetException("Unable to load user account information. Cannot continue.");
            }

            UserAccount foundAccount = new UserAccount();
            foundAccount.setGuid((String) userList.get(1)); // CN
            foundAccount.setUsername((String) userList.get(0)); // UID
            foundAccount.setUserRole(SecurityUserRole.valueOf((String) userList.get(2))); //cwsrole
            foundAccount.setSuspended((Boolean) userList.get(8));  // SUSPENDED

            if (DEBUG)
            {
                DEBUGGER.debug("UserAccount: {}", foundAccount);
            }

            response.setRequestStatus(SecurityRequestStatus.SUCCESS);
            response.setUserAccount(foundAccount);

            if (DEBUG)
            {
                DEBUGGER.debug("AccountResetResponse: {}", response);
            }
        }
        catch (final SecurityServiceException ssx)
        {
            ERROR_RECORDER.error(ssx.getMessage(), ssx);

            throw new AccountResetException(ssx.getMessage(), ssx);
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AccountResetException(sqx.getMessage(), sqx);
        }

        return response;
    }

    /**
     * @see com.cws.esolutions.security.processors.interfaces.IAccountResetProcessor#submitPasswordReset(com.cws.esolutions.security.processors.dto.AccountResetRequest)
     */
    public AccountResetResponse submitPasswordReset(final AccountResetRequest request) throws AccountResetException
    {
        final String methodName = AccountResetProcessorImpl.CNAME + "#submitPasswordReset(final AccountResetRequest request) throws AccountResetException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("AccountResetRequest: {}", request);
        }

        AccountResetResponse response = new AccountResetResponse();

        final RequestHostInfo reqInfo = request.getHostInfo();
        final UserAccount reqAccount = request.getUserAccount();
        final AccountChangeData changeData = request.getChangeData();

        if (DEBUG)
        {
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
            DEBUGGER.debug("UserAccount: {}", reqAccount);
            DEBUGGER.debug("AccountChangeData: {}", changeData);
        }

        try
        {
            if (!(Arrays.equals(changeData.getNewPassword(), changeData.getConfirmPassword())))
            {
                throw new AccountResetException("The new password and confirmed password must match.");
            }
            else if ((changeData.getNewPassword().length < secConfig.getPasswordMinLength()) // less than minimum
                    || (changeData.getNewPassword().length > secConfig.getPasswordMaxLength())) // greater than maximum
            {
                // password doesnt meet requirements, is either too short or too long
                throw new AccountResetException("The chosen password does not meet the configured length requirements.");
            }

            String newSalt = PasswordUtils.returnGeneratedSalt(secConfig.getRandomGenerator(), secConfig.getSaltLength());

            if (DEBUG)
            {
            	DEBUGGER.debug("newSalt: {}", newSalt);
            }

            if (StringUtils.isNotBlank(newSalt))
            {
                boolean isComplete = userSec.addOrUpdateUserSalt(reqAccount.getGuid(), newSalt, SaltType.LOGON.name());

                if (DEBUG)
                {
                	DEBUGGER.debug("isComplete: {}", isComplete);
                }

                if (isComplete)
                {
                    isComplete = userSec.modifyUserPassword(reqAccount.getGuid(), reqAccount.getUsername(),
                            PasswordUtils.encryptText(changeData.getNewPassword(), newSalt,
                            		secConfig.getSecretKeyAlgorithm(), secConfig.getIterations(), secConfig.getKeyLength(),
                            		sysConfig.getEncoding()), false);

                    if (DEBUG)
                    {
                        DEBUGGER.debug("isComplete: {}", isComplete);
                    }

                    if (isComplete)
                    {
                    	response.setUserAccount(reqAccount);
                        response.setRequestStatus(SecurityRequestStatus.SUCCESS);
                    }
                    else
                    {
                        response.setRequestStatus(SecurityRequestStatus.FAILURE);
                    }
                }
                else
                {
                    throw new AccountChangeException("Unable to obtain existing salt value from datastore. Cannot continue.");
                }
            }
            else
            {
            	throw new AccountChangeException("Unable to generate salt for the provided account. Cannot continue.");
            }

        }
        catch (final SecurityServiceException ssx)
        {
            ERROR_RECORDER.error(ssx.getMessage(), ssx);

            throw new AccountResetException(ssx.getMessage(), ssx);
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new AccountResetException(sqx.getMessage(), sqx);
        }

        return response;
    }
}
