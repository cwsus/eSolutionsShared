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
import java.util.UUID;
import java.util.ArrayList;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import com.cws.esolutions.core.dao.interfaces.IKnowledgeDataDAO;
import com.cws.esolutions.core.init.CoreServicesInitializer;
import com.cws.esolutions.security.init.SecurityServiceInitializer;
/**
 * @see com.cws.esolutions.core.dao.interfaces.IApplicationDataDAO
 * @author cws-khuntly
 * @version 1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KnowledgeDataDAOImplTest
{
	private IKnowledgeDataDAO dao = (IKnowledgeDataDAO) new KnowledgeDataDAOImpl();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUp() throws Exception
	{
        try
        {
            SecurityServiceInitializer.initializeService("SecurityService/config/ServiceConfig.xml", "SecurityService/logging/logging.xml", true);
            CoreServicesInitializer.initializeService("eSolutionsCore/config/ServiceConfig.xml", "eSolutionsCore/logging/logging.xml", true, true);
        }
        catch (final Exception ex)
        {
            Assertions.fail(ex.getMessage());

            System.exit(-1);
        }
	}

	/**
	 * Test method for {@link com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl#addArticle(java.util.List)}.
	 */
	@Test
	final void addArticle()
	{
		List<String> articleData = new ArrayList<String>();
		articleData.add(UUID.randomUUID().toString());
		articleData.add("e1006d6d-e815-4b27-9a8c-fb91227cc2b5");
		articleData.add("test");
		articleData.add("This is a test article");
		articleData.add("Test article");
		articleData.add("testing");
		articleData.add("this is a test");
		articleData.add("This is a test of the article management system. This is only a test.");

		try
		{
			boolean isComplete = dao.addArticle(articleData);

			Assertions.assertThat(isComplete).isTrue();
		}
		catch (final SQLException sqx)
		{
			sqx.printStackTrace();
			Assertions.fail(sqx.getMessage());
		}
	}

	/**
	 * Test method for {@link com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl#updateArticle(java.util.List)}.
	 */
	@Test
	final void testUpdateArticle()
	{
		List<String> articleData = new ArrayList<String>();
		articleData.add("9627a4e1-f1e0-4db4-8c91-a938a30d6d87"); // article id
		articleData.add("test"); // keywords
		articleData.add("This is a test article"); // title
		articleData.add("Test article"); // symptoms
		articleData.add("testing"); // cause
		articleData.add("THIS. IS. AWESOME.");// resolution
		articleData.add("e1006d6d-e815-4b27-9a8c-fb91227cc2b5"); // modified by

		try
		{
			boolean isComplete = dao.updateArticle(articleData);

			Assertions.assertThat(isComplete).isTrue();
		}
		catch (final SQLException sqx)
		{
			sqx.printStackTrace();
			Assertions.fail(sqx.getMessage());
		}
	}

	/**
	 * Test method for {@link com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl#removeArticle(java.lang.String)}.
	 */
	@Test
	final void testRemoveArticle()
	{
		try
		{
			boolean isComplete = dao.removeArticle("9627a4e1-f1e0-4db4-8c91-a938a30d6d87", "e1006d6d-e815-4b27-9a8c-fb91227cc2b5");

			Assertions.assertThat(isComplete).isTrue();
		}
		catch (final SQLException sqx)
		{
			sqx.printStackTrace();
			Assertions.fail(sqx.getMessage());
		}
	}

	/**
	 * Test method for {@link com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl#listArticles(int)}.
	 */
	@Test
	final void testListArticles()
	{
		try
		{
			List<String[]> articleData = dao.listArticles(0);

			Assertions.assertThat(articleData).isNotEmpty();
		}
		catch (final SQLException sqx)
		{
			sqx.printStackTrace();
			Assertions.fail(sqx.getMessage());
		}
	}

	/**
	 * Test method for {@link com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl#getArticlesByAttribute(java.lang.String, int)}.
	 */
	@Test
	final void testGetArticlesByAttribute()
	{
		try
		{
			List<String[]> articleData = dao.getArticlesByAttribute("test", 0);

			Assertions.assertThat(articleData).isNotEmpty();
		}
		catch (final SQLException sqx)
		{
			sqx.printStackTrace();
			Assertions.fail(sqx.getMessage());
		}
	}

	/**
	 * Test method for {@link com.cws.esolutions.core.dao.impl.KnowledgeDataDAOImpl#getArticle(java.lang.String)}.
	 */
	@Test
	final void testGetArticle()
	{
		try
		{
			List<Object> articleData = dao.getArticle("8f97a451-a709-4615-b077-b45e6701cb7d");

			Assertions.assertThat(articleData).isNotEmpty();
		}
		catch (final SQLException sqx)
		{
			sqx.printStackTrace();
			Assertions.fail(sqx.getMessage());
		}
	}

}
