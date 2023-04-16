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
 * Package: com.cws.esolutions.core.processors.impl
 * File: ServerManagementProcessorImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.UUID;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.processors.dto.Server;
import com.cws.esolutions.core.CoreServicesConstants;
import com.cws.esolutions.core.enums.CoreServicesStatus;
import com.cws.esolutions.core.processors.enums.ServerType;
import com.cws.esolutions.core.processors.enums.ServerStatus;
import com.cws.esolutions.core.processors.enums.ServiceRegion;
import com.cws.esolutions.security.enums.SecurityRequestStatus;
import com.cws.esolutions.core.processors.enums.NetworkPartition;
import com.cws.esolutions.core.processors.dto.ServerManagementRequest;
import com.cws.esolutions.core.processors.dto.ServerManagementResponse;
import com.cws.esolutions.security.processors.dto.AccountControlRequest;
import com.cws.esolutions.security.processors.dto.AccountControlResponse;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditEntry;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
import com.cws.esolutions.utility.services.dto.AccessControlServiceRequest;
import com.cws.esolutions.utility.services.dto.AccessControlServiceResponse;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditRequest;
import com.cws.esolutions.core.processors.exception.ServerManagementException;
import com.cws.esolutions.security.processors.impl.AccountControlProcessorImpl;
import com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor;
import com.cws.esolutions.security.processors.exception.AccountControlException;
import com.cws.esolutions.security.processors.interfaces.IAccountControlProcessor;
import com.cws.esolutions.utility.services.exception.AccessControlServiceException;
import com.cws.esolutions.utility.securityutils.processors.exception.AuditServiceException;
/**
 * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor
 */
public class ServerManagementProcessorImpl implements IServerManagementProcessor
{
    /**
     * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor#addNewServer(com.cws.esolutions.core.processors.dto.ServerManagementRequest)
     */
    public ServerManagementResponse addNewServer(final ServerManagementRequest request) throws ServerManagementException
    {
        final String methodName = IServerManagementProcessor.CNAME + "#addNewServer(final ServerManagementRequest request) throws ServerManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServerManagementRequest: {}", request);
        }

        ServerManagementResponse response = new ServerManagementResponse();

        final Server requestServer = request.getTargetServer();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Server: {}", requestServer);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setServiceGuid(request.getServiceId());
            accessRequest.setUserAccount(
            		new ArrayList<Object>(
            				Arrays.asList(
            						userAccount.getGuid(),
            						userAccount.getUserRole().toString(),
            						userAccount.getUserGroups())));

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
                        auditEntry.setAuditType(AuditType.ADDSERVER);
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

            if (Objects.isNull(requestServer))
            {
                throw new ServerManagementException("No server was provided. Cannot continue.");
            }

            // valid server
            if ((requestServer.getServerType() == ServerType.VIRTUALHOST) || (requestServer.getServerType() == ServerType.DMGRSERVER))
            {
                if (StringUtils.isEmpty(requestServer.getMgrUrl()))
                {
                    throw new ServerManagementException("Server type provided was " + requestServer.getServerType() + " but additional information is required.");
                }
                else if ((requestServer.getServerType() == ServerType.DMGRSERVER) && (requestServer.getDmgrPort() == 0))
                {
                    throw new ServerManagementException("Server type provided was " + requestServer.getServerType() + " but additional information is required.");
                }
            }

            List<Object> insertData = new ArrayList<Object>(
                    Arrays.asList(
                            UUID.randomUUID().toString(),
                            requestServer.getOsName(),
                            requestServer.getServerStatus().name(),
                            requestServer.getServerRegion().name(),
                            requestServer.getNetworkPartition().name(),
                            requestServer.getDatacenter().getGuid(),
                            requestServer.getServerType().name(),
                            requestServer.getDomainName(),
                            requestServer.getCpuType(),
                            requestServer.getCpuCount(),
                            requestServer.getServerModel(),
                            requestServer.getSerialNumber(),
                            requestServer.getInstalledMemory(),
                            requestServer.getOperIpAddress(),
                            requestServer.getOperHostName(),
                            (StringUtils.isNotEmpty(requestServer.getMgmtIpAddress())) ? requestServer.getMgmtIpAddress() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getMgmtHostName())) ? requestServer.getMgmtHostName() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getBkIpAddress())) ? requestServer.getBkIpAddress() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getBkHostName())) ? requestServer.getBkHostName() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getNasIpAddress())) ? requestServer.getNasIpAddress() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getNasHostName())) ? requestServer.getNasHostName() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getNatAddress())) ? requestServer.getNatAddress() : CoreServicesConstants.NOT_SET,
                            requestServer.getServerComments(),
                            userAccount.getGuid(),
                            (StringUtils.isNotBlank(requestServer.getMgrUrl())) ? requestServer.getMgrUrl() : CoreServicesConstants.NOT_SET,
                            requestServer.getDmgrPort(),
                            (StringUtils.isNotBlank(requestServer.getServerRack())) ? requestServer.getServerRack() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotBlank(requestServer.getRackPosition())) ? requestServer.getRackPosition() : CoreServicesConstants.NOT_SET,
                            (requestServer.getOwningDmgr() != null) ? requestServer.getOwningDmgr().getServerGuid() : CoreServicesConstants.NOT_SET));

            if (DEBUG)
            {
                for (Object str : insertData)
                {
                    DEBUGGER.debug("Value: {}", str);
                }
            }

            boolean isComplete = serverDAO.addServer(insertData);

            if (DEBUG)
            {
                DEBUGGER.debug("isComplete: {}", isComplete);
            }

            if (isComplete)
            {
                // install agent
                response.setRequestStatus(CoreServicesStatus.SUCCESS);
            }
            else
            {
                response.setRequestStatus(CoreServicesStatus.FAILURE);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new ServerManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);

            throw new ServerManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.ADDSERVER);
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

    /**
     * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor#updateServerData(com.cws.esolutions.core.processors.dto.ServerManagementRequest)
     */
    public ServerManagementResponse updateServerData(final ServerManagementRequest request) throws ServerManagementException
    {
        final String methodName = IServerManagementProcessor.CNAME + "#updateServerData(final ServerManagementRequest request) throws ServerManagementException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServerManagementRequest: {}", request);
        }

        ServerManagementResponse response = new ServerManagementResponse();

        final Server requestServer = request.getTargetServer();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Server: {}", requestServer);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setServiceGuid(request.getServiceId());
            accessRequest.setUserAccount(
            		new ArrayList<Object>(
            				Arrays.asList(
            						userAccount.getGuid(),
            						userAccount.getUserRole().toString(),
            						userAccount.getUserGroups())));

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
                        auditEntry.setAuditType(AuditType.UPDATESERVER);
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

            if ((requestServer.getServerType() == ServerType.VIRTUALHOST) || (requestServer.getServerType() == ServerType.DMGRSERVER))
            {
                if (StringUtils.isEmpty(requestServer.getMgrUrl()))
                {
                    throw new ServerManagementException("Server type provided was " + requestServer.getServerType() + " but additional information is required.");
                }
                else if ((requestServer.getServerType() == ServerType.DMGRSERVER) && (requestServer.getDmgrPort() == 0))
                {
                    throw new ServerManagementException("Server type provided was " + requestServer.getServerType() + " but additional information is required.");
                }
            }

            List<Object> insertData = new ArrayList<Object>(
                    Arrays.asList(
                            requestServer.getOsName(),
                            requestServer.getServerStatus().name(),
                            requestServer.getServerRegion().name(),
                            requestServer.getNetworkPartition().name(),
                            requestServer.getServerType().name(),
                            requestServer.getDomainName(),
                            requestServer.getCpuType(),
                            requestServer.getCpuCount(),
                            requestServer.getServerModel(),
                            requestServer.getSerialNumber(),
                            requestServer.getInstalledMemory(),
                            requestServer.getOperIpAddress(),
                            requestServer.getOperHostName(),
                            (StringUtils.isNotEmpty(requestServer.getMgmtIpAddress())) ? requestServer.getMgmtIpAddress() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getMgmtHostName())) ? requestServer.getMgmtHostName() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getBkIpAddress())) ? requestServer.getBkIpAddress() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getBkHostName())) ? requestServer.getBkHostName() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getNasIpAddress())) ? requestServer.getNasIpAddress() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getNasHostName())) ? requestServer.getNasHostName() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotEmpty(requestServer.getNatAddress())) ? requestServer.getNatAddress() : CoreServicesConstants.NOT_SET,
                            requestServer.getServerComments(),
                            requestServer.getAssignedEngineer().getGuid(),
                            (StringUtils.isNotBlank(requestServer.getMgrUrl())) ? requestServer.getMgrUrl() : CoreServicesConstants.NOT_SET,
                            requestServer.getDmgrPort(),
                            (StringUtils.isNotBlank(requestServer.getServerRack())) ? requestServer.getServerRack() : CoreServicesConstants.NOT_SET,
                            (StringUtils.isNotBlank(requestServer.getRackPosition())) ? requestServer.getRackPosition() : CoreServicesConstants.NOT_SET,
                            (requestServer.getOwningDmgr() != null) ? requestServer.getOwningDmgr().getServerGuid() : CoreServicesConstants.NOT_SET));

            if (DEBUG)
            {
                for (Object str : insertData)
                {
                    DEBUGGER.debug("Value: {}", str);
                }
            }

            boolean isComplete = serverDAO.updateServer(requestServer.getServerGuid(), insertData);

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
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);

            throw new ServerManagementException(acsx.getMessage(), acsx);
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new ServerManagementException(sqx.getMessage(), sqx);
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
                    auditEntry.setAuditType(AuditType.UPDATESERVER);
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

    /**
     * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor#removeServerData(com.cws.esolutions.core.processors.dto.ServerManagementRequest)
     */
    public ServerManagementResponse removeServerData(final ServerManagementRequest request) throws ServerManagementException
    {
        final String methodName = IServerManagementProcessor.CNAME + "#removeServerData(final ServerManagementRequest request) throws ServerManagementException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServerManagementRequest: {}", request);
        }

        ServerManagementResponse response = new ServerManagementResponse();

        final Server requestServer = request.getTargetServer();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("Server: {}", requestServer);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setServiceGuid(request.getServiceId());
            accessRequest.setUserAccount(
            		new ArrayList<Object>(
            				Arrays.asList(
            						userAccount.getGuid(),
            						userAccount.getUserRole().toString(),
            						userAccount.getUserGroups())));

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
                        auditEntry.setAuditType(AuditType.DELETESERVER);
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
            }

            boolean isComplete = serverDAO.removeServer(requestServer.getServerGuid());

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
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);

            throw new ServerManagementException(acsx.getMessage(), acsx);
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new ServerManagementException(sqx.getMessage(), sqx);
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
                    auditEntry.setAuditType(AuditType.DELETESERVER);
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

    /**
     * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor#listServers(com.cws.esolutions.core.processors.dto.ServerManagementRequest)
     */
    public ServerManagementResponse listServers(final ServerManagementRequest request) throws ServerManagementException
    {
        final String methodName = IServerManagementProcessor.CNAME + "#listServers(final ServerManagementRequest request) throws ServerManagementException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServerManagementRequest: {}", request);
        }

        ServerManagementResponse response = new ServerManagementResponse();

        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setServiceGuid(request.getServiceId());
            accessRequest.setUserAccount(
            		new ArrayList<Object>(
            				Arrays.asList(
            						userAccount.getGuid(),
            						userAccount.getUserRole().toString(),
            						userAccount.getUserGroups())));

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
                        auditEntry.setAuditType(AuditType.LISTSERVERS);
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

            List<String[]> serverData = serverDAO.listServers(request.getStartPage());

            if (DEBUG)
            {
                DEBUGGER.debug("serverList: {}", serverData);
            }

            if ((serverData != null) && (serverData.size() != 0))
            {
                List<Server> serverList = new ArrayList<Server>();

                for (String[] data : serverData)
                {
                    Server server = new Server();
                    server.setServerGuid(data[0]); // SYSTEM_GUID
                    server.setOperHostName(data[1]); // OPER_HOSTNAME
                    

                    if (DEBUG)
                    {
                        DEBUGGER.debug("Server: {}", server);
                    }

                    serverList.add(server);
                }

                if (DEBUG)
                {
                    DEBUGGER.debug("serverList: {}", serverList);
                }

                // response.setEntryCount(value);
                response.setRequestStatus(CoreServicesStatus.SUCCESS);
                response.setServerList(serverList);
            }
            else
            {
                response.setRequestStatus(CoreServicesStatus.FAILURE);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new ServerManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new ServerManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.LISTSERVERS);
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

    /**
     * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor#listServersByAttribute(com.cws.esolutions.core.processors.dto.ServerManagementRequest)
     */
    public ServerManagementResponse listServersByAttribute(final ServerManagementRequest request) throws ServerManagementException
    {
        final String methodName = IServerManagementProcessor.CNAME + "#listServersByAttribute(final ServerManagementRequest request) throws ServerManagementException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServerManagementRequest: {}", request);
        }

        ServerManagementResponse response = new ServerManagementResponse();

        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();

        if (DEBUG)
        {
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setServiceGuid(request.getServiceId());
            accessRequest.setUserAccount(
            		new ArrayList<Object>(
            				Arrays.asList(
            						userAccount.getGuid(),
            						userAccount.getUserRole().toString(),
            						userAccount.getUserGroups())));

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
                        auditEntry.setAuditType(AuditType.LISTSERVERS);
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

            List<String[]> serverData = serverDAO.getServersByAttribute(request.getAttribute(), request.getStartPage());

            if (DEBUG)
            {
                DEBUGGER.debug("serverList: {}", serverData);
            }

            if ((serverData != null) && (serverData.size() != 0))
            {
                List<Server> serverList = new ArrayList<Server>();

                for (Object[] data : serverData)
                {
                    Server server = new Server();
                    server.setServerGuid((String) data[0]); // SYSTEM_GUID
                    server.setOperHostName((String) data[1]); // OPER_HOSTNAME

                    if (DEBUG)
                    {
                        DEBUGGER.debug("Server: {}", server);
                    }

                    serverList.add(server);
                }

                if (DEBUG)
                {
                    DEBUGGER.debug("serverList: {}", serverList);
                }

                //response.setEntryCount(value);
                response.setRequestStatus(CoreServicesStatus.SUCCESS);
                response.setServerList(serverList);
            }
            else
            {
                response.setRequestStatus(CoreServicesStatus.FAILURE);
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new ServerManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new ServerManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.LISTSERVERS);
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

    /**
     * @see com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor#getServerData(com.cws.esolutions.core.processors.dto.ServerManagementRequest)
     */
    public ServerManagementResponse getServerData(final ServerManagementRequest request) throws ServerManagementException
    {
        final String methodName = IServerManagementProcessor.CNAME + "#getServerData(final ServerManagementRequest request) throws ServerManagementException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServerManagementRequest: {}", request);
        }

        UserAccount searchAccount = null;
        AccountControlRequest searchRequest = null;
        AccountControlResponse searchResponse = null;
        ServerManagementResponse response = new ServerManagementResponse();

        final Server requestServer = request.getTargetServer();
        final UserAccount userAccount = request.getUserAccount();
        final RequestHostInfo reqInfo = request.getRequestInfo();
        final IAccountControlProcessor acctControl = new AccountControlProcessorImpl();

        if (DEBUG)
        {
            DEBUGGER.debug("Server: {}", requestServer);
            DEBUGGER.debug("UserAccount: {}", userAccount);
            DEBUGGER.debug("RequestHostInfo: {}", reqInfo);
        }

        try
        {
            // this will require admin and service authorization
            AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
            accessRequest.setServiceGuid(request.getServiceId());
            accessRequest.setUserAccount(
            		new ArrayList<Object>(
            				Arrays.asList(
            						userAccount.getGuid(),
            						userAccount.getUserRole().toString(),
            						userAccount.getUserGroups())));

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
                        auditEntry.setAuditType(AuditType.GETSERVER);
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

            if (requestServer != null)
            {
                List<Object> serverData = serverDAO.getServer(requestServer.getServerGuid());

                if (DEBUG)
                {
                    DEBUGGER.debug("serverData: {}", serverData);
                }

                if ((serverData != null) && (serverData.size() != 0))
                {
                    Server server = new Server();
                    server.setServerGuid((String) serverData.get(0)); // SYSTEM_GUID
                    server.setOsName((String) serverData.get(1)); // SYSTEM_OSTYPE
                    server.setServerStatus(ServerStatus.valueOf((String) serverData.get(2))); // SYSTEM_STATUS
                    server.setServerRegion(ServiceRegion.valueOf((String) serverData.get(3))); // SYSTEM_REGION
                    server.setNetworkPartition(NetworkPartition.valueOf((String) serverData.get(4))); // NETWORK_PARTITION
                    server.setServerType(ServerType.valueOf((String) serverData.get(5))); // SYSTEM_TYPE
                    server.setDomainName((String) serverData.get(6)); // DOMAIN_NAME
                    server.setCpuType((String) serverData.get(7)); // CPU_TYPE
                    server.setCpuCount((Integer) serverData.get(8)); // CPU_COUNT
                    server.setServerRack((String) serverData.get(9)); // SERVER_RACK
                    server.setRackPosition((String) serverData.get(10)); // RACK_POSITION
                    server.setServerModel((String) serverData.get(11)); // SERVER_MODEL
                    server.setSerialNumber((String) serverData.get(12)); // SERIAL_NUMBER
                    server.setInstalledMemory((Integer) serverData.get(13)); // INSTALLED_MEMORY
                    server.setOperIpAddress((String) serverData.get(14)); // OPER_IP
                    server.setOperHostName((String) serverData.get(15)); // OPER_HOSTNAME
                    server.setMgmtIpAddress((String) serverData.get(16)); // MGMT_IP
                    server.setMgmtHostName((String) serverData.get(17)); // MGMT_HOSTNAME
                    server.setBkIpAddress((String) serverData.get(18)); // BKUP_IP
                    server.setBkHostName((String) serverData.get(19)); // BKUP_HOSTNAME
                    server.setNasIpAddress((String) serverData.get(20)); // NAS_IP
                    server.setNasHostName((String) serverData.get(21)); // NAS_HOSTNAME
                    server.setNatAddress((String) serverData.get(22)); // NAT_ADDR
                    server.setServerComments((String) serverData.get(23)); // COMMENTS

                    searchAccount = new UserAccount();
                    searchAccount.setGuid((String) serverData.get(24));

                    if (DEBUG)
                    {
                        DEBUGGER.debug("UserAccount: {}", searchAccount);
                    }

                    searchRequest = new AccountControlRequest();
                    searchRequest.setHostInfo(request.getRequestInfo());
                    searchRequest.setUserAccount(searchAccount);
                    searchRequest.setApplicationName(request.getApplicationName());
                    searchRequest.setApplicationId(request.getApplicationId());
                    searchRequest.setRequestor(userAccount);

                    if (DEBUG)
                    {
                        DEBUGGER.debug("AccountControlRequest: {}", searchRequest);
                    }

                    try
                    {
                        searchResponse = acctControl.loadUserAccount(searchRequest);

                        if (DEBUG)
                        {
                            DEBUGGER.debug("AccountControlResponse: {}", searchResponse);
                        }

                        if (searchResponse.getRequestStatus() == SecurityRequestStatus.SUCCESS)
                        {
                            server.setAssignedEngineer(searchResponse.getUserAccount()); // ASSIGNED_ENGINEER
                        }
                    }
                    catch (final AccountControlException acx)
                    {
                        ERROR_RECORDER.error(acx.getMessage(), acx);
                    }

                    switch (server.getServerType())
                    {
                        case APPSERVER:
                            // set owning dmgr
                            List<Object> dmgrData = serverDAO.getServer((String) serverData.get(28));

                            if (DEBUG)
                            {
                                DEBUGGER.debug("dmgrData: {}", dmgrData);
                            }

                            if ((dmgrData != null) && (dmgrData.size() != 0))
                            {
                                Server dmgrServer = new Server();
                                dmgrServer.setServerGuid((String) dmgrData.get(0)); // SYSTEM_GUID
                                dmgrServer.setOsName((String) dmgrData.get(1)); // SYSTEM_OSTYPE

                                if (DEBUG)
                                {
                                    DEBUGGER.debug("Server: {}", dmgrServer);
                                }

                                server.setOwningDmgr(dmgrServer); // OWNING_DMGR
                            }

                            break;
                        case DMGRSERVER:
                            server.setDmgrPort((Integer) serverData.get(27)); // DMGR_PORT
                            server.setMgrUrl((String) serverData.get(29)); // MGR_ENTRY

                            break;
                        case VIRTUALHOST:
                            server.setDmgrPort((Integer) serverData.get(27)); // DMGR_PORT
                            server.setMgrUrl((String) serverData.get(29)); // MGR_ENTRY

                            break;
                        default:
                            break;
                    }

                    if (DEBUG)
                    {
                        DEBUGGER.debug("Server: {}", server);
                    }

                    response.setRequestStatus(CoreServicesStatus.SUCCESS);
                    response.setServer(server);
                }
                else
                {
                    response.setRequestStatus(CoreServicesStatus.FAILURE);
                }
            }
            else
            {
                throw new ServerManagementException("No server search data was provided. Cannot continue");
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

            throw new ServerManagementException(sqx.getMessage(), sqx);
        }
        catch (final AccessControlServiceException acsx)
        {
            ERROR_RECORDER.error(acsx.getMessage(), acsx);
            
            throw new ServerManagementException(acsx.getMessage(), acsx);
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
                    auditEntry.setAuditType(AuditType.GETSERVER);
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
        }

        return response;
    }
}
