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
package com.cws.esolutions.security.dao.userauth.impl;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.dao.userauth.impl
 * File: SQLAuthenticator.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.sql.Types;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.security.dao.userauth.interfaces.Authenticator;
import com.cws.esolutions.security.dao.userauth.exception.AuthenticatorException;
/**
 * @see com.cws.esolutions.security.dao.userauth.interfaces.Authenticator
 */
public class SQLAuthenticator implements Authenticator
{
    private static final String CNAME = SQLAuthenticator.class.getName();

    /**
     * 
     */
    public synchronized boolean performLogon(final String userGuid, final String userName, final String password) throws AuthenticatorException
    {
        final String methodName = SQLAuthenticator.CNAME + "#performLogon(final String userGuid, final String userName, final String password) throws AuthenticatorException";
        
        if(DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new AuthenticatorException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL getUserPassword(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userGuid);
            stmt.setString(2, userName);

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("resultSet: {}", resultSet);
            }

            if (Objects.isNull(resultSet))
            {
            	throw new AuthenticatorException("Failed to load the user password from the authentication datastore.");
            }

            if (resultSet.next())
            {
            	resultSet.first();

            	String retrievedPassword = resultSet.getString(1);

            	if (StringUtils.equals(retrievedPassword, password))
            	{
            		isComplete = true;
            	}
            }
        }
        catch (final SQLException sqx)
        {
            throw new AuthenticatorException(sqx.getMessage(), sqx);
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
                throw new AuthenticatorException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * 
     */
    public synchronized boolean verifySecurityData(final String userGuid, final String userName, final HashMap<String, String> questionMap) throws AuthenticatorException
    {
        final String methodName = SQLAuthenticator.CNAME + "#verifySecurityData(final String userGuid, final String userName, final HashMap<String, String> questionMap) throws AuthenticatorException";
        
        if(DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new AuthenticatorException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL getSecurityData(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userGuid);
            stmt.setString(2, userName);

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("resultSet: {}", resultSet);
            }

            if (Objects.isNull(resultSet))
            {
            	throw new AuthenticatorException("Failed to load security information from the authentication datastore.");
            }

            if (resultSet.next())
            {
            	resultSet.first();

            	String foundQuestionOne = resultSet.getString(1);
            	String foundQuestionTwo = resultSet.getString(2);
            	String foundSecAnswerOne = resultSet.getString(3);
            	String foundSecAnswerTwo = resultSet.getString(4);
            	String givenAnswerOne = questionMap.get(foundQuestionOne);
            	String givenAnswerTwo = questionMap.get(foundQuestionTwo);

            	if ((StringUtils.isNotBlank(givenAnswerOne)) && (StringUtils.isNotBlank(givenAnswerTwo)))
            	{
            		if ((StringUtils.equals(foundSecAnswerOne, givenAnswerOne)) && (StringUtils.equals(foundSecAnswerTwo, givenAnswerTwo)))
            		{
            			isComplete = true;
            		}
            	}
            }
        }
        catch (final SQLException sqx)
        {
            throw new AuthenticatorException(sqx.getMessage(), sqx);
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
                throw new AuthenticatorException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * 
     */
    public synchronized void performLogoff(final String userGuid, final String userName, final String tokenSalt, final String authToken) throws AuthenticatorException
    {
        final String methodName = SQLAuthenticator.CNAME + "#performLogoff(final String userGuid, final String userName, final String tokenSalt, final String authToken) throws AuthenticatorException";
        
        if(DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new AuthenticatorException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            sqlConn.setAutoCommit(true);
            stmt = sqlConn.prepareStatement("{ CALL removeSessionData(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userGuid);
            stmt.setString(2, tokenSalt);

            stmt.execute();
        }
        catch (final SQLException sqx)
        {
            throw new AuthenticatorException(sqx.getMessage(), sqx);
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
                throw new AuthenticatorException(sqx.getMessage(), sqx);
            }
        }
    }

    /**
     * 
     */
    public synchronized boolean validateAuthToken(final String userGuid, final String userId, final String authToken) throws AuthenticatorException
    {
        final String methodName = SQLAuthenticator.CNAME + "#validateAuthToken(final String userGuid, final String userId, final String authToken) throws AuthenticatorException";
        
        if(DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new AuthenticatorException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL getAuthToken(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userGuid); // guid
            stmt.setString(2, userId);

            if (!(stmt.execute()))
            {
                throw new AuthenticatorException("No user was found for the provided user information");
            }

            resultSet = stmt.getResultSet();

            if (DEBUG)
            {
                DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if (resultSet.next())
            {
            	resultSet.first();

            	String returnedToken = resultSet.getString(1);

            	if (DEBUG)
            	{
            		DEBUGGER.debug("returnedToken: {}", returnedToken);
            	}

            	if (StringUtils.equals(returnedToken, authToken))
            	{
            		isComplete = true;
            	}
            }
        }
        catch (final SQLException sqx)
        {
            throw new AuthenticatorException(sqx.getMessage(), sqx);
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
                throw new AuthenticatorException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.Authenticator#performSuccessfulLogin(String, String, int, Long)
     */
    public boolean performSuccessfulLogin(final String userId, final String guid, final String authToken) throws AuthenticatorException
    {
        final String methodName = SQLAuthenticator.CNAME + "#modifyUserSecurity(final String userId, final String guid, final String authToken) throws AuthenticatorException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new AuthenticatorException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            // first make sure the existing password is proper
            // then make sure the new password doesnt match the existing password
            stmt = sqlConn.prepareCall("{ CALL performSuccessfulLogin(?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userId);
            stmt.setString(2, guid);
            stmt.setString(3, authToken);
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.execute();
            int updateCount = stmt.getInt(4);

            if (updateCount == 1)
            {
            	isComplete = true;
            }
        }
        catch (final SQLException sqx)
        {
            throw new AuthenticatorException(sqx.getMessage(), sqx);
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
                throw new AuthenticatorException(sqx.getMessage(), sqx);
            }
        }

        return isComplete;
    }

    /**
     * @see com.cws.esolutions.security.dao.usermgmt.interfaces.Authenticator#getOlrStatus(java.lang.String, java.lang.String)
     */
    public List<Boolean> getOlrStatus(final String guid, final String userId) throws AuthenticatorException
    {
        final String methodName = SQLAuthenticator.CNAME + "#getOlrStatus(final String guid, final String userId) throws AuthenticatorException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", guid);
            DEBUGGER.debug("Value: {}", userId);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<Boolean> response = null;

        if (Objects.isNull(dataSource))
        {
        	throw new AuthenticatorException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (DEBUG)
            {
            	DEBUGGER.debug("sqlConn: {}", sqlConn);
            }

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            // first make sure the existing password is proper
            // then make sure the new password doesnt match the existing password
            stmt = sqlConn.prepareStatement("{ CALL getOlrStatus(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, guid);
            stmt.setString(2, userId);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            if (!(stmt.execute()))
            {
                throw new AuthenticatorException("No user was found for the provided user information");
            }

            resultSet = stmt.getResultSet();

            if (DEBUG)
            {
                DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if (resultSet.next())
            {
            	resultSet.first();

            	response = new ArrayList<Boolean>(
            			Arrays.asList(
            					resultSet.getBoolean(1), // ISOLRSETUP
            					resultSet.getBoolean(2))); // ISOLRLOCKED

            	if (DEBUG)
            	{
            		DEBUGGER.debug("Response list: {}", response);
            	}
            }
        }
        catch (final SQLException sqx)
        {
            throw new AuthenticatorException(sqx.getMessage(), sqx);
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
                throw new AuthenticatorException(sqx.getMessage(), sqx);
            }
        }

        return response;
    }
}
