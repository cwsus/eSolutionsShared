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
package com.cws.esolutions.core.init;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.init
 * File: CoreServiceInitializer.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import javax.sql.DataSource;
import java.sql.SQLException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.JAXBException;
import java.net.MalformedURLException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.dbcp2.BasicDataSource;

import com.cws.esolutions.core.CoreServicesBean;
import com.cws.esolutions.security.SecurityServicesBean;
import com.cws.esolutions.core.config.xml.DataSourceManager;
import com.cws.esolutions.utility.securityutils.PasswordUtils;
import com.cws.esolutions.core.exception.CoreServicesException;
import com.cws.esolutions.core.config.xml.CoreConfigurationData;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public class CoreServicesInitializer
{
    private static final CoreServicesBean appBean = CoreServicesBean.getInstance();
    private static final SecurityServicesBean secBean = SecurityServicesBean.getInstance();

    /**
     * Initializes the core service in a standalone mode - used for applications outside of a container or when
     * run as a standalone jar.
     *
     * @param configFile - The service configuration file to utilize
     * @param logConfig - The logging configuration file to utilize
     * @param loadSecurity - Flag to start security
     * @param startConnections - Flag to start connections
     * @throws CoreServicesException @{link com.cws.esolutions.core.exception.CoreServicesException}
     * if an exception occurs during initialization
     */
    public static void initializeService(final String configFile, final String logConfig, final boolean loadSecurity, final boolean startConnections) throws CoreServicesException
    {
        URL xmlURL = null;
        JAXBContext context = null;
        Unmarshaller marshaller = null;
        CoreConfigurationData configData = null;

        final String serviceConfig = (StringUtils.isBlank(configFile)) ? System.getProperty("coreConfigFile") : configFile;

        try
        {
            xmlURL = CoreServicesInitializer.class.getClassLoader().getResource(serviceConfig);

            if (Objects.isNull(xmlURL))
            {
                // try loading from the filesystem
                xmlURL = FileUtils.getFile(configFile).toURI().toURL();
            }

            context = JAXBContext.newInstance(CoreConfigurationData.class);
            marshaller = context.createUnmarshaller();
            configData = (CoreConfigurationData) marshaller.unmarshal(xmlURL);

            CoreServicesInitializer.appBean.setConfigData(configData);

            if (startConnections)
            {
                Map<String, DataSource> dsMap = CoreServicesInitializer.appBean.getDataSources();

                if (dsMap == null)
                {
                    dsMap = new HashMap<String, DataSource>();
                }

                for (DataSourceManager mgr : configData.getResourceConfig().getDsManager())
                {
                    if (!(dsMap.containsKey(mgr.getDsName())))
                    {
                        StringBuilder sBuilder = new StringBuilder()
                            .append("connectTimeout=" + mgr.getConnectTimeout() + ";")
                            .append("socketTimeout=" + mgr.getConnectTimeout() + ";")
                            .append("autoReconnect=" + mgr.getAutoReconnect() + ";");

                        BasicDataSource dataSource = new BasicDataSource();
                            dataSource.setDriverClassName(mgr.getDriver());
                            dataSource.setUrl(mgr.getDataSource());
                            dataSource.setUsername(mgr.getDsUser());
                            dataSource.setConnectionProperties(sBuilder.toString());
                            dataSource.setPassword(PasswordUtils.decryptText(mgr.getDsPass(), mgr.getDsSalt(),
                            		secBean.getConfigData().getSecurityConfig().getSecretKeyAlgorithm(),
                            		secBean.getConfigData().getSecurityConfig().getIterations(),
                            		secBean.getConfigData().getSecurityConfig().getKeyLength(),
                            		secBean.getConfigData().getSecurityConfig().getEncryptionAlgorithm(),
                            		secBean.getConfigData().getSecurityConfig().getEncryptionInstance(),
                            		appBean.getConfigData().getSystemConfig().getEncoding()));

                        dsMap.put(mgr.getDsName(), dataSource);
                    }
                }

                CoreServicesInitializer.appBean.setDataSources(dsMap);
            }
        }
        catch (final JAXBException jx)
        {
            jx.printStackTrace();
            throw new CoreServicesException(jx.getMessage(), jx);
        }
        catch (final MalformedURLException mux)
        {
            mux.printStackTrace();
            throw new CoreServicesException(mux.getMessage(), mux);
        }
    }

    /**
     * Shuts down the running core service process.
     */
    public static void shutdown()
    {
        final Map<String, DataSource> datasources = CoreServicesInitializer.appBean.getDataSources();

        try
        {
            if ((datasources != null) && (datasources.size() != 0))
            {
                for (String key : datasources.keySet())
                {
                    BasicDataSource dataSource = (BasicDataSource) datasources.get(key);

                    if ((dataSource != null ) && (!(dataSource.isClosed())))
                    {
                        dataSource.close();
                    }
                }
            }
        }
        catch (final SQLException sqx)
        {
        	System.err.println("CoreServicesInitializer#shutdown(): Exception occurred while shutting down: " + sqx.getMessage());
        }
    }
}
