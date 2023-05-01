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
package com.cws.esolutions.core.dao.impl;
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
import java.sql.Types;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

import com.cws.esolutions.core.dao.interfaces.IKnowledgeDataDAO;
/**
 * Interface for the Application Data DAO layer. Allows access
 * into the asset management database to obtain, modify and remove
 * application information.
 *
 * @author cws-khuntly
 * @version 1.0
 */
public class KnowledgeDataDAOImpl implements IKnowledgeDataDAO
{
	public final synchronized boolean addArticle(final List<String> value) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#addArticle(final List<String> value) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL addNewArticle(?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, value.get(0)); // articleid
            stmt.setString(2, value.get(1)); // author
            stmt.setString(3, value.get(2)); // keywords
            stmt.setString(4, value.get(3)); // title
            stmt.setString(5, value.get(4)); // symptoms
            stmt.setString(6, value.get(5)); // cause
            stmt.setString(7, value.get(6)); // resolution
            stmt.registerOutParameter(8, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            stmt.execute();
            int updateCount = stmt.getInt(8);

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
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return isComplete;
	}

	public final synchronized boolean updateArticle(final List<String> value) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#updateArticle(final List<String> value) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL updateArticle(?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, value.get(0)); // articleid
            stmt.setString(2, value.get(1)); // keywords
            stmt.setString(3, value.get(2)); // title
            stmt.setString(4, value.get(3)); // symptoms
            stmt.setString(5, value.get(4)); // cause
            stmt.setString(6, value.get(5)); // resolution
            stmt.setString(7, value.get(6)); // modified by
            stmt.registerOutParameter(8, Types.INTEGER);
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            stmt.execute();
            int updateCount = stmt.getInt(8);

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
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return isComplete;
	}

	public final synchronized boolean updateArticleStatus(final String articleId, final String modifiedBy, final String status) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#updateArticleStatus(final String articleId, final String modifiedBy, final String status) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", articleId);
            DEBUGGER.debug("Value: {}", modifiedBy);
            DEBUGGER.debug("Value: {}", status);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL updateArticleStatus(?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, articleId); // articleid
            stmt.setString(2, modifiedBy); // modifiedby
            stmt.setString(3, status); // article status
            stmt.registerOutParameter(4, Types.INTEGER);

            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
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
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return isComplete;
	}

	public final synchronized boolean removeArticle(final String articleId, final String userId) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#updateArticleStatus(final String articleId, final String userId) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", articleId);
            DEBUGGER.debug("Value: {}", userId);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        CallableStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL removeArticle(?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, articleId); // articleid
            stmt.setString(2, userId); // modifiedBy
            stmt.registerOutParameter(3, Types.INTEGER);
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
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
            throw new SQLException(sqx.getMessage(), sqx);
        }
        finally
        {
            if (!(Objects.isNull(stmt)))
            {
                stmt.close();
            }

            if ((sqlConn != null) && (!(sqlConn.isClosed())))
            {
                sqlConn.close();
            }
        }

        return isComplete;
	}

	public final synchronized List<String[]> listArticles(int startRow) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#listArticles(final int startRow) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", startRow);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        CallableStatement stmt = null;
        List<String[]> resultList = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL listArticles(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, startRow); // startrow
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if ((!(Objects.isNull(resultSet)) && (resultSet.next())))
            {
            	resultSet.beforeFirst();

            	resultList = new ArrayList<String[]>();

            	while (resultSet.next())
            	{
            		String[] articleData = new String[] { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3) };

            		if (DEBUG)
            		{
            			DEBUGGER.debug("articleData: {}", (Object) articleData);
            		}

            		resultList.add(articleData);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("resultList: {}", resultList);
            		}
            	}

        		if (DEBUG)
        		{
        			DEBUGGER.debug("resultList: {}", resultList);
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

        return resultList;
	}

	public final synchronized List<String[]> getArticlesByAttribute(final String attribute, final int startRow) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#listArticles(final String attribute, final int startRow) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", attribute);
            DEBUGGER.debug("Value: {}", startRow);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        CallableStatement stmt = null;
        List<String[]> resultList = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL getArticlesByAttribute(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, attribute);
            stmt.setInt(2, startRow); // startrow
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if ((!(Objects.isNull(resultSet)) && (resultSet.next())))
            {
            	resultSet.beforeFirst();

            	resultList = new ArrayList<String[]>();

            	while (resultSet.next())
            	{
            		String[] articleData = new String[] { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3) };

            		if (DEBUG)
            		{
            			DEBUGGER.debug("articleData: {}", (Object) articleData);
            		}

            		resultList.add(articleData);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("resultList: {}", resultList);
            		}
            	}

        		if (DEBUG)
        		{
        			DEBUGGER.debug("resultList: {}", resultList);
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

        return resultList;
	}

	public final synchronized List<Object> getArticle(final String value) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#getArticle(final String value) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        CallableStatement stmt = null;
        List<Object> resultList = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL getArticleData(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, value);
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if ((!(Objects.isNull(resultSet)) && (resultSet.next())))
            {
            	resultSet.first();

            	resultList = new ArrayList<Object>(Arrays.asList(
            			resultSet.getString(1), // ARTICLEID
            			resultSet.getTimestamp(2), // CREATEDATE
            			resultSet.getString(3), // AUTHOR
            			resultSet.getString(4), // KEYWORDS
            			resultSet.getString(5), // TITLE
            			resultSet.getString(6), // SYMPTOMS
            			resultSet.getString(7), // CAUSE
            			resultSet.getString(8), // RESOLUTION
            			resultSet.getString(9), // REVIEWEDBY
            			resultSet.getTimestamp(10), // REVIEWEDATE
            			resultSet.getString(11), // MODIFIEDBY
            			resultSet.getTimestamp(12), // MODIFIEDDATE
            			resultSet.getTimestamp(13), // APPROVEDDATE
            			resultSet.getString(14))); // APPROVEDBY

            	if (DEBUG)
            	{
            		DEBUGGER.debug("resultList: {}", resultList);
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

        return resultList;
	}

	public final synchronized List<Object> getArticleForApproval(final String value) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#getArticleForApproval(final String value) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        CallableStatement stmt = null;
        List<Object> resultList = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL getArticleForApproval(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, value);
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if ((!(Objects.isNull(resultSet)) && (resultSet.next())))
            {
            	resultSet.first();

            	resultList = new ArrayList<Object>(Arrays.asList(
            			resultSet.getString(1), // ARTICLEID
            			resultSet.getTimestamp(2), // CREATEDATE
            			resultSet.getString(3), // AUTHOR
            			resultSet.getString(4), // KEYWORDS
            			resultSet.getString(5), // TITLE
            			resultSet.getString(6), // SYMPTOMS
            			resultSet.getString(7), // CAUSE
            			resultSet.getString(8), // RESOLUTION
            			resultSet.getString(9), // REVIEWEDBY
            			resultSet.getTimestamp(10), // REVIEWEDATE
            			resultSet.getString(11), // MODIFIEDBY
            			resultSet.getTimestamp(12))); // MODIFIEDDATE

            	if (DEBUG)
            	{
            		DEBUGGER.debug("resultList: {}", resultList);
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

        return resultList;
	}

	public final synchronized List<String[]> getArticlesForApproval(final int startRow) throws SQLException
	{
        final String methodName = IKnowledgeDataDAO.CNAME + "#getArticlesForApproval(final int startRow) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", startRow);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        CallableStatement stmt = null;
        List<String[]> resultList = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if (sqlConn.isClosed())
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL getArticlesForApproval(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, startRow); // startrow
            
            if (DEBUG)
            {
                DEBUGGER.debug("CallableStatement: {}", stmt);
            }

            resultSet = stmt.executeQuery();

            if (DEBUG)
            {
            	DEBUGGER.debug("ResultSet: {}", resultSet);
            }

            if ((!(Objects.isNull(resultSet)) && (resultSet.next())))
            {
            	resultSet.beforeFirst();

            	resultList = new ArrayList<String[]>();

            	while (resultSet.next())
            	{
            		String[] articleData = new String[] { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3) };

            		if (DEBUG)
            		{
            			DEBUGGER.debug("articleData: {}", (Object) articleData);
            		}

            		resultList.add(articleData);

            		if (DEBUG)
            		{
            			DEBUGGER.debug("resultList: {}", resultList);
            		}
            	}

        		if (DEBUG)
        		{
        			DEBUGGER.debug("resultList: {}", resultList);
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

        return resultList;
	}
}
