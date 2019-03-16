package com.mdgriffin.distributedcomputingproject.server;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Map;

public interface Authentication {
    boolean isValidUser (String username, String password);
    boolean hasActiveSession (String username, String sessionId);
    Session login (String username, String password) throws AccountNotFoundException;
    void logoff (String username);
    Map<String, Session> getSessions ();
}
