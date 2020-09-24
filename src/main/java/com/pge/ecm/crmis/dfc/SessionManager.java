package com.pge.ecm.crmis.dfc;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

/**
 * Custom session manager class to maintain the user and admin session managers.
 */
public class SessionManager {

    private IDfSessionManager adminSessionManager;
    private IDfClientX goClientx;

    //here we need docbase install owner credentials
    public SessionManager() throws DfException {
        goClientx = new DfClientX();
        IDfClient loClient = goClientx.getLocalClient();
        adminSessionManager = loClient.newSessionManager();
    }

    /**
     * Create a user session
     * @param docbase : name of the docbase
     * @param user
     * @param passwd
     * @return an instance of admin session (IDfSession)
     * @throws DfException if error occurs
     */
    public synchronized IDfSession getSession(String docbase, String user, String passwd) throws Exception {

        IDfLoginInfo loLoginInfo = goClientx.getLoginInfo();
        loLoginInfo.setUser(user);
        loLoginInfo.setPassword(passwd);
        // if an identity for this repository already exists, replace it silently
        if (adminSessionManager.hasIdentity(docbase)) {
            adminSessionManager.clearIdentity(docbase);
        }
        adminSessionManager.setIdentity(docbase, loLoginInfo);
        return adminSessionManager.getSession(docbase);
    }

    /**
     * ******** Unless required don't call this method ****************
     * ******** Always call getTicketedUserSession to get a login ticket *************
     * Create a new login ticket session for a user. Admin session needs to be created first in order
     * to create a new login ticket for the user so docbase installation owner credentials is required.
     * @param repository: name of the docbase
     * @param userName: Docbase user name
     * @return instance of IDfSession (new login ticket)s
     * @throws DfException if error occurs
     */
    public synchronized IDfSession getTicketedUserSession (IDfSession adminSession, String repository, String userName) throws DfException
    {
        // Obtains a login ticket for userName from a superuser session
        String ticket = adminSession.getLoginTicketForUser(userName);

        // set an identity in the user session manager using the
        // ticket in place of the password
        DfClientX clientx = new DfClientX();
        IDfLoginInfo loginInfo = clientx.getLoginInfo();
        loginInfo.setUser(userName);
        loginInfo.setPassword(ticket);
        // if an identity for this repository already exists, replace it silently
        if (adminSessionManager.hasIdentity(repository)) {
            adminSessionManager.clearIdentity(repository);
        }
        adminSessionManager.setIdentity(repository, loginInfo);
        // get the session and return it
        return adminSessionManager.getSession(repository);
    }

    /**
     * Release admin session
     * @param adminSession: instance of IDfSession
     * @return true if successfully release else false
     */
    public boolean releaseSession(IDfSession adminSession) {
        if (adminSessionManager != null && adminSession != null) {
            adminSessionManager.release(adminSession);
            return true;
        }
        return false;
    }

    /**
     * Flushes all the unused sessions associated with this Session Manager
     */
    public void flushSessions() {
        if (adminSessionManager != null)
            adminSessionManager.flushSessions();
    }
    /**
     * Release admin session
     * @param dfSession: instance of IDfSession
     * @return true if successfully release else false
     */
    public boolean releaseUserSession(IDfSession dfSession) {
        if (dfSession != null) {
            dfSession.getSessionManager().release(dfSession);
            return true;
        }
        return false;
    }

}
