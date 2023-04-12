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
package com.cws.esolutions.security.filters;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.filters
 * File: SSLEnforcementFilter.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly           11/23/2008 22:39:20             Created.
 */
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.Filter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.Logger;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebFilter;
import java.util.MissingResourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cws.esolutions.security.SecurityServiceBean;
import com.cws.esolutions.security.config.xml.SystemConfig;
import com.cws.esolutions.security.SecurityServiceConstants;
/**
 * @see javax.servlet.Filter
 */
@WebFilter(filterName = "SSLEnforcementFilter", urlPatterns = {"/*"}, initParams = @WebInitParam(name = "filter-config", value = "SecurityService/filters/SSLEnforcementFilter"))
public class SSLEnforcementFilter implements Filter
{
    private String[] ignoreURIs = null;
    private String[] ignoreHosts = null;

    private static final String SECURE_URL_PREFIX = "https://";
    private static final String IGNORE_URI_LIST = "ignore.uri.list";
    private static final String IGNORE_HOST_LIST = "ignore.host.list";
    private static final String FILTER_CONFIG_PARAM_NAME = "filter-config";
    private static final String CNAME = SSLEnforcementFilter.class.getName();
    private static final String FILTER_CONFIG_FILE_NAME = "config/FilterConfig";
    private static final Set<String> LOCALHOST = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("localhost", "127.0.0.1")));
    private static final SystemConfig systemConfig = SecurityServiceBean.getInstance().getConfigData().getSystemConfig();

    private static final Logger DEBUGGER = LogManager.getLogger(SecurityServiceConstants.DEBUGGER);
    private static final boolean DEBUG = DEBUGGER.isDebugEnabled();
    private static final Logger ERROR_RECORDER = LogManager.getLogger(SecurityServiceConstants.ERROR_LOGGER + CNAME);

    public void init(final FilterConfig filterConfig) throws ServletException
    {
        final String methodName = SSLEnforcementFilter.CNAME + "#init(FilterConfig filterConfig) throws ServletException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("FilterConfig: {}", filterConfig);
        }

        ResourceBundle rBundle = null;

        try
        {
            if (filterConfig.getInitParameter(SSLEnforcementFilter.FILTER_CONFIG_PARAM_NAME) == null)
            {
                ERROR_RECORDER.error("Filter configuration not found. Using default !");

                rBundle = ResourceBundle.getBundle(SSLEnforcementFilter.FILTER_CONFIG_FILE_NAME);
            }
            else
            {
                rBundle = ResourceBundle.getBundle(filterConfig.getInitParameter(SSLEnforcementFilter.FILTER_CONFIG_PARAM_NAME));
            }

            this.ignoreHosts = (
                    StringUtils.isNotEmpty(rBundle.getString(SSLEnforcementFilter.IGNORE_HOST_LIST)))
                    ? rBundle.getString(SSLEnforcementFilter.IGNORE_HOST_LIST).trim().split(",") : null;
            this.ignoreURIs = (
                    StringUtils.isNotEmpty(rBundle.getString(SSLEnforcementFilter.IGNORE_URI_LIST)))
                    ? rBundle.getString(SSLEnforcementFilter.IGNORE_URI_LIST).trim().split(",") : null;

            if (DEBUG)
            {
                if (this.ignoreHosts != null)
                {
                    for (String str : this.ignoreHosts)
                    {
                        DEBUGGER.debug(str);
                    }
                }

                if (this.ignoreURIs != null)
                {
                    for (String str : this.ignoreURIs)
                    {
                        DEBUGGER.debug(str);
                    }
                }
            }
        }
        catch (final MissingResourceException mre)
        {
            ERROR_RECORDER.error(mre.getMessage(), mre);

            throw new UnavailableException(mre.getMessage());
        }
    }

    public void doFilter(final ServletRequest sRequest, final ServletResponse sResponse, final FilterChain filterChain) throws ServletException, IOException
    {
        final String methodName = SSLEnforcementFilter.CNAME + "#doFilter(final ServletRequest req, final servletResponse res, final FilterChain filterChain) throws ServletException, IOException";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
            DEBUGGER.debug("ServletRequest: {}", sRequest);
            DEBUGGER.debug("ServletResponse: {}", sResponse);
            DEBUGGER.debug("FilterChain: {}", filterChain);
        }

        final HttpServletRequest hRequest = (HttpServletRequest) sRequest;
        final HttpServletResponse hResponse = (HttpServletResponse) sResponse;

        if (DEBUG)
        {
            final HttpSession hSession = hRequest.getSession();

            DEBUGGER.debug("HttpServletRequest: {}", hRequest);
            DEBUGGER.debug("HttpServletResponse: {}", hResponse);
            DEBUGGER.debug("HttpSession: {}", hSession);

            DEBUGGER.debug("Dumping session content:");
            Enumeration<?> sessionEnumeration = hSession.getAttributeNames();

            while (sessionEnumeration.hasMoreElements())
            {
                String element = (String) sessionEnumeration.nextElement();
                Object value = hSession.getAttribute(element);

                DEBUGGER.debug("Attribute: {}; Value: {}", element, value);
            }

            DEBUGGER.debug("Dumping request content:");
            Enumeration<?> requestEnumeration = hRequest.getAttributeNames();

            while (requestEnumeration.hasMoreElements())
            {
                String element = (String) requestEnumeration.nextElement();
                Object value = hRequest.getAttribute(element);

                DEBUGGER.debug("Attribute: {}; Value: {}", element, value);
            }

            DEBUGGER.debug("Dumping request parameters:");
            Enumeration<?> paramsEnumeration = hRequest.getParameterNames();

            while (paramsEnumeration.hasMoreElements())
            {
                String element = (String) paramsEnumeration.nextElement();
                Object value = hRequest.getParameter(element);

                DEBUGGER.debug("Parameter: {}; Value: {}", element, value);
            }
        }

        if (SSLEnforcementFilter.LOCALHOST.contains(sRequest.getServerName()))
        {
            if (DEBUG)
            {
                DEBUGGER.debug("Local request. Breaking out...");
            }

            filterChain.doFilter(sRequest, sResponse);

            return;
        }

        if ((this.ignoreHosts != null) && (this.ignoreHosts.length != 0))
        {
            if (Arrays.asList(this.ignoreHosts).contains("ALL"))
            {
                if (DEBUG)
                {
                    DEBUGGER.debug("ALL URIs are ignored. Breaking ...");
                }

                filterChain.doFilter(sRequest, sResponse);

                return;
            }

            for (String host : this.ignoreHosts)
            {
                String requestHost = host.trim();

                if (DEBUG)
                {
                    DEBUGGER.debug(host);
                    DEBUGGER.debug(requestHost);
                }

                if (StringUtils.equals(requestHost, sRequest.getServerName().trim()))
                {
                    if (DEBUG)
                    {
                        DEBUGGER.debug("Host found in ignore list. Not processing request!");
                    }

                    filterChain.doFilter(sRequest, sResponse);

                    return;
                }
            }
        }

        if ((this.ignoreURIs != null) && (this.ignoreURIs.length != 0))
        {
            if (Arrays.asList(this.ignoreURIs).contains("ALL"))
            {
                if (DEBUG)
                {
                    DEBUGGER.debug("ALL URIs are ignored. Breaking ...");
                }

                filterChain.doFilter(sRequest, sResponse);

                return;
            }

            // no hosts in ignore list
            for (String uri : this.ignoreURIs)
            {
                String requestURI = uri.trim();

                if (DEBUG)
                {
                    DEBUGGER.debug(uri);
                    DEBUGGER.debug(requestURI);
                }

                if (StringUtils.equals(requestURI, hRequest.getRequestURI().trim()))
                {
                    if (DEBUG)
                    {
                        DEBUGGER.debug("URI found in ignore list. Not processing request!");
                    }

                    filterChain.doFilter(sRequest, sResponse);

                    return;
                }
            }
        }

        if (hRequest.isSecure())
        {
            // Request came in on a secure channel or
            // the HTTP:DECRYPTED header is true
            // do nothing
            if (DEBUG)
            {
                DEBUGGER.debug("Filter not applied to request - already secured. No action taken.");
            }

            filterChain.doFilter(sRequest, sResponse);

            return;
        }

        // secure it
        StringBuilder redirectURL = new StringBuilder()
            .append(SSLEnforcementFilter.SECURE_URL_PREFIX)
            .append(sRequest.getServerName())
            .append(hRequest.getRequestURI());

        if (StringUtils.isNotBlank(hRequest.getQueryString()))
        {
            redirectURL.append("?" + hRequest.getQueryString());
        }

        if (DEBUG)
        {
            DEBUGGER.debug("redirectURL: {}", redirectURL);
        }

        hResponse.sendRedirect(URLEncoder.encode(redirectURL.toString(), systemConfig.getEncoding()));

        return;
    }

    public void destroy()
    {
        final String methodName = SSLEnforcementFilter.CNAME + "#destroy()";

        if (DEBUG)
        {
            DEBUGGER.debug(methodName);
        }

        this.ignoreHosts = null;
        this.ignoreURIs = null;
    }
}
