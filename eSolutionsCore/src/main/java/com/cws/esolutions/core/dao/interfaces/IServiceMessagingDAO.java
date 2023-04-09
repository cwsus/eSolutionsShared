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
 * Package: com.cws.esolutions.core.dao.processors.interfaces
 * File: IMessagingDAO.java
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
 * @author cws-khuntly
 * @version 1.0
 */
public interface IServiceMessagingDAO
{
    static final String CNAME = IServiceMessagingDAO.class.getName();

    static final CoreServicesBean appBean = CoreServicesBean.getInstance();
    static final DataSource dataSource = appBean.getDataSources().get("ApplicationDataSource");

    static final Logger ERROR_RECORDER = LogManager.getLogger(CoreServicesConstants.ERROR_LOGGER + CNAME);
    static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    boolean insertMessage(final List<Object> messageList) throws SQLException;

    List<Object> retrieveMessage(final String messageId) throws SQLException;

    List<Object[]> retrieveMessages() throws SQLException;

    List<Object[]> retrieveAlertMessages() throws SQLException;

    List<Object[]> getMessagesByAttribute(final String value) throws SQLException;

    boolean updateMessage(final String messageId, final List<Object> messageList) throws SQLException;

    boolean deleteMessage(final String messageId) throws SQLException;
}
