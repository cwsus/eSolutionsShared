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
package com.cws.esolutions.core.dao.interfaces;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.dao.interfaces
 * File: IApplicationDataDAO.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import javax.sql.DataSource;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.core.CoreServicesBean;
import com.cws.esolutions.core.CoreServicesConstants;
/**
 * Interface for the Application Data DAO layer. Allows access
 * into the asset management database to obtain, modify and remove
 * application information.
 *
 * @author cws-khuntly
 * @version 1.0
 */
public interface IKnowledgeDataDAO
{
    static final CoreServicesBean appBean = CoreServicesBean.getInstance();

    static final String CNAME = IKnowledgeDataDAO.class.getName();
    static final DataSource dataSource = appBean.getDataSources().get("ApplicationDataSource");

    static final Logger ERROR_RECORDER = LogManager.getLogger(CoreServicesConstants.ERROR_LOGGER + CNAME);
    static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    /**
     * 
     * @param articleData
     * @return
     * @throws SQLException
     */
    boolean addArticle(final List<String> articleData) throws SQLException;

    /**
     * 
     * @param articleData
     * @return
     * @throws SQLException
     */
    boolean updateArticle(final List<String> articleData) throws SQLException;

    /**
     * 
     * @param articleId
     * @param modifiedBy
     * @param status
     * @return
     * @throws SQLException
     */
    boolean updateArticleStatus(final String articleId, final String modifiedBy, final String status) throws SQLException;

    /**
     * 
     * @param articleId
     * @param userId
     * @return
     * @throws SQLException
     */
    boolean removeArticle(final String articleId, final String userId) throws SQLException;

    /**
     * 
     * @param startRow
     * @return
     * @throws SQLException
     */
    List<String[]> listArticles(final int startRow) throws SQLException;

    /**
     * 
     * @param attribute
     * @param startRow
     * @return
     * @throws SQLException
     */
    List<String[]> getArticlesByAttribute(final String attribute, final int startRow) throws SQLException;

    /**
     * 
     * @param appGuid
     * @return
     * @throws SQLException
     */
    List<Object> getArticle(final String appGuid) throws SQLException;

    /**
     * 
     * @param appGuid
     * @return
     * @throws SQLException
     */
    List<Object> getArticleForApproval(final String appGuid) throws SQLException;

    /**
     * 
     * @param startRow
     * @return
     * @throws SQLException
     */
    List<String[]> getArticlesForApproval(final int startRow) throws SQLException;
}
