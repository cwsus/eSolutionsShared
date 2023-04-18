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
package com.cws.esolutions.core.processors.impl;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.dao.impl
 * File: java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;

import com.cws.esolutions.security.dto.UserGroup;
import com.cws.esolutions.security.dto.UserAccount;
import com.cws.esolutions.core.processors.dto.Article;
import com.cws.esolutions.core.enums.CoreServicesStatus;
import com.cws.esolutions.core.init.CoreServicesInitializer;
import com.cws.esolutions.security.enums.SecurityUserRole;
import com.cws.esolutions.security.init.SecurityServicesInitializer;
import com.cws.esolutions.core.processors.enums.ArticleStatus;
import com.cws.esolutions.security.processors.dto.RequestHostInfo;
import com.cws.esolutions.core.processors.dto.KnowledgeManagementRequest;
import com.cws.esolutions.core.processors.dto.KnowledgeManagementResponse;
import com.cws.esolutions.core.processors.exception.KnowledgeManagementException;
import com.cws.esolutions.core.processors.interfaces.IKnowledgeManagementProcessor;
/**
 * @see com.cws.esolutions.core.dao.interfaces.IApplicationDataDAO
 * @author cws-khuntly
 * @version 1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KnowledgeManagementProcessorImplTest
{
	private static final IKnowledgeManagementProcessor processor = (IKnowledgeManagementProcessor) new KnowledgeManagementProcessorImpl();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
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

	@Test
	final void testAddArticleData()
	{
		RequestHostInfo reqInfo = new RequestHostInfo();
		reqInfo.setHostAddress("junit");
		reqInfo.setHostAddress("junit");

		UserGroup userGroup = new UserGroup();
		userGroup.setGuid("4B081972-92C3-455B-9403-B81E68C538B6");

		List<UserGroup> userGroups = new ArrayList<UserGroup>();
		userGroups.add(userGroup);
		
		UserAccount userAccount = new UserAccount();
		userAccount.setGuid("e1006d6d-e815-4b27-9a8c-fb91227cc2b5");
		userAccount.setUsername("khuntly");
		userAccount.setUserRole(SecurityUserRole.USER);
		userAccount.setUserGroups(userGroups);

		Article article = new Article();
		article.setAuthor(userAccount);
		article.setCause("There is another a test");
		article.setKeywords("test");
		article.setResolution("Some kind of testing was tested and testy mctesterson");
		article.setSymptoms("Testy");
		article.setTitle("Test Article");

		KnowledgeManagementRequest request = new KnowledgeManagementRequest();
		request.setArticle(article);
        request.setServiceId("4B081972-92C3-455B-9403-B81E68C538B6");
        request.setRequestInfo(reqInfo);
        request.setUserAccount(userAccount);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
        	KnowledgeManagementResponse response = processor.addArticleData(request);

        	Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (KnowledgeManagementException kmx)
        {
        	Assertions.fail(kmx.getMessage());
        }
	}

	@Test
	public final void testListPendingArticles()
	{
		RequestHostInfo reqInfo = new RequestHostInfo();
		reqInfo.setHostAddress("junit");
		reqInfo.setHostAddress("junit");

		UserAccount userAccount = new UserAccount();
		userAccount.setGuid("e1006d6d-e815-4b27-9a8c-fb91227cc2b5");
		userAccount.setUsername("khuntly");
		userAccount.setUserRole(SecurityUserRole.SITE_ADMIN);

		Article article = new Article();
		article.setStatus(ArticleStatus.PENDING);

		KnowledgeManagementRequest request = new KnowledgeManagementRequest();
		request.setArticle(article);
        request.setServiceId("4B081972-92C3-455B-9403-B81E68C538B6");
        request.setRequestInfo(reqInfo);
        request.setUserAccount(userAccount);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
        	KnowledgeManagementResponse response = processor.listPendingArticles(request);

        	Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (KnowledgeManagementException kmx)
        {
        	Assertions.fail(kmx.getMessage());
        }
	}

	@Test
	final void testUpdateArticleData() {
		Assertions.fail("Not yet implemented"); // TODO
	}

	@Test
	final void testDeleteArticleData() {
		Assertions.fail("Not yet implemented"); // TODO
	}

	@Test
	final void testListArticles()
	{
		RequestHostInfo reqInfo = new RequestHostInfo();
		reqInfo.setHostAddress("junit");
		reqInfo.setHostAddress("junit");

		UserAccount userAccount = new UserAccount();
		userAccount.setGuid("e1006d6d-e815-4b27-9a8c-fb91227cc2b5");
		userAccount.setUsername("khuntly");
		userAccount.setUserRole(SecurityUserRole.SITE_ADMIN);

		KnowledgeManagementRequest request = new KnowledgeManagementRequest();
        request.setServiceId("4B081972-92C3-455B-9403-B81E68C538B6");
        request.setRequestInfo(reqInfo);
        request.setUserAccount(userAccount);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
        	KnowledgeManagementResponse response = processor.listArticles(request);

        	Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (KnowledgeManagementException kmx)
        {
        	kmx.printStackTrace();
        	Assertions.fail(kmx.getMessage());
        }
	}

	@Test
	final void testListArticlesByAttribute()
	{
		RequestHostInfo reqInfo = new RequestHostInfo();
		reqInfo.setHostAddress("junit");
		reqInfo.setHostAddress("junit");

		UserAccount userAccount = new UserAccount();
		userAccount.setGuid("e1006d6d-e815-4b27-9a8c-fb91227cc2b5");
		userAccount.setUsername("khuntly");
		userAccount.setUserRole(SecurityUserRole.SITE_ADMIN);

		Article article = new Article();
		article.setSearchTerms("test");

		KnowledgeManagementRequest request = new KnowledgeManagementRequest();
		request.setArticle(article);
        request.setServiceId("4B081972-92C3-455B-9403-B81E68C538B6");
        request.setRequestInfo(reqInfo);
        request.setUserAccount(userAccount);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
        	KnowledgeManagementResponse response = processor.listArticlesByAttribute(request);

        	Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (KnowledgeManagementException kmx)
        {
        	kmx.printStackTrace();
        	Assertions.fail(kmx.getMessage());
        }
	}

	@Test
	final void testGetArticleData()
	{
		RequestHostInfo reqInfo = new RequestHostInfo();
		reqInfo.setHostAddress("junit");
		reqInfo.setHostAddress("junit");

		UserAccount userAccount = new UserAccount();
		userAccount.setGuid("e1006d6d-e815-4b27-9a8c-fb91227cc2b5");
		userAccount.setUsername("khuntly");
		userAccount.setUserRole(SecurityUserRole.SITE_ADMIN);

		Article article = new Article();
		article.setArticleId("8f97a451-a709-4615-b077-b45e6701cb7d");

		KnowledgeManagementRequest request = new KnowledgeManagementRequest();
		request.setArticle(article);
        request.setServiceId("4B081972-92C3-455B-9403-B81E68C538B6");
        request.setRequestInfo(reqInfo);
        request.setUserAccount(userAccount);
        request.setApplicationId("6236B840-88B0-4230-BCBC-8EC33EE837D9");
        request.setApplicationName("eSolutions");

        try
        {
        	KnowledgeManagementResponse response = processor.getArticleData(request);

        	Assertions.assertThat(response.getRequestStatus()).isEqualTo(CoreServicesStatus.SUCCESS);
        }
        catch (KnowledgeManagementException kmx)
        {
        	kmx.printStackTrace();
        	Assertions.fail(kmx.getMessage());
        }
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}
}
