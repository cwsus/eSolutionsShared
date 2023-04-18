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
package com.cws.esolutions.utility;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security
 * File: SecurityServiceConstants.java
 *
 * History
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 * Kevin Huntly         12/05/2008 13:36:09             Added method to process change requests
 */
/**
 * @author cws-khuntly
 * @version 1.0
 */
public final class UtilityConstants
{
	public static final String DS_CONTEXT = "java:comp/env/";

	// logging
    public static final String DEBUGGER = "UTILITY_DEBUGGER";
    public static final String AUDIT_LOGGER = "AUDIT_RECORDER";
    public static final String ERROR_LOGGER = "ERROR_RECORDER.";

    public static final String LINE_BREAK = System.getProperty("line.separator");
}
