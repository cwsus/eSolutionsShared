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
package com.cws.esolutions.core.processors.interfaces;
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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.core.CoreServicesBean;
import com.cws.esolutions.core.CoreServicesConstants;
import com.cws.esolutions.security.SecurityServiceBean;
import com.cws.esolutions.core.config.xml.ApplicationConfig;
import com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl;
import com.cws.esolutions.security.config.xml.SecurityConfig;
import com.cws.esolutions.security.dao.usermgmt.factory.UserManagerFactory;
import com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager;
import com.cws.esolutions.core.dao.interfaces.IKnowledgeDataDAO;
import com.cws.esolutions.utility.services.impl.AccessControlServiceImpl;
import com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest;
import com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse;
import com.cws.esolutions.utility.services.interfaces.IAccessControlService;
import com.cws.esolutions.core.processors.exception.KnowledgeManagementException;
import com.cws.esolutions.utility.securityutils.processors.impl.AuditProcessorImpl;
import com.cws.esolutions.utility.securityutils.processors.interfaces.IAuditProcessor;
/**
 * API allowing application management functionality
 *
 * @author cws-khuntly
 * @version 1.0
 */
public interface IKnowledgeManagementProcessor
{
    static final CoreServicesBean appBean = CoreServicesBean.getInstance();
    static final SecurityServiceBean secBean = SecurityServiceBean.getInstance();
    static final SecurityConfig secConfig = secBean.getConfigData().getSecurityConfig();
    static final ApplicationConfig appConfig = appBean.getConfigData().getAppConfig();
    static final IAuditProcessor auditor = (IAuditProcessor) new AuditProcessorImpl();
    static final IKnowledgeDataDAO dao = (IKnowledgeDataDAO) new KnowledgeDataDAOImpl();
    static final UserManager userManager = (UserManager) UserManagerFactory.getUserManager(secConfig.getUserManager());
    static final IAccessControlService accessControl = (IAccessControlService) new AccessControlServiceImpl();

    static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    static final boolean DEBUG = DEBUGGER.isDebugEnabled();
    static final Logger ERROR_RECORDER = LogManager.getLogger(CoreServicesConstants.ERROR_LOGGER);

    /**
     * Allows addition of a new application to the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse addArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * Allows updates to aa application in the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse updateArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * Allows updates to aa application in the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse updateArticleStatus(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * Allows removal of an application in the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse deleteArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * Lists all applications housed within the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse listArticles(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * Lists all applications with the provided attributes housed within the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse listArticlesByAttribute(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * 
     * @param request
     * @return
     * @throws KnowledgeManagementException
     */
    KnowledgeManagementResponse listPendingArticles(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * Obtains detailed information regarding a provided application housed within the service datastore
     *
     * @param request - The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest}
     * housing the necessary data to process
     * @return The {@link com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse} containing the response information, or error code
     * @throws KnowledgeManagementException {@link com.cws.esolutions.core.processors.exception.KnowledgeManagementException} if an error occurs during processing
     */
    KnowledgeManagementResponse getArticleData(final KnowledgeManagementRequest request) throws KnowledgeManagementException;

    /**
     * 
     * @param request
     * @return
     * @throws KnowledgeManagementException
     */
    KnowledgeManagementResponse getArticleForApproval(final KnowledgeManagementRequest request) throws KnowledgeManagementException;
}
