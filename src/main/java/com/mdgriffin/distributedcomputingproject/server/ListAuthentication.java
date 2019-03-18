package com.mdgriffin.distributedcomputingproject.server;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ListAuthentication implements  Authentication {

    private Map<String, String> validUsers;
    private Map<String, Session> sessions;

    public ListAuthentication () {
        validUsers = new HashMap<>();
        validUsers.put("jdoe", "password123");

        sessions = new HashMap<>();
    }

    public boolean isValidUser (String username, String password) {
        String foundPassword = validUsers.get(username);

        if (foundPassword != null && foundPassword.equals(password)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean hasActiveSession(String username, String sessionId) {
        if (sessions.containsKey(sessionId)) {
            Session session = sessions.get(sessionId);
            return session.getUsername().equals(username) && session.getExpiry().after(new Date());
        }

        return false;
    }

    @Override
    public Session login(String username, String password) throws AccountNotFoundException {
        if (!isValidUser(username, password)) {
            throw new AccountNotFoundException();
        }
        Session session = new SessionImpl(username);
        sessions.put(session.getId(), session);

        return session;
    }

    @Override
    public void logoff(String username) {
        sessions.entrySet().removeIf(entry -> entry.getValue().getUsername().equals(username));
    }

    @Override
    public Map<String, Session> getSessions() {
        return sessions;
    }
}
