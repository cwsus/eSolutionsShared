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
package com.cws.esolutions.utility.securityutils.dao.accesscontrol.dao.interfaces;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.dao.reference.interfaces
 * File: IAccessControlServiceDAO.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.utility.UtilityBean;
import com.cws.esolutions.utility.UtilityConstants;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public interface IAccessControlServiceDAO
{
	static final UtilityBean bean = UtilityBean.getInstance();

	static final Logger DEBUGGER = LogManager.getLogger(UtilityConstants.DEBUGGER);
    static final boolean DEBUG = DEBUGGER.isDebugEnabled();
    static final Logger AUDIT_RECORDER = LogManager.getLogger(UtilityConstants.AUDIT_LOGGER);

    /**
     * 
     * @return
     * @throws SQLException
     */
    List<String> getAccessGroups() throws SQLException;

    /**
     * 
     * @param commonName
     * @param userId
     * @return
     * @throws SQLException
     */
    String getUserGroups(final String commonName) throws SQLException;

    /**
     * 
     * @param commonName
     * @return
     * @throws SQLException
     */
    boolean isGroupEnabled(final String commonName) throws SQLException;
}
