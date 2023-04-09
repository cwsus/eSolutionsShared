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
package com.cws.esolutions.utility.securityutils.processors.enums;
/*
 * Project: eSolutionsSecurity
 * Package: com.cws.esolutions.security.processors.enums
 * File: AuditType.java
 *
 * History
 *
 * Author               Date                            Comments
 * ----------------------------------------------------------------------------
 * cws-khuntly          11/23/2008 22:39:20             Created.
 */
/**
 * @author cws-khuntly
 * @version 1.0
 */
public enum AuditType
{
    NONE,

    // umm
    ENABLE_SERVICE,
    DISABLE_SERVICE,
    GET_SERVICE_STATUS,

    // authentication
    LOGON,
    LOGOFF,
    FORCELOGOFF,
    CHANGEPASS,
    RESETPASS,
    LOADSECURITY,
    VERIFYSECURITY,
    VERIFYRESET,
    VALIDATESECURITY,
    VALIDATETOKEN,
    GETQUESTIONS,
    VALIDATEOLR,
    ADDRESETENTRY,

    // user management
    CREATEUSER,
    MODIFYUSER,
    DELETEUSER,
    SUSPENDUSER,
    PSUSPENDUSER,
    LISTUSERS,
    UNSUSPENDUSER,
    LOCKUSER,
    UNLOCKUSER,
    ADDSECURITY,
    SHOWAUDIT,
    SEARCHACCOUNTS,
    LOADACCOUNT,
    CHANGEKEYS,
    GETAUDITENTRIES,
    CHANGESECURITY,
    CHANGECONTACT,
    CHANGEEMAIL,
    CHANGEROLE,
    MODIFYLOCKOUT,
    VALIDATERESET,

    // file security
    DECRYPTFILE,
    ENCRYPTFILE,
    VERIFYFILE,
    SIGNFILE,

    // emailing
    SENDEMAIL,

    // service messaging
    SHOWMESSAGES,
    LOADMESSAGE,
    ADDSVCMESSAGE,
    EDITSVCMESSAGE,

    // app mgmt
    ADDAPP,
    UPDATEAPP,
    DELETEAPP,
    LISTAPPS,
    LOADAPP,
    GETFILES,
    DEPLOYAPP,

    // sysmgmt
    KILL,
    EXECCMD,
    ADDSERVER,
    DELETESERVER,
    UPDATESERVER,
    LISTSERVERS,
    GETSERVER,
    TELNET,
    REMOTEDATE,
    NETSTAT,
    PROCESSLIST,
    STOP,
    START,
    RESTART,
    SUSPEND,

    // project mgmt
    ADDPROJECT,
    UPDATEPROJECT,
    LISTPROJECTS,
    LOADPROJECT,

    // platform mgmt
    ADDPLATFORM,
    DELETEPLATFORM,
    UPDATEPLATFORM,
    LISTPLATFORMS,
    LOADPLATFORM,

    // dns mgmt
    CREATEDNSRECORD,
    PUSHDNSRECORD,
    SITETXFR,
    LOADRECORD,

    // datacenter mgmt
    ADDDATACENTER,
    LISTDATACENTERS,
    LOADDATACENTER,
    UPDATEDATACENTER,
    DELETEDATACENTER,

    // certificate mgmt
    LISTCSR,
    GENERATECERT,
    APPLYCERT,

    // added to satisfy service tests
    // DO NOT REMOVE
    JUNIT;
}
