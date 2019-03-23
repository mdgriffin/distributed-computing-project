package com.mdgriffin.distributedcomputingproject.server;

import javax.security.auth.login.AccountNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Map;

public interface Authentication {
    boolean isValidUser (String username, String password);
    boolean hasActiveSession (String username, String sessionId);
    boolean sessionIsActive (String sessionId);
    Session getActiveSession(String sessionId) throws AccessDeniedException;
    Session login (String username, String password) throws AccountNotFoundException;
    boolean logoff (String username);
    Map<String, Session> getSessions ();
}
