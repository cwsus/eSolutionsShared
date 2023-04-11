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
package com.cws.esolutions.core.processors.impl;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.processors.interfaces
 * File: VirtualServiceManager.java
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.SQLException;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.processors.dto.Article;
import com.cws.esolutions.core.enums.CoreServicesStatus;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest;
import com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditEntry;
import com.cws.esolutions.utility.services.dto.AccessControlServiceRequest;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditRequest;
import com.cws.esolutions.utility.services.dto.AccessControlServiceResponse;
import com.cws.esolutions.core.processors.exception.KnowledgeManagementException;
import com.cws.esolutions.security.dao.usermgmt.exception.UserManagementException;
import com.cws.esolutions.core.processors.interfaces.IKnowledgeManagementProcessor;
import com.cws.esolutions.utility.services.exception.AccessControlServiceException;
import com.cws.esolutions.utility.securityutils.processors.exception.AuditServiceException;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public class KnowledgeManagementProcessorImpl implements IKnowledgeManagementProcessor
{
	private static final String CNAME = KnowledgeManagementProcessorImpl.class.getName();

	public final KnowledgeManagementResponse addArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#addArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.ADDARTICLE);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            String articleId = UUID.randomUUID().toString();

            if (DEBUG)
            {
                DEBUGGER.debug("articleId: {}", articleId);
            }

            List<String> articeData = new ArrayList<String>(
            		Arrays.asList(
            				articleId,
            				userAccount.getGuid(),
            				article.getKeywords(),
            				article.getTitle(),
            				article.getSymptoms(),
            				article.getCause(),
            				article.getResolution()));

            if (DEBUG)
            {
                DEBUGGER.debug("articeData: {}", articeData);
            }

            boolean isComplete = dao.addArticle(articeData);

            if (DEBUG)
            {
            	DEBUGGER.debug("isComplete: {}", isComplete);
            }

            if (isComplete)
            {
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	response.setRequestStatus(CoreServicesStatus.FAILURE);
            }

            if (DEBUG)
            {
                DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.ADDARTICLE);
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

	@Override
	public final KnowledgeManagementResponse updateArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#addArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.UPDATEARTICLE);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            List<String> articeData = new ArrayList<String>(
            		Arrays.asList(
            				article.getArticleId(),
            				article.getKeywords(),
            				article.getTitle(),
            				article.getSymptoms(),
            				article.getCause(),
            				article.getResolution(),
            				userAccount.getGuid()));

            if (DEBUG)
            {
                DEBUGGER.debug("articeData: {}", articeData);
            }

            boolean isComplete = dao.updateArticle(articeData);

            if (DEBUG)
            {
            	DEBUGGER.debug("isComplete: {}", isComplete);
            }

            if (isComplete)
            {
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	response.setRequestStatus(CoreServicesStatus.FAILURE);
            }

            if (DEBUG)
            {
                DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.UPDATEARTICLE);
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

	public final KnowledgeManagementResponse updateArticleStatus(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#updateArticleStatus(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.UPDATEARTICLESTATUS);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            boolean isComplete = dao.updateArticleStatus(article.getArticleId(), userAccount.getGuid(), article.getStatus().name());

            if (DEBUG)
            {
            	DEBUGGER.debug("isComplete: {}", isComplete);
            }

            if (isComplete)
            {
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	response.setRequestStatus(CoreServicesStatus.FAILURE);
            }

            if (DEBUG)
            {
                DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.UPDATEARTICLESTATUS);
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

	@Override
	public final KnowledgeManagementResponse deleteArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#deleteArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.DELETEARTICLE);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            boolean isComplete = dao.removeArticle(article.getArticleId(), userAccount.getGuid());

            if (DEBUG)
            {
            	DEBUGGER.debug("isComplete: {}", isComplete);
            }

            if (isComplete)
            {
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	response.setRequestStatus(CoreServicesStatus.FAILURE);
            }

            if (DEBUG)
            {
                DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.DELETEARTICLE);
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

	@Override
	public final KnowledgeManagementResponse listArticles(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#listArticles(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.LISTARTICLES);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            List<String[]> articleData = dao.listArticles(request.getStartPage());

            if (DEBUG)
            {
            	DEBUGGER.debug("List<String>: {}", articleData);
            }

            if ((Objects.isNull(articleData)) || (articleData.size() == 0))
            {
            	response.setEntryCount(0);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	List<Article> responseArticles = new ArrayList<Article>();

            	for (int x = 0; x < articleData.size(); x++)
            	{
            		Article resArticle = new Article();
            		resArticle.setArticleId(articleData.get(x)[0]);
            		resArticle.setTitle(articleData.get(x)[1]);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("Article: {}", resArticle);
            		}

            		responseArticles.add(resArticle);
            	}

            	response.setArticleList(responseArticles);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }

            if (DEBUG)
            {
            	DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.LISTARTICLES);
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

	public final KnowledgeManagementResponse listArticlesByAttribute(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#listArticlesByAttribute(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.SEARCHARTICLES);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            List<String[]> articleData = dao.getArticlesByAttribute(article.getSearchTerms(), 0);

            if (DEBUG)
            {
            	DEBUGGER.debug("List<String>: {}", articleData);
            }

            if ((Objects.isNull(articleData)) || (articleData.size() == 0))
            {
            	response.setEntryCount(0);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	List<Article> responseArticles = new ArrayList<Article>();

            	for (int x = 0; x < articleData.size(); x++)
            	{
            		Article resArticle = new Article();
            		resArticle.setArticleId(articleData.get(x)[0]);
            		resArticle.setTitle(articleData.get(x)[1]);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("Article: {}", resArticle);
            		}

            		responseArticles.add(resArticle);
            	}

            	response.setArticleList(responseArticles);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }

            if (DEBUG)
            {
            	DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.SEARCHARTICLES);
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

	public final KnowledgeManagementResponse listPendingArticles(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#listPendingArticles(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.LISTPENDING);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            List<String[]> articleData = dao.getArticlesByAttribute(article.getStatus().toString(), 0);

            if (DEBUG)
            {
            	DEBUGGER.debug("List<String>: {}", articleData);
            }

            if ((Objects.isNull(articleData)) || (articleData.size() == 0))
            {
            	response.setEntryCount(0);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	List<Article> responseArticles = new ArrayList<Article>();

            	for (int x = 0; x < articleData.size(); x++)
            	{
            		Article resArticle = new Article();
            		resArticle.setArticleId(articleData.get(x)[0]);
            		resArticle.setTitle(articleData.get(x)[1]);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("Article: {}", resArticle);
            		}

            		responseArticles.add(resArticle);
            	}

            	response.setArticleList(responseArticles);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }

            if (DEBUG)
            {
            	DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.LISTPENDING);
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

	public final KnowledgeManagementResponse getArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException
	{
        final String methodName = KnowledgeManagementProcessorImpl.CNAME + "#getArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("KnowledgeManagementRequest: {}", request);
        }

        KnowledgeManagementResponse response = new KnowledgeManagementResponse();

        final Article article = request.getArticle();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Article: {}", article);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setUserAccount(new ArrayList<String>(Arrays.asList(userAccount.getGuid(), userAccount.getUserRole().toString())));

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
                response.setRequestStatus(CoreServicesStatus.UNAUTHORIZED);

                // audit
                if (secConfig.getPerformAudit())
                {
                    // audit if a valid account. if not valid we cant audit much,
                    // but we should try anyway. not sure how thats going to work
                    try
                    {
                        AuditEntry auditEntry = new AuditEntry();
                        auditEntry.setAuditType(AuditType.VIEWARTICLE);
                        auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                        auditEntry.setSessionId(userAccount.getSessionId());
                        auditEntry.setUserGuid(userAccount.getGuid());
                        auditEntry.setUserName(userAccount.getUsername());
                        auditEntry.setUserRole(userAccount.getUserRole().toString());
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

                return response;
            }

            List<Object> articleData = dao.getArticle(article.getArticleId());

            if (DEBUG)
            {
            	DEBUGGER.debug("List<String>: {}", articleData);
            }

            if ((Objects.isNull(articleData)) || (articleData.size() == 0))
            {
            	System.out.println(articleData);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
            	List<Object> authorData = userManager.loadUserAccount((String) articleData.get(2));
            	List<Object> modifyData = userManager.loadUserAccount((String) articleData.get(10));
            	List<Object> approveData = userManager.loadUserAccount((String) articleData.get(13));

            	if (DEBUG)
            	{
            		DEBUGGER.debug("UserData: {}", authorData);
            		DEBUGGER.debug("UserData: {}", modifyData);
            		DEBUGGER.debug("UserData: {}", approveData);
            	}

            	if ((Objects.isNull(authorData)) || (authorData.size() == 0))
            	{
            		throw new KnowledgeManagementException("No author information could be found. Unable to load article.");
            	}

            	if ((Objects.isNull(modifyData)) || (modifyData.size() == 0))
            	{
            		throw new KnowledgeManagementException("No modifier information could be found. Unable to load article.");
            	}

            	if ((Objects.isNull(approveData)) || (approveData.size() == 0))
            	{
            		throw new KnowledgeManagementException("No approval information could be found. Unable to load article.");
            	}

            	UserAccount authorAccount = new UserAccount();
            	authorAccount.setGuid((String) authorData.get(1));
            	authorAccount.setUsername((String) authorData.get(0));
            	authorAccount.setDisplayName((String) authorData.get(11));

            	if (DEBUG)
            	{
            		DEBUGGER.debug("UserAccount: {}", authorAccount);
            	}

            	UserAccount modifiedAccount = new UserAccount();
            	modifiedAccount.setGuid((String) modifyData.get(1));
            	modifiedAccount.setUsername((String) modifyData.get(0));
            	modifiedAccount.setDisplayName((String) modifyData.get(11));

            	if (DEBUG)
            	{
            		DEBUGGER.debug("UserAccount: {}", modifiedAccount);
            	}

            	UserAccount approveAccount = new UserAccount();
            	approveAccount.setGuid((String) approveData.get(1));
            	approveAccount.setUsername((String) approveData.get(0));
            	approveAccount.setDisplayName((String) approveData.get(11));

            	if (DEBUG)
            	{
            		DEBUGGER.debug("UserAccount: {}", approveAccount);
            	}

            	Article responseArticle = new Article();
            	responseArticle.setArticleId((String) articleData.get(0));
            	responseArticle.setCreateDate(new Date(((Timestamp) articleData.get(1)).getTime()));
            	responseArticle.setAuthor(authorAccount);
            	responseArticle.setKeywords((String) articleData.get(3));
            	responseArticle.setTitle((String) articleData.get(4));
            	responseArticle.setSymptoms((String) articleData.get(5));
            	responseArticle.setCause((String) articleData.get(6));
            	responseArticle.setResolution((String) articleData.get(7));
            	responseArticle.setReviewedBy((String) articleData.get(8));
            	responseArticle.setReviewDate(new Date(((Timestamp) articleData.get(9)).getTime()));
            	responseArticle.setModifiedBy(modifiedAccount);
            	responseArticle.setModifyDate(new Date(((Timestamp) articleData.get(11)).getTime()));
            	responseArticle.setApproveDate(new Date(((Timestamp) articleData.get(12)).getTime()));
            	responseArticle.setApprovedBy(approveAccount);

            	if (DEBUG)
            	{
            		DEBUGGER.debug("responseArticle: {}", responseArticle);
            	}

            	response.setArticle(responseArticle);
            	response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }

            if (DEBUG)
            {
            	DEBUGGER.debug("KnowledgeManagementResponse: {}", response);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new KnowledgeManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new KnowledgeManagementException(acsx.getMessage(), acsx);
        }
        catch (UserManagementException umx)
        {
            ERROR_RECORDER.error(umx.getMessage(), umx);
            
            throw new KnowledgeManagementException(umx.getMessage(), umx);
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
                    auditEntry.setAuditType(AuditType.VIEWARTICLE);
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
