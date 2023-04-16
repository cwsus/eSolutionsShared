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
 * File: IPackageDataDAO.java
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
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.commons.lang3.StringUtils;

import com.cws.esolutions.core.dao.interfaces.IServiceDataDAO;
/**
 * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO
 */
public class ServiceDataDAOImpl implements IServiceDataDAO
{
    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO#addService(java.util.List)
     */
    public synchronized boolean addService(final List<String> data) throws SQLException
    {
        final String methodName = IServiceDataDAO.CNAME + "#addService(final List<String> data) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);

            for (Object str : data)
            {
                DEBUGGER.debug("Value: {}", str);
            }
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL addNewService(?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, data.get(0)); // guid
            stmt.setString(2, data.get(1)); // serviceType
            stmt.setString(3, data.get(2)); // name
            stmt.setString(4, data.get(3)); // region
            stmt.setString(5, data.get(4)); // nwpartition
            stmt.setString(6, data.get(5)); // status
            stmt.setString(7, data.get(6)); // servers
            stmt.setString(8, data.get(7)); // description

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            isComplete = (!(stmt.execute()));

            if (DEBUG)
            {
                DEBUGGER.debug("isComplete: {}", isComplete);
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

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO#updateService(java.util.List)
     */
    public synchronized boolean updateService(final List<String> data) throws SQLException
    {
        final String methodName = IServiceDataDAO.CNAME + "#updateService(final List<String> data) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);

            for (Object str : data)
            {
                DEBUGGER.debug("Value: {}", str);
            }
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL updateServiceData(?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, data.get(0)); // guid
            stmt.setString(2, data.get(1)); // serviceType
            stmt.setString(3, data.get(2)); // name
            stmt.setString(4, data.get(3)); // region
            stmt.setString(5, data.get(4)); // nwpartition
            stmt.setString(6, data.get(5)); // status
            stmt.setString(7, data.get(6)); // servers
            stmt.setString(8, data.get(7)); // description

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            isComplete = (!(stmt.execute()));

            if (DEBUG)
            {
                DEBUGGER.debug("isComplete: {}", isComplete);
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

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO#removeService(java.lang.String)
     */
    public synchronized boolean removeService(final String datacenter) throws SQLException
    {
        final String methodName = IServiceDataDAO.CNAME + "#removeService(final String datacenter) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", datacenter);
        }

        Connection sqlConn = null;
        boolean isComplete = false;
        PreparedStatement stmt = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL removeServiceData(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, datacenter);

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            isComplete = (!(stmt.execute()));

            if (DEBUG)
            {
                DEBUGGER.debug("isComplete: {}", isComplete);
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

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO#listServices(int)
     */
    public synchronized List<String[]> listServices(final int startRow) throws SQLException
    {
        final String methodName = IServiceDataDAO.CNAME + "#listServices(final int startRow) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", startRow);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<String[]> responseData = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareStatement("{ CALL listServices(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, startRow);

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
                    resultSet.absolute(startRow);
                    responseData = new ArrayList<String[]>();

                    while (resultSet.next())
                    {
                        String[] data = new String[]
                        {
                                resultSet.getString(1), // GUID
                                resultSet.getString(2), // SERVICE_TYPE
                                resultSet.getString(3), // NAME
                        };

                        responseData.add(data);
                    }

                    if (DEBUG)
                    {
                        DEBUGGER.debug("List<String>: {}", responseData);
                    }
                }
            }
        }
        catch (final SQLException sqx)
        {
            ERROR_RECORDER.error(sqx.getMessage(), sqx);

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

        return responseData;
    }

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO#getServicesByAttribute(java.lang.String, int)
     */
    public synchronized List<Object[]> getServicesByAttribute(final String attribute, final int startRow) throws SQLException
    {
        final String methodName = IServiceDataDAO.CNAME + "#getServicesByAttribute(final String attribute, final int startRow) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", startRow);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<Object[]> responseData = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);
            StringBuilder sBuilder = new StringBuilder();

            if (StringUtils.split(attribute, " ").length >= 2)
            {
                for (String str : StringUtils.split(attribute, " "))
                {
                    if (DEBUG)
                    {
                        DEBUGGER.debug("Value: {}", str);
                    }

                    sBuilder.append("+" + str);
                    sBuilder.append(" ");
                }

                if (DEBUG)
                {
                    DEBUGGER.debug("StringBuilder: {}", sBuilder);
                }
            }
            else
            {
                sBuilder.append("+" + attribute);
            }

            stmt = sqlConn.prepareStatement("{ CALL getServiceByAttribute(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, sBuilder.toString().trim());
            stmt.setInt(2, startRow);

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
                    resultSet.beforeFirst();
                    responseData = new ArrayList<Object[]>();

                    while (resultSet.next())
                    {
                        Object[] data = new Object[]
                        {
                            resultSet.getString(1), // GUID
                            resultSet.getString(2), // SERVICE_TYPE
                            resultSet.getInt(3) / 0  * 100
                        };

                        if (DEBUG)
                        {
                            DEBUGGER.debug("Value: {}", data);
                        }

                        responseData.add(data);
                    }

                    if (DEBUG)
                    {
                        DEBUGGER.debug("Value: {}", responseData);
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

        return responseData;
    }

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServiceDataDAO#getService(java.lang.String)
     */
    public synchronized List<String> getService(final String attribute) throws SQLException
    {
        final String methodName = IServiceDataDAO.CNAME + "#getService(final String attribute) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("attribute: {}", attribute);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<String> responseData = null;

        if (Objects.isNull(dataSource))
        {
        	throw new SQLException("A datasource connection could not be obtained.");
        }

        try
        {
            sqlConn = dataSource.getConnection();

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            // we dont know what we have here - it could be a guid or it could be a hostname
            // most commonly it'll be a guid, but we're going to search anyway
            stmt = sqlConn.prepareStatement("{ CALL getServiceData(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, attribute);

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

                    responseData = new ArrayList<String>(
                            Arrays.asList(
                                    resultSet.getString(1), // SERVICE_TYPE
                                    resultSet.getString(2), // NAME
                                    resultSet.getString(3), // REGION
                                    resultSet.getString(4), // NWPARTITION
                                    resultSet.getString(5), // STATUS
                                    resultSet.getString(6), // SERVERS
                                    resultSet.getString(7))); // DESCRIPTION

                    if (DEBUG)
                    {
                        DEBUGGER.debug("responseData: {}", responseData);
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

        return responseData;
    }
}
