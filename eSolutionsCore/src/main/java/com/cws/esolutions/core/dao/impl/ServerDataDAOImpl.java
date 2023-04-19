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
import java.sql.Types;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;

import com.cws.esolutions.core.dao.interfaces.IServerDataDAO;
/**
 * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO
 */
public class ServerDataDAOImpl implements IServerDataDAO
{
    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO#addServer(java.util.List)
     */
    public synchronized boolean addServer(final List<Object> serverData) throws SQLException
    {
        final String methodName = IServerDataDAO.CNAME + "#addServer(final List<Object> serverData) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("platformData: {}", serverData);
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

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }

            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL addNewServer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, (String) serverData.get(0)); // IN systemGuid VARCHAR(128),
            stmt.setString(2, (String) serverData.get(1)); // IN systemOs VARCHAR(45),
            stmt.setString(3, (String) serverData.get(2)); // IN systemStatus VARCHAR(45),
            stmt.setString(4, (String) serverData.get(3)); // IN systemRegion VARCHAR(45),
            stmt.setString(5, (String) serverData.get(5)); // IN networkPartition VARCHAR(45),
            stmt.setString(6, (String) serverData.get(6)); // IN datacenter VARCHAR(128),
            stmt.setString(7, (String) serverData.get(7)); // IN systemType VARCHAR(45),
            stmt.setString(8, (String) serverData.get(8)); // IN domainName VARCHAR(255),
            stmt.setString(9, (String) serverData.get(9)); // IN cpuType VARCHAR(255),
            stmt.setInt(10, (Integer) serverData.get(10)); // IN cpuCount INT,
            stmt.setString(11, (String) serverData.get(11)); // IN serverModel VARCHAR(255),
            stmt.setString(12, (String) serverData.get(12)); // IN serialNumber VARCHAR(255),
            stmt.setInt(13, (Integer) serverData.get(13)); // IN installedMemory INT,
            stmt.setString(14, (String) serverData.get(14)); // IN operIp VARCHAR(50),
            stmt.setString(15, (String) serverData.get(15)); // IN operHostname VARCHAR(100),
            stmt.setString(16, (String) serverData.get(16)); // IN mgmtIp VARCHAR(50),
            stmt.setString(17, (String) serverData.get(17)); // IN mgmtHostname VARCHAR(100),
            stmt.setString(18, (String) serverData.get(18)); // IN backupIp VARCHAR(50),
            stmt.setString(19, (String) serverData.get(19)); // IN backupHostname VARCHAR(100),
            stmt.setString(20, (String) serverData.get(20)); // IN nasIp VARCHAR(50),
            stmt.setString(21, (String) serverData.get(21)); // IN nasHostname VARCHAR(100),
            stmt.setString(22, (String) serverData.get(22)); // IN natAddr VARCHAR(50),
            stmt.setString(23, (String) serverData.get(23)); // IN systemComments TEXT,
            stmt.setString(24, (String) serverData.get(24)); // IN engineer VARCHAR(100),
            stmt.setString(25, (String) serverData.get(25)); // IN serverRack VARCHAR(255),
            stmt.setString(26, (String) serverData.get(26)); // IN rackPosition VARCHAR(255),
            stmt.registerOutParameter(27, Types.INTEGER); // OUT updateCount INTEGER

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.execute();
            int updateCount = stmt.getInt(30);

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

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO#updateServer(java.lang.String, java.util.List)
     */
    public synchronized boolean updateServer(final String serverGuid, final List<Object> serverData) throws SQLException
    {
        final String methodName = IServerDataDAO.CNAME + "#updateServer(final String serverGuid, final List<Object> serverData) throws SQLException";
        
        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", serverGuid);

            for (Object str : serverData)
            {
                DEBUGGER.debug("Value: {}", str);
            }
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

            if ((Objects.isNull(sqlConn)) || (sqlConn.isClosed()))
            {
                throw new SQLException("Unable to obtain application datasource connection");
            }
 
            sqlConn.setAutoCommit(true);

            stmt = sqlConn.prepareCall("{ CALL updateaServerData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, (String) serverData.get(0)); // IN systemGuid VARCHAR(128),
            stmt.setString(2, (String) serverData.get(1)); // IN systemOs VARCHAR(45),
            stmt.setString(3, (String) serverData.get(2)); // IN systemStatus VARCHAR(45),
            stmt.setString(4, (String) serverData.get(3)); // IN systemRegion VARCHAR(45),
            stmt.setString(5, (String) serverData.get(5)); // IN networkPartition VARCHAR(45),
            stmt.setString(6, (String) serverData.get(6)); // IN datacenter VARCHAR(128),
            stmt.setString(7, (String) serverData.get(7)); // IN systemType VARCHAR(45),
            stmt.setString(8, (String) serverData.get(8)); // IN domainName VARCHAR(255),
            stmt.setString(9, (String) serverData.get(9)); // IN cpuType VARCHAR(255),
            stmt.setInt(10, (Integer) serverData.get(10)); // IN cpuCount INT,
            stmt.setString(11, (String) serverData.get(11)); // IN serverModel VARCHAR(255),
            stmt.setString(12, (String) serverData.get(12)); // IN serialNumber VARCHAR(255),
            stmt.setInt(13, (Integer) serverData.get(13)); // IN installedMemory INT,
            stmt.setString(14, (String) serverData.get(14)); // IN operIp VARCHAR(50),
            stmt.setString(15, (String) serverData.get(15)); // IN operHostname VARCHAR(100),
            stmt.setString(16, (String) serverData.get(16)); // IN mgmtIp VARCHAR(50),
            stmt.setString(17, (String) serverData.get(17)); // IN mgmtHostname VARCHAR(100),
            stmt.setString(18, (String) serverData.get(18)); // IN backupIp VARCHAR(50),
            stmt.setString(19, (String) serverData.get(19)); // IN backupHostname VARCHAR(100),
            stmt.setString(20, (String) serverData.get(20)); // IN nasIp VARCHAR(50),
            stmt.setString(21, (String) serverData.get(21)); // IN nasHostname VARCHAR(100),
            stmt.setString(22, (String) serverData.get(22)); // IN natAddr VARCHAR(50),
            stmt.setString(23, (String) serverData.get(23)); // IN systemComments TEXT,
            stmt.setString(24, (String) serverData.get(24)); // IN engineer VARCHAR(100),
            stmt.setString(25, (String) serverData.get(25)); // IN serverRack VARCHAR(255),
            stmt.setString(26, (String) serverData.get(26)); // IN rackPosition VARCHAR(255),
            stmt.registerOutParameter(27, Types.INTEGER); // OUT updateCount INTEGER

            if (DEBUG)
            {
                DEBUGGER.debug("PreparedStatement: {}", stmt);
            }

            stmt.execute();
            int upateCount = stmt.getInt(30);

            if (DEBUG)
            {
                DEBUGGER.debug("upateCount: {}", upateCount);
            }

            if (upateCount == 1)
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

    /**
     * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO#removeServer(java.lang.String)
     */
    public synchronized boolean removeServer(final String serverGuid) throws SQLException
    {
        final String methodName = IServerDataDAO.CNAME + "#removeServer(final String serverGuid) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("serverGuid: {}", serverGuid);
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

            stmt = sqlConn.prepareStatement("{ CALL removeServerFromAssets(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, serverGuid);

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
     * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO#listServers(int)
     */
    public synchronized List<String[]> listServers(final int startRow) throws SQLException
    {
        final String methodName = IServerDataDAO.CNAME + "#listServers(final int startRow) throws SQLException";

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

            stmt = sqlConn.prepareStatement("{ CALL retrServerList(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
                    resultSet.beforeFirst();
                    responseData = new ArrayList<String[]>();

                    while (resultSet.next())
                    {
                        String[] serverData = new String[]
                        {
                                resultSet.getString(1), // SYSTEM_GUID
                                resultSet.getString(2) // NAME
                        };

                        if (DEBUG)
                        {
                            for (Object obj : serverData)
                            {
                                DEBUGGER.debug("Value: {}", obj);
                            }
                        }

                        responseData.add(serverData);
                    }

                    if (DEBUG)
                    {
                        for (Object[] objArr : responseData)
                        {
                            for (Object obj : objArr)
                            {
                                DEBUGGER.debug("Value: {}", obj);
                            }
                        }
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
     * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO#getServersByAttribute(java.lang.String, int)
     */
    public synchronized List<String[]> getServersByAttribute(final String value, final int startRow) throws SQLException
    {
        final String methodName = IServerDataDAO.CNAME + "#getServersByAttribute(final String value, final int startRow) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
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

            stmt = sqlConn.prepareStatement("{ CALL getServerByAttribute(?, ?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, value);
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
                    responseData = new ArrayList<String[]>();

                    while (resultSet.next())
                    {
                        String[] data = new String[]
                        {
                            resultSet.getString(1), // GUID
                            resultSet.getString(2)
                        };

                        if (DEBUG)
                        {
                            DEBUGGER.debug("Value: {}", (Object) data);
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
     * @see com.cws.esolutions.core.dao.interfaces.IServerDataDAO#getServer(java.lang.String)
     */
    public synchronized List<Object> getServer(final String serverGuid) throws SQLException
    {
        final String methodName = IServerDataDAO.CNAME + "#getServer(final String serverGuid) throws SQLException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("attribute: {}", serverGuid);
        }

        Connection sqlConn = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        List<Object> responseData = null;

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
            stmt = sqlConn.prepareStatement("{ CALL getServerData(?) }", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, serverGuid);

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

                    responseData = new ArrayList<Object>(
                            Arrays.asList(
                                    resultSet.getString(1), // T1.GUID,
                                    resultSet.getString(2), // T1.SYSTEM_OSTYPE
                                    resultSet.getString(3), // T1.SYSSTATUS
                                    resultSet.getString(4), // T1.REGION
                                    resultSet.getString(5), // T1.NETWORK_PARTITION
                                    resultSet.getString(6), // T1.SYSTEM_TYPE
                                    resultSet.getString(7), // T1.DOMAIN_NAME
                                    resultSet.getString(8), // T1.CPU_TYPE
                                    resultSet.getInt(9), // T1.CPU_COUNT
                                    resultSet.getString(10), // T1.SERVER_RACK
                                    resultSet.getString(11), // T1.RACK_POSITION
                                    resultSet.getString(12), // T1.SERVER_MODEL
                                    resultSet.getString(13), // T1.SERIAL_NUMBER
                                    resultSet.getInt(14), // T1.INSTALLED_MEMORY
                                    resultSet.getString(15), // T1.OPER_IP
                                    resultSet.getString(16), // T1.OPER_HOSTNAME
                                    resultSet.getString(17), // T1.MGMT_IP
                                    resultSet.getString(18), // T1.MGMT_HOSTNAME
                                    resultSet.getString(19), // T1.BKUP_IP
                                    resultSet.getString(20), // T1.BKUP_HOSTNAME
                                    resultSet.getString(21), // T1.NAS_IP
                                    resultSet.getString(22), // T1.NAS_HOSTNAME
                                    resultSet.getString(23), // T1.NAT_ADDR
                                    resultSet.getString(24), // T1.COMMENTS
                                    resultSet.getString(25), // T1.ASSIGNED_ENGINEER
                                    resultSet.getTimestamp(26), // T1.ADD_DATE
                                    resultSet.getTimestamp(27), // T1.DELETE_DATE
                                    resultSet.getString(28), // T2.GUID
                                    resultSet.getString(29))); // T2.NAME

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
