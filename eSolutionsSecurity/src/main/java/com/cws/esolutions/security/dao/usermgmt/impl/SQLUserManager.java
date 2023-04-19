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
package com.cws.esolutions.security.dao.usermgmt.impl;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.dao.usermgmt.impl
 * File: SQLUserManager.java
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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager;
import com.cws.esolutions.security.dao.usermgmt.exception.UserManagementException;
/**
 * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager
 */
public class SQLUserManager implements UserManager
{
    private static final String CNAME = SQLUserManager.class.getName();

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#validateUserAccount(String, String)
     */
    public synchronized boolean validateUserAccount(final String userId, final String userGuid) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#validateUserAccount(final String userId, final String userGuid) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Value: {}", userGuid);
        }

        boolean isValid = false;
        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL getUserByAttribute(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userGuid);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.executeQuery();

                if (DEBUG)
                {
                    DEBUGGER.debug("ResultSet: {}", resultSet);
                }

                if (resultSet.next())
                {
                    resultSet.beforeFirst();

                    while (resultSet.next())
                    {
                        if ((StringUtils.equals(resultSet.getString(1), userGuid)) || (StringUtils.equals(resultSet.getString(2), userId)))
                        {
                        	throw new UserManagementException("A user currently exists with the provided information.");
                        }
                    }

                    isValid = true;
                }
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(resultSet)))
                {
                    resultSet.close();
                }

                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isValid;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#addUserAccount(java.util.List)
     */
    public synchronized boolean addUserAccount(final List<String> userAccount) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#addUserAccount(final List<String> userAccount) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userAccount);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        PreparedStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL addUserAccount(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userAccount.get(0)); // commonName NVARCHAR(128),
            stmt.setString(2, userAccount.get(1)); // uid NVARCHAR(45),
            stmt.setString(3, userAccount.get(2)); // userpassword NVARCHAR(255)
            stmt.setString(4, userAccount.get(3)); // cwsrole NVARCHAR(45),
            stmt.setString(5, userAccount.get(4)); // sn NVARCHAR(100),
            stmt.setString(6, userAccount.get(5)); // givenname NVARCHAR(100),
            stmt.setString(7, userAccount.get(6)); // salt value
            stmt.setString(8, userAccount.get(7)); // salt type
            stmt.setString(9, userAccount.get(8)); // email addr
            stmt.setString(10, userAccount.get(9)); // telephonenumber NVARCHAR(10),
            stmt.setString(11, userAccount.get(10)); // pager NVARCHAR(10),

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (!(stmt.execute()))
            {
                isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
        	switch (sqx.getErrorCode())
        	{
        		case 1061:
        			throw new UserManagementException("An account already exists with that username.");
    			default:
    				throw new UserManagementException(sqx.getMessage(), sqx);
        	}
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#removeUserAccount(java.lang.String)
     */
    public synchronized boolean removeUserAccount(final String userId) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#removeUserAccount(final String userId) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("userId: {}", userId);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        PreparedStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL removeUserAccount(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (!(stmt.execute()))
            {
                isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#searchUsers(java.lang.String)
     */
    public synchronized List<String[]> findUsers(final String searchData) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#findUsers(final String searchData) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", searchData);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<String[]> results = null;

        try
        {
        	if (StringUtils.contains(searchData, "@"))
        	{
                if (Objects.isNull(contactDataSource))
                {
                    throw new UserManagementException("A datasource connection could not be obtained.");
                }

                sqlConn = contactDataSource.getConnection();

                if (DEBUG)
                {
                    DEBUGGER.debug("sqlConn: {}", sqlConn);
                }

                if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
                {
                    throw new SQLException("Unable to obtain application datasource connection");
                }

                sqlConn.setAutoCommit(true);

                stmt = sqlConn.prepareStatement("{ CALL getUserByEmail(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, searchData);
        	}
        	else
        	{
                if (Objects.isNull(authDataSource))
                {
                    throw new UserManagementException("A datasource connection could not be obtained.");
                }

                sqlConn = authDataSource.getConnection();

                if (DEBUG)
                {
                    DEBUGGER.debug("sqlConn: {}", sqlConn);
                }

                if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
                {
                    throw new SQLException("Unable to obtain application datasource connection");
                }

                sqlConn.setAutoCommit(true);

                stmt = sqlConn.prepareStatement("{ CALL getUserByAttribute(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, searchData);
        	}

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
                    results = new ArrayList<String[]>();

                    while (resultSet.next())
                    {
                        String[] userData = new String[]
                        {
                            resultSet.getString(1), resultSet.getString(2)
                        };

                        if (DEBUG)
                        {
                            DEBUGGER.debug("Data: {}", (Object) userData);
                        }

                        results.add(userData);
                    }

                    if (DEBUG)
                    {
                        DEBUGGER.debug("List: {}", results);
                    }
                }
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(resultSet)))
                {
                    resultSet.close();
                }
            
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return results;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#loadUserAccount(java.lang.String)
     */
    public synchronized List<Object> loadUserAccount(final String userGuid) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#loadUserAccount(final String guid) throws UserManagementException";
        
        if(DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userGuid);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<Object> userAccount = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL showUserAccount(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userGuid); // common name

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.getResultSet();

                if (DEBUG)
                {
                    DEBUGGER.debug("ResultSet: {}", resultSet);
                }

                if (resultSet.next())
                {
                    resultSet.last();
                    int x = resultSet.getRow();

                    if (DEBUG)
                    {
                        DEBUGGER.debug("x: {}", x);
                    }

                    if (x > 1)
                    {
                    	throw new UserManagementException("Multiple records were found for the given information.");
                    }

                    resultSet.first();
                    ResultSetMetaData resultMetaData = resultSet.getMetaData(); 
                    int columnCount = resultMetaData.getColumnCount();
                    userAccount = new ArrayList<Object>();

                    if (DEBUG)
                    {
                    	DEBUGGER.debug("ResultSetMetaData: {}", resultMetaData);
                    	DEBUGGER.debug("columnCount: {}", columnCount);
                    }

                    for (int y = 1; y != columnCount + 1; y++)
                    {
                    	if (DEBUG)
                    	{
                    		DEBUGGER.debug("Data: {}", resultSet.getObject(y));
                    	}

                    	userAccount.add(resultSet.getObject(y));
                    }

                    if (DEBUG)
                    {
                        DEBUGGER.debug("userAccount: {}", userAccount);
                    }
                }
            }
            else
            {
                throw new UserManagementException("No users were located with the provided information");
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(resultSet)))
                {
                    resultSet.close();
                }
            
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return userAccount;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#listUserAccounts()
     */
    public synchronized String loadUserGroups(final String guid) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#loadUserGroups(final String guid) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", guid);
        }

        String results = null;
        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL getUserGroups(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, guid);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.getResultSet();

                if (resultSet.next())
                {
                    resultSet.first();
                    results = resultSet.getString(1);
                }
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(resultSet)))
                {
                    resultSet.close();
                }
            
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return results;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#getUserByEmailAddress(java.lang.String)
     */
    public synchronized List<String> getUserByUsername(final String searchData) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#getUserByUsername(final String searchData) throws UserManagementException";
        
        if(DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", searchData);
        }

        Connection sqlConn = null;
        String userAccount = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<String> responseList = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL getUserByAttribute(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, searchData); // common name

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.execute())
            {
                resultSet = stmt.getResultSet();

                if (DEBUG)
                {
                    DEBUGGER.debug("ResultSet: {}", resultSet);
                }

                if (resultSet.next())
                {
                    resultSet.last();
                    int x = resultSet.getRow();

                    if (DEBUG)
                    {
                        DEBUGGER.debug("x: {}", x);
                    }

                    if ((x == 0) || (x > 1))
                    {
                        throw new UserManagementException("No user account was located for the provided data.");
                    }

                    resultSet.first();

                    responseList = new ArrayList<String>(
                    		Arrays.asList(
                    				resultSet.getString(1),
                    				resultSet.getString(2)));

                    if (DEBUG)
                    {
                        DEBUGGER.debug("userAccount: {}", userAccount);
                    }
                }
            }
            else
            {
                throw new UserManagementException("No users were located with the provided information");
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(resultSet)))
                {
                    resultSet.close();
                }
            
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return responseList;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#listUserAccounts()
     */
    public synchronized List<Object[]> listUserAccounts() throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#listUserAccounts() throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<Object[]> results = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL listUserAccounts() }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

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
                    results = new ArrayList<Object[]>();

                    while (resultSet.next())
                    {
                        Object[] userData = new Object[] { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getTimestamp(4) };

                        if (DEBUG)
                        {
                            for (Object obj : userData)
                            {
                                DEBUGGER.debug(obj);
                            }
                        }

                        results.add(userData);
                    }

                    if (DEBUG)
                    {
                        DEBUGGER.debug("List: {}", results);
                    }
                }
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(resultSet)))
                {
                    resultSet.close();
                }
            
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return results;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#modifyUserEmail(java.lang.String, java.lang.String)
     */
    public synchronized boolean modifyUserEmail(final String userId, final String value) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#modifyUserEmail(final String userId, final String value) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Value: {}", value);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        ResultSet resultSet = null;
        CallableStatement stmt = null;

        if (Objects.isNull(contactDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = contactDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            // first make sure the existing password is proper
            // then make sure the new password doesnt match the existing password
            stmt = sqlConn.prepareCall("{ CALL updateUserEmail(?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setString(2, value);
            stmt.registerOutParameter(3, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.execute();
            int updateCount = stmt.getInt(3);

            if (DEBUG)
            {
            	DEBUGGER.debug("updateCount: {}", updateCount);
            }

            if (updateCount == 1)
            {
            	isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
            	if (!(Objects.isNull(resultSet)))
            	{
            		resultSet.close();
            	}
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#modifyUserContact(java.lang.String, java.util.List)
     */
    public synchronized boolean modifyUserContact(final String userId, final List<String> values) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#modifyUserContact(final String userId, final List<String> values) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Values: {}", values);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(contactDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = contactDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL updateUserContact(?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setString(2, values.get(0));
            stmt.setString(3, values.get(1));
            stmt.registerOutParameter(4, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.executeUpdate();
            int updateCount = stmt.getInt(4);

            if (DEBUG)
            {
            	DEBUGGER.debug("updateCount: {}", updateCount);
            }

            if (updateCount == 1)
            {
            	isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }
    
    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#modifyUserSuspension(java.lang.String, boolean)
     */
    public synchronized boolean modifyUserSuspension(final String userId, final boolean isSuspended) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#modifyUserSuspension(final String userId, final boolean isSuspended) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Value: {}", isSuspended);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL modifyUserSuspension(?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setBoolean(2, isSuspended);
            stmt.registerOutParameter(3, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.execute();
            int resultCount = stmt.getInt(3);

            if (DEBUG)
            {
            	DEBUGGER.debug("resultCount: {}", resultCount);
            }

            isComplete = true;
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#modifyUserRole(java.lang.String, java.lang.Object[])
     */
    public synchronized boolean modifyUserRole(final String userId, final String role) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#modifyUserRole(final String userId, final String role) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Value: {}", role);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        PreparedStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            // first make sure the existing password is proper
            // then make sure the new password doesnt match the existing password
            stmt = sqlConn.prepareStatement("{ CALL updateUserRole(?, ?,}", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setString(2, role);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (stmt.executeUpdate() == 1)
            {
                isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#modifyOlrLock(java.lang.String, boolean)
     */
    public synchronized boolean modifyOlrLock(final String userId, final boolean isLocked) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#modifyOlrLock(final String userId, final boolean value) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Value: {}", isLocked);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            // first make sure the existing password is proper
            // then make sure the new password doesnt match the existing password
            stmt = sqlConn.prepareCall("{ CALL modifyOlrLock(?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setBoolean(2, isLocked);
            stmt.registerOutParameter(3, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.execute();
            int resultCount = stmt.getInt(3);

            if (DEBUG)
            {
                DEBUGGER.debug("resultCount {}", resultCount);
            }

            if (resultCount == 1)
            {
            	isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.UserManager#modifyUserLock(java.lang.String, boolean, int)
     */
    public synchronized boolean modifyUserLock(final String userId, final boolean isLocked, final int increment) throws UserManagementException
    {
        final String methodName = SQLUserManager.CNAME + "#modifyUserLock(final String userId, final boolean int, final boolean increment) throws UserManagementException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", userId);
            DEBUGGER.debug("Value: {}", isLocked);
            DEBUGGER.debug("Value: {}", increment);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(authDataSource))
        {
        	throw new UserManagementException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = authDataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL modifyUserLock(?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setBoolean(2, isLocked);
            stmt.setInt(3, increment);
            stmt.registerOutParameter(4, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.execute();
            int updateCount = stmt.getInt(4);

            if (DEBUG)
            {
            	DEBUGGER.debug("updateCount: {}", updateCount);
            }

            if (updateCount == 1)
            {
            	isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new UserManagementException(sqx.getMessage(), sqx);
        }
        finally
        {
            try
            {
                if (!(Objects.isNull(stmt)))
                {
                    stmt.close();
                }

                if (!(Objects.isNull(sqlConn)) && (!(sqlConn.isClosed())))
                {
                    sqlConn.close();
                }
            }
            catch (final SQLException sqx)
            {
                throw new UserManagementException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }
}
