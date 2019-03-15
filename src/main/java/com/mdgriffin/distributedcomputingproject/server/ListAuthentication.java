package com.mdgriffin.distributedcomputingproject.server;

import java.util.HashMap;
import java.util.Map;

public class ListAuthentication implements  Authentication {

    private Map<String, String> validUsers;

    public ListAuthentication () {
        validUsers = new HashMap<>();
        validUsers.put("jdoe", "password123");
    }


    public boolean isValidUser (String username, String password) {
        String foundPassword = validUsers.get(username);

        if (foundPassword != null && foundPassword.equals(password)) {
            return true;
        }

        return false;
    }
}
