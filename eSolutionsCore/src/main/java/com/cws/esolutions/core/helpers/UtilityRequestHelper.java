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
package com.cws.esolutions.core.helpers;
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
import java.util.Arrays;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.CoreServicesConstants;
import com.cws.esolutions.security.SecurityServicesBean;
import com.cws.esolutions.security.config.xml.SecurityConfig;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.utility.services.impl.AccessControlServiceImpl;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditEntry;
import com.cws.esolutions.utility.securityutils.processors.enums.AuditType;
import com.cws.esolutions.utility.services.dto.AccessControlServiceRequest;
import com.cws.esolutions.utility.securityutils.processors.dto.AuditRequest;
import com.cws.esolutions.utility.services.dto.AccessControlServiceResponse;
import com.cws.esolutions.utility.services.interfaces.IAccessControlService;
import com.cws.esolutions.utility.securityutils.processors.impl.AuditProcessorImpl;
import com.cws.esolutions.utility.services.exception.AccessControlServiceException;
import com.cws.esolutions.utility.securityutils.processors.interfaces.IAuditProcessor;
import com.cws.esolutions.utility.securityutils.processors.exception.AuditServiceException;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public class UtilityRequestHelper
{
	static final IAuditProcessor auditor = new AuditProcessorImpl();
    static final SecurityServicesBean secBean = SecurityServicesBean.getInstance();
    static final SecurityConfig secConfig = secBean.getConfigData().getSecurityConfig();
    static final IAccessControlService accessControl = new AccessControlServiceImpl();

    static final String CNAME = UtilityRequestHelper.class.getName();

    static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    static final boolean DEBUG = DEBUGGER.isDebugEnabled();
    static final Logger ERROR_RECORDER = LogManager.getLogger(CoreServicesConstants.ERROR_LOGGER);

    private UtilityRequestHelper()
    {
    	super();
    }

	public static final boolean checkForAppropriateAccess(final String serviceId, final UserAccount userAccount) throws AccessControlServiceException
	{
        final String methodName = UtilityRequestHelper.CNAME + "#checkForAppropriateAccess(final String serviceId, final UserAccount userAccount)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("serviceId: {}", serviceId);
            DEBUGGER.debug("userAccount: {}", userAccount);
        }

        AccessControlServiceRequest accessRequest = new AccessControlServiceRequest();
        accessRequest.setServiceGuid(serviceId);
        accessRequest.setUserAccount(
        		new ArrayList<>(
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

        return accessResponse.getIsUserAuthorized();
	}

	public static final void doAuditEntry(final AuditType auditType, final UserAccount userAccount, final RequestHostInfo reqInfo, final boolean isAuthorized, final String applicationId, final String applicationName)
	{
        if (secConfig.getPerformAudit())
        {
            // audit if a valid account. if not valid we cant audit much,
            // but we should try anyway. not sure how thats going to work
            try
            {
                AuditEntry auditEntry = new AuditEntry();
                auditEntry.setAuditType(auditType);
                auditEntry.setAuditDate(new Date(System.currentTimeMillis()));
                auditEntry.setSessionId(userAccount.getSessionId());
                auditEntry.setUserGuid(userAccount.getGuid());
                auditEntry.setUserName(userAccount.getUsername());
                auditEntry.setUserRole(userAccount.getUserRole().toString());
                auditEntry.setAuthorized(isAuthorized);
                auditEntry.setApplicationId(applicationId);
                auditEntry.setApplicationName(applicationName);

                if (DEBUG)
                {
                    DEBUGGER.debug("AuditEntry: {}", auditEntry);
                }

                List<String> auditHostInfo = new ArrayList<>(
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
}
