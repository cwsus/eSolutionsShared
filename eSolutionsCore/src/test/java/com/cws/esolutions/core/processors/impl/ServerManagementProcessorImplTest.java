/*
 * Copyright (c) 2009 - 2020 CaspersBox Web Services]
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
package com.cws.esolutions.core.processors.impl;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.processors.impl
 * File: ServerManagementProcessorImplTest.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.apache.commons.lang3.RandomStringUtils;

import com.cws.esolutions.security.dto.UserGroup;
import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.processors.dto.Server;
import com.cws.esolutions.core.enums.CoreServicesStatus;
import com.cws.esolutions.core.init.CoreServicesInitializer;
import com.cws.esolutions.core.processors.dto.Datacenter;
import com.cws.esolutions.security.enums.SecurityUserRole;
import com.cws.esolutions.security.init.SecurityServicesInitializer;
import com.cws.esolutions.core.processors.enums.ServerType;
import com.cws.esolutions.core.processors.enums.ServerStatus;
import com.cws.esolutions.core.processors.enums.ServiceRegion;
import com.cws.esolutions.core.processors.enums.NetworkPartition;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.core.processors.dto.ServerManagementRequest;
import com.cws.esolutions.core.processors.dto.ServerManagementResponse;
import com.cws.esolutions.core.processors.exception.ServerManagementException;
import com.cws.esolutions.core.processors.interfaces.IServerManagementProcessor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerManagementProcessorImplTest
{
	private static Datacenter datacenter = new Datacenter();
    private static UserAccount account = new UserAccount();
    private static RequestHostInfo reqInfo = new RequestHostInfo();

    private static final IServerManagementProcessor processor = (IServerManagementProcessor) new ServerManagementProcessorImpl();

    @BeforeAll public void setUp()
    {
    	reqInfo = new RequestHostInfo();
    	reqInfo.setHostAddress("junit");
    	reqInfo.setHostName("junit");

    	UserGroup group = new UserGroup();
    	group.setGuid("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");

        datacenter = new Datacenter();
        datacenter.setGuid("703c7e3f-d4cb-4f52-9b20-4ee08733115b");

    	account = new UserAccount();
    	account.setGuid("68c26c4a-33db-453c-a2f9-0a9190573dd8");
    	account.setUsername("khuntly");
    	account.setUserRole(SecurityUserRole.SITE_ADMIN);
    	account.setUserGroups(new ArrayList<UserGroup>(Arrays.asList(group)));

        try
        {
            SecurityServicesInitializer.initializeService("SecurityService/config/ServiceConfig.xml", "SecurityService/logging/logging.xml", false);
            CoreServicesInitializer.initializeService("eSolutionsCore/config/ServiceConfig.xml", "eSolutionsCore/logging/logging.xml", true, true);
        }
        catch (final Exception ex)
        {
            Assertions.fail(ex.getMessage());

            System.exit(-1);
        }
    }

    @Test public void addNewServer()
    {
    	for (int x = 0; x < 3; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-dmgr-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-dmgr-nas");
            
            if (x == 0)
            {
                server.setServerRegion(ServiceRegion.DEV);
            }
            else if (x == 1)
            {
                server.setServerRegion(ServiceRegion.QA);
            }
            else if (x == 2)
            {
                server.setServerRegion(ServiceRegion.PRD);
            }

            server.setDatacenter(datacenter);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.DBSERVER);
            server.setServerComments("dmgr server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setMgrUrl("https://dmgr.myserver.org:18003/console");
            server.setDmgrPort(18003);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsDevAppServer()
    {
        Server dmgrServer = new Server();
        dmgrServer.setServerGuid("bc55f443-202b-4f7c-9118-47dd80500ffb");

        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.DEV);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.APPSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setOwningDmgr(dmgrServer);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsDevWebServer()
    {
        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.DEV);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.WEBSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsQaAppServer()
    {
        Server dmgrServer = new Server();
        dmgrServer.setServerGuid("dac2e765-109e-4385-8563-aab66d6713f9");

        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.QA);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.APPSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setOwningDmgr(dmgrServer);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsQaWebServer()
    {
        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.QA);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.WEBSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsPrdAppServer()
    {
        Server dmgrServer = new Server();
        dmgrServer.setServerGuid("fde6d6e9-8bac-4a82-99c6-ef225945d846");

        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.PRD);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.APPSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setOwningDmgr(dmgrServer);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsPrdWebServer()
    {
        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.PRD);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.WEBSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsDevMasterDnsServer()
    {
        String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        Server server = new Server();
        server.setOsName("CentOS");
        server.setDomainName("caspersbox.corp");
        server.setOperIpAddress(createRandom());
        server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
        server.setMgmtIpAddress(createRandom());
        server.setMgmtHostName(name + "-mgt");
        server.setBkIpAddress(createRandom());
        server.setBkHostName(name + "-bak");
        server.setNasIpAddress(createRandom());
        server.setNasHostName(name + "-nas");
        server.setServerRegion(ServiceRegion.DEV);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.DNSMASTER);
        server.setServerComments("app server");
        server.setAssignedEngineer(account);
        server.setCpuType("AMD 1.0 GHz");
        server.setCpuCount(1);
        server.setServerModel("Virtual Server");
        server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
        server.setInstalledMemory(4096);
        server.setNetworkPartition(NetworkPartition.DRN);
        server.setDatacenter(datacenter);
        server.setServerRack(RandomStringUtils.randomNumeric(2));
        server.setRackPosition(RandomStringUtils.randomNumeric(2));

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.addNewServer(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void addServerAsQaMasterDnsServer()
    {
        String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        Server server = new Server();
        server.setOsName("CentOS");
        server.setDomainName("caspersbox.corp");
        server.setOperIpAddress(createRandom());
        server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
        server.setMgmtIpAddress(createRandom());
        server.setMgmtHostName(name + "-mgt");
        server.setBkIpAddress(createRandom());
        server.setBkHostName(name + "-bak");
        server.setNasIpAddress(createRandom());
        server.setNasHostName(name + "-nas");
        server.setServerRegion(ServiceRegion.QA);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.DNSMASTER);
        server.setServerComments("app server");
        server.setAssignedEngineer(account);
        server.setCpuType("AMD 1.0 GHz");
        server.setCpuCount(1);
        server.setServerModel("Virtual Server");
        server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
        server.setInstalledMemory(4096);
        server.setNetworkPartition(NetworkPartition.DRN);
        server.setDatacenter(datacenter);
        server.setServerRack(RandomStringUtils.randomNumeric(2));
        server.setRackPosition(RandomStringUtils.randomNumeric(2));

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.addNewServer(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void addServerAsPrdMasterDnsServer()
    {
        String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        Server server = new Server();
        server.setOsName("CentOS");
        server.setDomainName("caspersbox.corp");
        server.setOperIpAddress(createRandom());
        server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
        server.setMgmtIpAddress(createRandom());
        server.setMgmtHostName(name + "-mgt");
        server.setBkIpAddress(createRandom());
        server.setBkHostName(name + "-bak");
        server.setNasIpAddress(createRandom());
        server.setNasHostName(name + "-nas");
        server.setServerRegion(ServiceRegion.PRD);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.DNSMASTER);
        server.setServerComments("app server");
        server.setAssignedEngineer(account);
        server.setCpuType("AMD 1.0 GHz");
        server.setCpuCount(1);
        server.setServerModel("Virtual Server");
        server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
        server.setInstalledMemory(4096);
        server.setNetworkPartition(NetworkPartition.DRN);
        server.setDatacenter(datacenter);
        server.setServerRack(RandomStringUtils.randomNumeric(2));
        server.setRackPosition(RandomStringUtils.randomNumeric(2));

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.addNewServer(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void addServerAsDevSlaveDnsServer()
    {
        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.DEV);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.DNSSLAVE);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsQaSlaveDnsServer()
    {
        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.QA);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.DNSSLAVE);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addServerAsPrdSlaveDnsServer()
    {
        for (int x = 0; x < 4; x++)
        {
            String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.PRD);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.DNSSLAVE);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");

            try
            {
                ServerManagementResponse response = processor.addNewServer(request);

                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addNewServerAsDevMqServer()
    {
        String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        for (int x = 0; x < 2; x++)
        {
            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.DEV);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.MQSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));
    
            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");
    
            try
            {
                ServerManagementResponse response = processor.addNewServer(request);
    
                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addNewServerAsQaMqServer()
    {
        String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        for (int x = 0; x < 2; x++)
        {
            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(RandomStringUtils.randomAlphanumeric(8).toLowerCase());
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.QA);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.MQSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));

            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");
    
            try
            {
                ServerManagementResponse response = processor.addNewServer(request);
    
                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addNewServerAsPrdMqServer()
    {
        String name = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        for (int x = 0; x < 2; x++)
        {
            Server server = new Server();
            server.setOsName("CentOS");
            server.setDomainName("caspersbox.corp");
            server.setOperIpAddress(createRandom());
            server.setOperHostName(name);
            server.setMgmtIpAddress(createRandom());
            server.setMgmtHostName(name + "-mgt");
            server.setBkIpAddress(createRandom());
            server.setBkHostName(name + "-bak");
            server.setNasIpAddress(createRandom());
            server.setNasHostName(name + "-nas");
            server.setServerRegion(ServiceRegion.PRD);
            server.setServerStatus(ServerStatus.ONLINE);
            server.setServerType(ServerType.MQSERVER);
            server.setServerComments("app server");
            server.setAssignedEngineer(account);
            server.setCpuType("AMD 1.0 GHz");
            server.setCpuCount(1);
            server.setServerModel("Virtual Server");
            server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
            server.setInstalledMemory(4096);
            server.setNetworkPartition(NetworkPartition.DRN);
            server.setDatacenter(datacenter);
            server.setServerRack(RandomStringUtils.randomNumeric(2));
            server.setRackPosition(RandomStringUtils.randomNumeric(2));
    
            ServerManagementRequest request = new ServerManagementRequest();
            request.setRequestInfo(reqInfo);
            request.setUserAccount(account);
            request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
            request.setTargetServer(server);
            request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
            request.setApplicationName("eSolutions");
    
            try
            {
                ServerManagementResponse response = processor.addNewServer(request);
    
                Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
            }
            catch (final ServerManagementException smx)
            {
                Assertions.fail(smx.getMessage());
            }
        }
    }

    @Test public void addNewServerAsDevVmgr()
    {
        String hostname = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        Server server = new Server();
        server.setOsName("CentOS");
        server.setDomainName("caspersbox.corp");
        server.setOperIpAddress(createRandom());
        server.setOperHostName(hostname);
        server.setMgmtIpAddress(createRandom());
        server.setMgmtHostName(hostname + "-mgt");
        server.setBkIpAddress(createRandom());
        server.setBkHostName(hostname + "bak");
        server.setNasIpAddress(createRandom());
        server.setNasHostName(hostname + "- nas");
        server.setServerRegion(ServiceRegion.DEV);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.VIRTUALHOST);
        server.setServerComments("app server");
        server.setAssignedEngineer(account);
        server.setCpuType("AMD 1.0 GHz");
        server.setCpuCount(1);
        server.setServerModel("Virtual Server");
        server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
        server.setMgrUrl("https://192.168.10.250:10981/index.html");
        server.setInstalledMemory(4096);
        server.setNetworkPartition(NetworkPartition.DRN);
        server.setDatacenter(datacenter);
        server.setServerRack(RandomStringUtils.randomNumeric(2));
        server.setRackPosition(RandomStringUtils.randomNumeric(2));

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.addNewServer(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void addNewServerAsQaVmgr()
    {
        String hostname = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        Server server = new Server();
        server.setOsName("CentOS");
        server.setDomainName("caspersbox.corp");
        server.setOperIpAddress(createRandom());
        server.setOperHostName(hostname);
        server.setMgmtIpAddress(createRandom());
        server.setMgmtHostName(hostname + "-mgt");
        server.setBkIpAddress(createRandom());
        server.setBkHostName(hostname + "bak");
        server.setNasIpAddress(createRandom());
        server.setNasHostName(hostname + "- nas");
        server.setServerRegion(ServiceRegion.QA);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.VIRTUALHOST);
        server.setServerComments("app server");
        server.setAssignedEngineer(account);
        server.setCpuType("AMD 1.0 GHz");
        server.setCpuCount(1);
        server.setServerModel("Virtual Server");
        server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
        server.setMgrUrl("https://192.168.10.250:10981/index.html");
        server.setInstalledMemory(4096);
        server.setNetworkPartition(NetworkPartition.DRN);
        server.setDatacenter(datacenter);
        server.setServerRack(RandomStringUtils.randomNumeric(2));
        server.setRackPosition(RandomStringUtils.randomNumeric(2));

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.addNewServer(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void addNewServerAsPrdVmgr()
    {
        String hostname = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        Server server = new Server();
        server.setOsName("CentOS");
        server.setDomainName("caspersbox.corp");
        server.setOperIpAddress(createRandom());
        server.setOperHostName(hostname);
        server.setMgmtIpAddress(createRandom());
        server.setMgmtHostName(hostname + "-mgt");
        server.setBkIpAddress(createRandom());
        server.setBkHostName(hostname + "bak");
        server.setNasIpAddress(createRandom());
        server.setNasHostName(hostname + "- nas");
        server.setServerRegion(ServiceRegion.PRD);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.VIRTUALHOST);
        server.setServerComments("app server");
        server.setAssignedEngineer(account);
        server.setCpuType("AMD 1.0 GHz");
        server.setCpuCount(1);
        server.setServerModel("Virtual Server");
        server.setSerialNumber(RandomStringUtils.randomAlphanumeric(8));
        server.setMgrUrl("https://192.168.10.250:10981/index.html");
        server.setInstalledMemory(4096);
        server.setNetworkPartition(NetworkPartition.DRN);
        server.setDatacenter(datacenter);
        server.setServerRack(RandomStringUtils.randomNumeric(2));
        server.setRackPosition(RandomStringUtils.randomNumeric(2));

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.addNewServer(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    /*
    @Test public void updateServerData()
    {
        Server server = new Server();
        server.setAssignedEngineer(account);
        server.setBkHostName("bak.myserver.org");
        server.setBkIpAddress("1.2.3.4");
        server.setMgmtHostName("mgmt.myserver.org");
        server.setMgmtIpAddress("1.2.3.4");
        server.setNasHostName("nas.myserver.org");
        server.setNasIpAddress("1.2.3.4");
        server.setNatAddress("1.2.3.4");
        server.setOperHostName("oper.myserver.org");
        server.setOperIpAddress("4.3.4.1");
        server.setOsName("Linux");
        server.setServerComments("other comments");
        server.setServerRegion(ServiceRegion.DEV);
        server.setServerStatus(ServerStatus.ONLINE);
        server.setServerType(ServerType.APPSERVER);
        server.setServerGuid("9EADCA3A-A1A3-4986-A71C-6FE8924C9443");

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.updateServerData(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void listServersByAttribute()
    {
        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setAttribute("DRN DEV");
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.listServersByAttribute(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }

    @Test public void getServerData()
    {
        Server server = new Server();
        server.setServerGuid("85ab3069-fd3c-4490-a4ba-0c59b7cebab2");

        ServerManagementRequest request = new ServerManagementRequest();
        request.setRequestInfo(reqInfo);
        request.setUserAccount(account);
        request.setServiceId("45F6BC9E-F45C-4E2E-B5BF-04F93C8F512E");
        request.setTargetServer(server);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
            ServerManagementResponse response = processor.getServerData(request);

            Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (final ServerManagementException smx)
        {
            Assertions.fail(smx.getMessage());
        }
    }
    */

    public static String createRandom() {
    	return randomNumber() + "." + randomNumber() + "." + randomNumber() + "." + randomNumber();
    }

    public static int randomNumber() {
    	return new Random().nextInt((255 - 1) + 1) + 1;
    }

    @AfterAll public void tearDown()
    {
        SecurityServicesInitializer.shutdown();
        CoreServicesInitializer.shutdown();
    }
}
