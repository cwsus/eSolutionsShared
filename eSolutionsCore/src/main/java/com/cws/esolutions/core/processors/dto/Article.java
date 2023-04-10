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
package com.cws.esolutions.core.processors.dto;
/*
 * Project: eSolutionsCore
 * Package: com.cws.esolutions.core.processors.enums
 * File: AppServerType.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
import java.util.Date;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cws.esolutions.core.CoreServicesConstants;
import com.cws.esolutions.core.processors.enums.ArticleStatus;
/**
 * @author cws-khuntly
 * @version 1.0
 */
public class Article implements Serializable
{
	private String title = null;
	private String cause = null;
	private String author = null;
	private Date reviewDate = null;
	private Date modifyDate = null;
	private Date createDate = null;
	private String keywords = null;
	private String symptoms = null;
	private String articleId = null;
	private String resolution = null;
	private String reviewedBy = null;
	private String modifiedBy = null;
	private ArticleStatus status = null;

	private static final String CNAME = Article.class.getName();
    private static final long serialVersionUID = -4196311827125939234L;

    private static final Logger DEBUGGER = LogManager.getLogger(CoreServicesConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();

    public final void setTitle(final String value)
    {
        final String methodName = Article.CNAME + "#setTitle(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.title = value;
    }

    public final void setCause(final String value)
    {
        final String methodName = Article.CNAME + "#setCause(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.cause = value;
    }

    public final void setAuthor(final String value)
    {
        final String methodName = Article.CNAME + "#setAuthor(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.author = value;
    }

    public final void setReviewDate(final Date value)
    {
        final String methodName = Article.CNAME + "#setReviewDate(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.reviewDate = value;
    }

    public final void setModifyDate(final Date value)
    {
        final String methodName = Article.CNAME + "#setModifyDate(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.modifyDate = value;
    }

    public final void setCreateDate(final Date value)
    {
        final String methodName = Article.CNAME + "#setTitle(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.createDate = value;
    }

    public final void setKeywords(final String value)
    {
        final String methodName = Article.CNAME + "#setKeywords(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.keywords = value;
    }

    public final void setSymptoms(final String value)
    {
        final String methodName = Article.CNAME + "#setSymptoms(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.symptoms = value;
    }

    public final void setArticleId(final String value)
    {
        final String methodName = Article.CNAME + "#setArticleId(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.articleId = value;
    }

    public final void setResolution(final String value)
    {
        final String methodName = Article.CNAME + "#setResolution(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.resolution = value;
    }

    public final void setReviewedBy(final String value)
    {
        final String methodName = Article.CNAME + "#setReviewedBy(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.reviewedBy = value;
    }

    public final void setModifiedBy(final String value)
    {
        final String methodName = Article.CNAME + "#setModifiedBy(final String value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.modifiedBy = value;
    }

    public final void setStatus(final ArticleStatus value)
    {
        final String methodName = Article.CNAME + "#setStatus(final ArticleStatus value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", value);
        }

        this.status = value;
    }

    public final String getTitle()
    {
        final String methodName = Article.CNAME + "#getTitle()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.title);
        }

        return this.title;
    }

    public final String getCause()
    {
        final String methodName = Article.CNAME + "#getCause()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.cause);
        }

        return this.cause;
    }

    public final String getAuthor()
    {
        final String methodName = Article.CNAME + "#getAuthor()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.author);
        }

        return this.author;
    }

    public final Date getReviewDate(final Date value)
    {
        final String methodName = Article.CNAME + "#getReviewDate(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.reviewDate);
        }

        return this.reviewDate;
    }

    public final Date getModifyDate(final Date value)
    {
        final String methodName = Article.CNAME + "#getModifyDate(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.modifyDate);
        }

        return this.modifyDate;
    }

    public final Date getCreateDate(final Date value)
    {
        final String methodName = Article.CNAME + "#getTitle(final Date value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.createDate);
        }

        return this.createDate;
    }

    public final String getKeywords()
    {
        final String methodName = Article.CNAME + "#getKeywords()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.keywords);
        }

        return this.keywords;
    }

    public final String getSymptoms()
    {
        final String methodName = Article.CNAME + "#getSymptoms()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.symptoms);
        }

        return this.symptoms;
    }

    public final String getArticleId()
    {
        final String methodName = Article.CNAME + "#getArticleId()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.articleId);
        }

        return this.articleId;
    }

    public final String getResolution()
    {
        final String methodName = Article.CNAME + "#getResolution()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.resolution);
        }

        return this.resolution;
    }

    public final String getReviewedBy()
    {
        final String methodName = Article.CNAME + "#getReviewedBy()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.reviewedBy);
        }

        return this.reviewedBy;
    }

    public final String getModifiedBy()
    {
        final String methodName = Article.CNAME + "#getModifiedBy()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.modifiedBy);
        }

        return this.modifiedBy;
    }

    public final ArticleStatus getStatus(final ArticleStatus value)
    {
        final String methodName = Article.CNAME + "#getStatus(final ArticleStatus value)";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("Value: {}", this.status);
        }

        return this.status;
    }

    @Override
    public final String toString()
    {
        StringBuilder sBuilder = new StringBuilder()
            .append("[" + this.getClass().getName() + "]" + CoreServicesConstants.LINE_BREAK + "{" + CoreServicesConstants.LINE_BREAK);

        for (Field field : this.getClass().getDeclaredFields())
        {
            if (!(field.getName().equals("methodName")) &&
                    (!(field.getName().equals("CNAME"))) &&
                    (!(field.getName().equals("DEBUGGER"))) &&
                    (!(field.getName().equals("DEBUG"))) &&
                    (!(field.getName().equals("ERROR_RECORDER"))) &&
                    (!(field.getName().equals("serialVersionUID"))))
            {
                try
                {
                    if (field.get(this) != null)
                    {
                        sBuilder.append("\t" + field.getName() + " --> " + field.get(this) + CoreServicesConstants.LINE_BREAK);
                    }
                }
                catch (final IllegalAccessException iax) {}
            }
        }

        sBuilder.append('}');

        return sBuilder.toString();
    }
}
