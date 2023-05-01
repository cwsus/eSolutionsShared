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
package com.cws.esolutions.utility.securityutils.dao.accesscontrol.dao.impl;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.dao.reference.impl
 * File: AccessControlServiceDAOImpl.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.naming.NamingException;

import com.cws.esolutions.utility.securityutils.dao.accesscontrol.dao.interfaces.IAccessControlServiceDAO;
/**
 * @see com.cws.esolutions.security.dao.reference.interfaces.ISecurityReferenceDAO
 */
public class AccessControlServiceDAOImpl implements IAccessControlServiceDAO
{
    private static final String CNAME = AccessControlServiceDAOImpl.class.getName();

    /**
     * @throws NamingException 
     * @see com.cws.esolutions.IAccessControlServiceDAO.dao.reference.interfaces.IUserSecurityInformationDAO#getUserSalt(java.lang.String, java.lang.String)
     */
    public synchronized List<String> getAccessGroups() throws SQLException
    {
        final String methodName = AccessControlServiceDAOImpl.CNAME + "#getAccessGroups() throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<String> responseList = null;

        try
        {
            DataSource dataSource = (DataSource) bean.getAuthDataSource();

            if (DEBUG)
            {
            	DEBUGGER.debug("DataSource: {}", dataSource);
            }

            if (Objects.isNull(dataSource))
            {
            	throw new SQLException("A datasource connection could not be obtained.");
            }

            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
                DEBUGGER.debug("Connection: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);
            stmt = sqlConn.prepareStatement("{ CALL getAvailableGroups(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.getResultSet();

                if (resultSet.next())
                {
                    resultSet.beforeFirst();

                    while (resultSet.next())
                    {
                    	responseList = new ArrayList<String>(
                    			Arrays.asList(
                    					resultSet.getString(1),
                    					resultSet.getString(2)));
                    }
                }
            }
        }
        catch (final SQLException sqx)
        {
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
        	if (!(Objects.isNull(resultSet)))
            {
                resultSet.close();
            }

            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return responseList;
    }

    /**
     * @see com.cws.esolutions.IAccessControlServiceDAO.dao.reference.interfaces.IUserSecurityInformationDAO#obtainSecurityQuestionList()
     */
    public synchronized String getUserGroups(final String commonName) throws SQLException
    {
        final String methodName = AccessControlServiceDAOImpl.CNAME + "#getUserGroups(final String commonName, final String userId) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        String responseList = null;
        PreparedStatement stmt = null;

        try
        {
        	DataSource dataSource = (DataSource) bean.getAuthDataSource();

            if (Objects.isNull(dataSource))
            {
            	throw new SQLException("A datasource connection could not be obtained.");
            }

            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);
            stmt = sqlConn.prepareStatement("{ CALL getUserGroups(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, commonName);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.getResultSet();

                if (DEBUG)
                {
                	DEBUGGER.debug("resultSet: {}", resultSet);
                }

                if (resultSet.next())
                {
                	resultSet.first();
                	responseList = resultSet.getString(1);

                	if (DEBUG)
                	{
                		DEBUGGER.debug("responseList: {}", responseList);
                	}
                }
            }
            else
            {
            	System.out.println("stmt.execute was false");
            }
        }
        catch (final SQLException sqx)
        {
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
            if (!(Objects.isNull(resultSet)))
            {
                resultSet.close();
            }

            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return responseList;
    }

    /**
     * @see com.cws.esolutions.IAccessControlServiceDAO.dao.reference.interfaces.IUserSecurityInformationDAO#getUserSalt(java.lang.String, java.lang.String)
     */
    public synchronized boolean isGroupEnabled(final String commonName) throws SQLException
    {
        final String methodName = AccessControlServiceDAOImpl.CNAME + "#isGroupEnabled(final String commonName) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        boolean isEnabled = false;
        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        try
        {
        	DataSource dataSource = (DataSource) bean.getAuthDataSource();

            if (Objects.isNull(dataSource))
            {
            	throw new SQLException("A datasource connection could not be obtained.");
            }

            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
                DEBUGGER.debug("Connection: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);
            stmt = sqlConn.prepareStatement("{ CALL getAvailableGroups() }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.getResultSet();

                if (resultSet.next())
                {
                    isEnabled = true;
                }
            }
        }
        catch (final SQLException sqx)
        {
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
            if (!(Objects.isNull(resultSet)))
            {
                resultSet.close();
            }

            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return isEnabled;
    }
}
